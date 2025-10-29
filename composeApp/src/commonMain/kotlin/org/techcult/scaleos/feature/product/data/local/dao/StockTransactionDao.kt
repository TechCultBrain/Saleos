package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.product.StockTransactionEntity

@Dao
interface StockTransactionDao {

    // 🔹 Insert new transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: StockTransactionEntity)

    // 🔹 Bulk insert (for batch updates)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<StockTransactionEntity>)

    // 🔹 Update existing transaction
    @Update
    suspend fun updateTransaction(transaction: StockTransactionEntity)

    // 🔹 Soft delete (mark as deleted)
    @Query("""
        UPDATE stock_transactions 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteTransaction(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore soft-deleted transaction
    @Query("""
        UPDATE stock_transactions 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreTransaction(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete (optional)
    @Query("DELETE FROM stock_transactions WHERE id = :id")
    suspend fun deleteTransactionPermanently(id: String)

    // 🔹 Get all active transactions
    @Query("SELECT * FROM stock_transactions WHERE isDeleted = 0 ORDER BY transactionDate DESC")
    fun getAllTransactions(): Flow<List<StockTransactionEntity>>

    // 🔹 Get transactions for a specific variant
    @Query("""
        SELECT * FROM stock_transactions 
        WHERE variantId = :variantId AND isDeleted = 0 
        ORDER BY transactionDate DESC
    """)
    fun getTransactionsByVariant(variantId: String): Flow<List<StockTransactionEntity>>

    // 🔹 Get transactions for a specific batch
    @Query("""
        SELECT * FROM stock_transactions 
        WHERE batchId = :batchId AND isDeleted = 0 
        ORDER BY transactionDate DESC
    """)
    fun getTransactionsByBatch(batchId: String): Flow<List<StockTransactionEntity>>

    // 🔹 Get transactions by type (e.g., SALE, PURCHASE)
    @Query("""
        SELECT * FROM stock_transactions 
        WHERE transactionType = :transactionType AND isDeleted = 0 
        ORDER BY transactionDate DESC
    """)
    fun getTransactionsByType(transactionType: String): Flow<List<StockTransactionEntity>>

    // 🔹 Get transaction by reference (for linking)
    @Query("""
        SELECT * FROM stock_transactions 
        WHERE referenceId = :referenceId AND isDeleted = 0 
        ORDER BY transactionDate DESC
    """)
    fun getTransactionsByReference(referenceId: String): Flow<List<StockTransactionEntity>>

    // 🔹 Search transactions by remarks, reference, or type
    @Query("""
        SELECT * FROM stock_transactions 
        WHERE isDeleted = 0 
        AND (remarks LIKE '%' || :query || '%' OR referenceType LIKE '%' || :query || '%' OR transactionType LIKE '%' || :query || '%')
        ORDER BY transactionDate DESC
    """)
    fun searchTransactions(query: String): Flow<List<StockTransactionEntity>>

    // 🔹 Get transactions for date range (for reports)
    @Query("""
        SELECT * FROM stock_transactions 
        WHERE isDeleted = 0 
        AND transactionDate BETWEEN :fromDate AND :toDate 
        ORDER BY transactionDate DESC
    """)
    suspend fun getTransactionsByDateRange(fromDate: String, toDate: String): List<StockTransactionEntity>

    // 🔹 Get all including deleted (for sync)
    @Query("SELECT * FROM stock_transactions ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<StockTransactionEntity>
}
