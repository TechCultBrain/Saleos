package org.techcult.scaleos.feature.tax.data.local.dao

import androidx.room.*

import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.tax.data.local.entity.TaxComponentEntity
import org.techcult.scaleos.feature.tax.data.local.entity.TaxSlabEntity
import org.techcult.scaleos.feature.tax.data.model.TaxSlabWithComponents

@Dao
interface TaxSlabDao {

    // 🔹 Insert or update a Tax Slab
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSlab(entity: TaxSlabEntity): Long

    @Query("SELECT * FROM tax_slabs WHERE id = :id")
    suspend fun getSlabById(id: Long): TaxSlabEntity?

    // --- Components ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceComponents(entities: List<TaxComponentEntity>)

    @Query("SELECT * FROM tax_components WHERE slabId = :slabId")
    suspend fun getComponentsForSlab(slabId: Long): List<TaxComponentEntity>

    @Query("DELETE FROM tax_components WHERE slabId = :slabId AND id IN (:ids)")
    suspend fun deleteComponentsByIds(slabId: Long, ids: List<Long>)

    @Query("DELETE FROM tax_components WHERE slabId = :slabId")
    suspend fun deleteAllComponentsForSlab(slabId: Long)
    @Update
    suspend fun updateTaxSlab(taxSlab: TaxSlabEntity)

    // 🔹 Insert tax components (for dynamic taxes)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaxComponents(components: List<TaxComponentEntity>)

    // 🔹 Get all active tax slabs with their components
    @Transaction
    @Query("SELECT * FROM tax_slabs WHERE isActive = 0 AND isActive = 1 ORDER BY taxName ASC")
    fun getAllActiveTaxSlabsWithComponents(): Flow<List<TaxSlabWithComponents>>

    // 🔹 Get single slab by ID with components
    @Transaction
    @Query("SELECT * FROM tax_slabs WHERE id = :id AND isActive = 0 LIMIT 1")
    suspend fun getTaxSlabWithComponentsById(id: String): TaxSlabWithComponents?

    // 🔹 Soft delete slab
    @Query("""
        UPDATE tax_slabs 
        SET isActive = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteTaxSlab(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore deleted slab
    @Query("""
        UPDATE tax_slabs 
        SET isActive = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreTaxSlab(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Deactivate / Activate
    @Query("""
        UPDATE tax_slabs 
        SET isActive = :isActive, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun setTaxSlabActiveState(id: String, isActive: Boolean, updatedAt: String, updatedBy: String?)

    // 🔹 Search slabs by name
    @Query("""
        SELECT * FROM tax_slabs 
        WHERE isActive = 0 AND taxName LIKE '%' || :query || '%' 
        ORDER BY taxName ASC
    """)
    fun searchTaxSlabs(query: String): Flow<List<TaxSlabEntity>>

    // 🔹 Get all including deleted (for sync)
    @Query("SELECT * FROM tax_slabs ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<TaxSlabEntity>

    // 🔹 Delete permanently (optional)
    @Query("DELETE FROM tax_slabs WHERE id = :id")
    suspend fun deleteTaxSlabPermanently(id: String)

    @Transaction
    @Query("""
        SELECT 
            t.*
        FROM tax_slabs t
        WHERE 
            -- availability filter: 0 = ALL, 1 = ACTIVE, 2 = INACTIVE
            CASE 
                WHEN :availability = 0 THEN 1
                WHEN :availability = 1 THEN t.isActive = 1
                WHEN :availability = 2 THEN t.isActive = 0
            END
            AND (
                :query IS NULL
                OR :query = ''
                OR LOWER(t.taxName) LIKE '%' || LOWER(:query) || '%'
                OR LOWER(IFNULL(t.taxCode, '')) LIKE '%' || LOWER(:query) || '%'
            )
        ORDER BY t.taxName COLLATE NOCASE
    """)
    fun observeTaxSlabsWithComponentsFiltered(
        availability: Int,    // TaxSlabAvailabilityFilter.dbValue
        query: String?        // search text
    ): kotlinx.coroutines.flow.Flow<List<TaxSlabWithComponents>>

    @Transaction
    suspend fun upsertSlabWithComponents(
        slab: TaxSlabEntity,
        components: List<TaxComponentEntity>
    ): Long {
        // 1) Upsert slab
        val newId = insertOrReplaceSlab(slab)
        val finalSlabId = if (slab.id.toLong() == 0L) newId else slab.id

        // 2) Prepare components with correct slabId
        val fixedComponents = components.map {
            it.copy(id = finalSlabId)
        }

        // 3) Delete components removed by user
        val existing = getComponentsForSlab(finalSlabId)
        val existingIds = existing.map { it.id }.toSet()
        val newIds = fixedComponents.map { it.id }.filter { it != 0L }.toSet()

        val idsToDelete = existingIds - newIds
        if (idsToDelete.isNotEmpty()) {
            deleteComponentsByIds(finalSlabId, idsToDelete.toList())
        }

        // 4) Upsert (insert/update) remaining components
        if (fixedComponents.isNotEmpty()) {
            insertOrReplaceComponents(fixedComponents)
        } else {
            // If no components at all, you may decide to delete all:
            deleteAllComponentsForSlab(finalSlabId)
        }

        return finalSlabId
    }
}
