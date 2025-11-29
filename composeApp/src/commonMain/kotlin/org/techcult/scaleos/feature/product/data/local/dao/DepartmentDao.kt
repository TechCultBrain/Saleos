package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.DepartmentEntity
import org.techcult.scaleos.feature.product.data.local.entity.UnitEntity

@Dao
interface DepartmentDao {

    // 🔹 Insert new department
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartment(department: DepartmentEntity)

    // 🔹 Update department details
    @Update
    suspend fun updateDepartment(department: DepartmentEntity)

    // 🔹 Soft delete department
    @Query("""
        UPDATE departments 
        SET isAvailable = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteDepartment(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore soft deleted department
    @Query("""
        UPDATE departments 
        SET isAvailable = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreDepartment(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete (if required)
    @Query("DELETE FROM departments WHERE id = :id")
    suspend fun deleteDepartmentPermanently(id: String)

    // 🔹 Get all active (non-deleted) departments
    @Query("SELECT * FROM departments WHERE isAvailable = 0 ORDER BY departmentName ASC")
    fun getAllDepartments(): Flow<List<DepartmentEntity>>

    // 🔹 Get department by ID
    @Query("SELECT * FROM departments WHERE id = :id AND isAvailable = 0 LIMIT 1")
    suspend fun getDepartmentById(id: String): DepartmentEntity?

    // 🔹 Search departments by name
    @Query("""
        SELECT * FROM departments 
        WHERE isAvailable = 0 AND departmentName LIKE '%' || :query || '%' 
        ORDER BY departmentName ASC
    """)
    fun searchDepartments(query: String): Flow<List<DepartmentEntity>>

    // 🔹 Get all including deleted (for sync)
    @Query("SELECT * FROM departments ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<DepartmentEntity>


    @Query("""
        SELECT * FROM departments 
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
                OR LOWER(departmentName) LIKE '%' || LOWER(:query) || '%' 
                OR LOWER(departmentName) LIKE '%' || LOWER(:query) || '%'
            )
        ORDER BY departmentName ASC
    """)
    fun observeDepartmentFiltered(availability: Int, query: String?): Flow<List<DepartmentEntity>>
}
