package org.techcult.scaleos.feature.supplier.data.local.dao

import org.techcult.scaleos.feature.supplier.data.local.entity.SupplierEntity


import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.supplier.data.local.entity.SupplierWithPurchaseStats

@Dao
interface SupplierDao {

    // 🔹 Insert new supplier
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier: SupplierEntity)

    // 🔹 Update supplier details
    @Update
    suspend fun updateSupplier(supplier: SupplierEntity)

    // 🔹 Soft delete supplier (mark as deleted)
    @Query(
        """
        UPDATE suppliers 
        SET isAvailable = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE supplierId = :id
    """
    )
    suspend fun softDeleteSupplier(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore soft-deleted supplier
    @Query(
        """
        UPDATE suppliers 
        SET isAvailable = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE supplierId = :id
    """
    )
    suspend fun restoreSupplier(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete supplier (only if required)
    @Query("DELETE FROM suppliers WHERE supplierId = :id")
    suspend fun deleteSupplierPermanently(id: String)

    // 🔹 Get all active (non-deleted) suppliers
    @Query("SELECT * FROM suppliers WHERE isAvailable = 0 ORDER BY supplierName ASC")
    fun getAllSuppliers(): Flow<List<SupplierEntity>>

    // 🔹 Get supplier by ID
    @Query("SELECT * FROM suppliers WHERE supplierId = :id AND isAvailable = 0 LIMIT 1")
    suspend fun getSupplierById(id: String): SupplierEntity?

    // 🔹 Search suppliers by name, email, or phone
    @Query(
        """
        SELECT * FROM suppliers 
        WHERE isAvailable = 0 
        AND (
            supplierName LIKE '%' || :query || '%' OR 
            emailId LIKE '%' || :query || '%' OR 
            contactNumber LIKE '%' || :query || '%'
        )
        ORDER BY supplierName ASC
    """
    )
    fun searchSuppliers(query: String): Flow<List<SupplierEntity>>

    // 🔹 Get all including deleted (for sync or admin)
    @Query("SELECT * FROM suppliers ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<SupplierEntity>

    @Query("""
        SELECT 
            s.*,
            MAX(p.purchaseDate) AS lastPurchaseDate,
            IFNULL(
                SUM(
                    CASE 
                        WHEN p.isCancelled = 0 THEN p.totalAmount
                        ELSE 0
                    END
                ),
                0.0
            ) AS totalPurchaseAmount
        FROM suppliers s
        LEFT JOIN purchase p ON p.supplierId = s.supplierId
        WHERE 
            -- status filter: 0 = ALL, 1 = ACTIVE, 2 = INACTIVE
            CASE 
                WHEN :statusFilter = 0 THEN 1
                WHEN :statusFilter = 1 THEN s.isAvailable = 1
                WHEN :statusFilter = 2 THEN s.isAvailable = 0
            END
            AND (
                :query IS NULL
                OR :query = ''
                OR LOWER(s.supplierName)  LIKE '%' || LOWER(:query) || '%'
                OR LOWER(s.contactNumber) LIKE '%' || LOWER(:query) || '%'
                OR LOWER(s.emailId) LIKE '%' || LOWER(:query) || '%'
            )
        GROUP BY s.supplierId
        ORDER BY s.supplierName COLLATE NOCASE
    """)
    fun observeSuppliersWithStatsFiltered(
        statusFilter: Int,     // SupplierStatusFilter.dbValue
        query: String?         // search text (optional)
    ): Flow<List<SupplierWithPurchaseStats>>
}

