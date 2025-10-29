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
    suspend fun insertTaxSlab(taxSlab: TaxSlabEntity)

    @Update
    suspend fun updateTaxSlab(taxSlab: TaxSlabEntity)

    // 🔹 Insert tax components (for dynamic taxes)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaxComponents(components: List<TaxComponentEntity>)

    // 🔹 Get all active tax slabs with their components
    @Transaction
    @Query("SELECT * FROM tax_slabs WHERE isDeleted = 0 AND isActive = 1 ORDER BY taxName ASC")
    fun getAllActiveTaxSlabsWithComponents(): Flow<List<TaxSlabWithComponents>>

    // 🔹 Get single slab by ID with components
    @Transaction
    @Query("SELECT * FROM tax_slabs WHERE id = :id AND isDeleted = 0 LIMIT 1")
    suspend fun getTaxSlabWithComponentsById(id: String): TaxSlabWithComponents?

    // 🔹 Soft delete slab
    @Query("""
        UPDATE tax_slabs 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteTaxSlab(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore deleted slab
    @Query("""
        UPDATE tax_slabs 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
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
        WHERE isDeleted = 0 AND taxName LIKE '%' || :query || '%' 
        ORDER BY taxName ASC
    """)
    fun searchTaxSlabs(query: String): Flow<List<TaxSlabEntity>>

    // 🔹 Get all including deleted (for sync)
    @Query("SELECT * FROM tax_slabs ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<TaxSlabEntity>

    // 🔹 Delete permanently (optional)
    @Query("DELETE FROM tax_slabs WHERE id = :id")
    suspend fun deleteTaxSlabPermanently(id: String)
}
