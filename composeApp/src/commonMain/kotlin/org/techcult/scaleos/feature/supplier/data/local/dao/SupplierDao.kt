package org.techcult.scaleos.feature.supplier.data.local.dao

import org.techcult.scaleos.feature.supplier.data.local.entity.SupplierEntity


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {

    // 🔹 Insert new supplier
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier: SupplierEntity)

    // 🔹 Update supplier details
    @Update
    suspend fun updateSupplier(supplier: SupplierEntity)

    // 🔹 Soft delete supplier (mark as deleted)
    @Query("""
        UPDATE suppliers 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteSupplier(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore soft-deleted supplier
    @Query("""
        UPDATE suppliers 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreSupplier(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete supplier (only if required)
    @Query("DELETE FROM suppliers WHERE id = :id")
    suspend fun deleteSupplierPermanently(id: String)

    // 🔹 Get all active (non-deleted) suppliers
    @Query("SELECT * FROM suppliers WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAllSuppliers(): Flow<List<SupplierEntity>>

    // 🔹 Get supplier by ID
    @Query("SELECT * FROM suppliers WHERE id = :id AND isDeleted = 0 LIMIT 1")
    suspend fun getSupplierById(id: String): SupplierEntity?

    // 🔹 Search suppliers by name, email, or phone
    @Query("""
        SELECT * FROM suppliers 
        WHERE isDeleted = 0 
        AND (
            name LIKE '%' || :query || '%' OR 
            email LIKE '%' || :query || '%' OR 
            contactNumber LIKE '%' || :query || '%'
        )
        ORDER BY name ASC
    """)
    fun searchSuppliers(query: String): Flow<List<SupplierEntity>>

    // 🔹 Get all including deleted (for sync or admin)
    @Query("SELECT * FROM suppliers ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<SupplierEntity>
}

