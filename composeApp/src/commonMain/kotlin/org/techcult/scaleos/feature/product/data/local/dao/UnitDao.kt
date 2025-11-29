package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.UnitEntity

@Dao
interface UnitDao {

    // 🔹 Insert a new unit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unit: UnitEntity)

    // 🔹 Update existing unit
    @Update
    suspend fun updateUnit(unit: UnitEntity)

    // 🔹 Soft delete (mark as deleted)
    @Query("""
        UPDATE units 
        SET isAvailable = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteUnit(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore a soft-deleted unit
    @Query("""
        UPDATE units 
        SET isAvailable = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreUnit(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete a unit (optional, use carefully)
    @Query("DELETE FROM units WHERE id = :id")
    suspend fun deleteUnitPermanently(id: String)

    // 🔹 Get all active (non-deleted) units
    @Query("SELECT * FROM units WHERE isAvailable = 0 ORDER BY name ASC")
    fun getAllUnits(): Flow<List<UnitEntity>>

    // 🔹 Get unit by ID
    @Query("SELECT * FROM units WHERE id = :id AND isAvailable = 0 LIMIT 1")
    suspend fun getUnitById(id: String): UnitEntity?

    // 🔹 Search units by name or symbol
    @Query("""
        SELECT * FROM units 
        WHERE   
            -- availability filter using CASE
            CASE 
                WHEN :availability = 0 THEN 1                            -- ALL
                WHEN :availability = 1 THEN isAvailable = 1               -- AVAILABLE
                WHEN :availability = 2 THEN isAvailable = 0               -- UNAVAILABLE
            END
            AND (
                :query IS NULL 
                OR :query = '' 
                OR LOWER(name) LIKE '%' || LOWER(:query) || '%' 
                OR LOWER(name) LIKE '%' || LOWER(:query) || '%'
            )
        ORDER BY name ASC
    """)
    fun observeUnitsFiltered(availability: Int, query: String?): Flow<List<UnitEntity>>

    // 🔹 Get all including deleted (for sync)
    @Query("SELECT * FROM units ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<UnitEntity>
}
