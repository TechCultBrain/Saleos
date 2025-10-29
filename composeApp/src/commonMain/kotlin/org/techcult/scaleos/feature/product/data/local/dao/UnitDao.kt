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
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteUnit(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore a soft-deleted unit
    @Query("""
        UPDATE units 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreUnit(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete a unit (optional, use carefully)
    @Query("DELETE FROM units WHERE id = :id")
    suspend fun deleteUnitPermanently(id: String)

    // 🔹 Get all active (non-deleted) units
    @Query("SELECT * FROM units WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAllUnits(): Flow<List<UnitEntity>>

    // 🔹 Get unit by ID
    @Query("SELECT * FROM units WHERE id = :id AND isDeleted = 0 LIMIT 1")
    suspend fun getUnitById(id: String): UnitEntity?

    // 🔹 Search units by name or symbol
    @Query("""
        SELECT * FROM units 
        WHERE isDeleted = 0 
        AND (name LIKE '%' || :query || '%' OR symbol LIKE '%' || :query || '%') 
        ORDER BY name ASC
    """)
    fun searchUnits(query: String): Flow<List<UnitEntity>>

    // 🔹 Get all including deleted (for sync)
    @Query("SELECT * FROM units ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<UnitEntity>
}
