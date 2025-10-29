package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.product.StockBatchEntity

@Dao
interface StockBatchDao {

    // 🔹 Insert single stock batch
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockBatch(batch: StockBatchEntity)

    // 🔹 Bulk insert (for purchase/sync)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockBatches(batches: List<StockBatchEntity>)

    // 🔹 Update stock batch details
    @Update
    suspend fun updateStockBatch(batch: StockBatchEntity)

    // 🔹 Soft delete stock batch
    @Query("""
        UPDATE stock_batches 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteStockBatch(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore stock batch
    @Query("""
        UPDATE stock_batches 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreStockBatch(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete (optional)
    @Query("DELETE FROM stock_batches WHERE id = :id")
    suspend fun deleteStockBatchPermanently(id: String)

    // 🔹 Get all active (non-deleted) batches
    @Query("SELECT * FROM stock_batches WHERE isDeleted = 0 AND isActive = 1 ORDER BY updatedAt DESC")
    fun getAllActiveStockBatches(): Flow<List<StockBatchEntity>>

    // 🔹 Get all batches for a variant
    @Query("""
        SELECT * FROM stock_batches 
        WHERE variantId = :variantId AND isDeleted = 0 
        ORDER BY expiryDate ASC
    """)
    fun getBatchesByVariant(variantId: String): Flow<List<StockBatchEntity>>

    // 🔹 Get specific batch by ID
    @Query("SELECT * FROM stock_batches WHERE id = :id AND isDeleted = 0 LIMIT 1")
    suspend fun getStockBatchById(id: String): StockBatchEntity?

    // 🔹 Get non-expired batches for sales
    @Query("""
        SELECT * FROM stock_batches 
        WHERE variantId = :variantId 
        AND isDeleted = 0 
        AND isActive = 1 
        AND (expiryDate IS NULL OR expiryDate >= :currentDate)
        ORDER BY expiryDate ASC
    """)
    suspend fun getAvailableBatches(variantId: String, currentDate: String): List<StockBatchEntity>

    // 🔹 Search batch by batch number
    @Query("""
        SELECT * FROM stock_batches 
        WHERE isDeleted = 0 
        AND (batchNo LIKE '%' || :query || '%')
        ORDER BY updatedAt DESC
    """)
    fun searchStockBatches(query: String): Flow<List<StockBatchEntity>>

    // 🔹 Update current stock (after sale or purchase)
    @Query("""
        UPDATE stock_batches 
        SET currentQty = :newStock, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun updateStockQuantity(id: String, newStock: Double, updatedAt: String, updatedBy: String?)

    // 🔹 Activate or deactivate batch
    @Query("""
        UPDATE stock_batches 
        SET isActive = :isActive, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun setBatchActiveState(id: String, isActive: Boolean, updatedAt: String, updatedBy: String?)

    // 🔹 Get all (including deleted) for sync
    @Query("SELECT * FROM stock_batches ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<StockBatchEntity>
}
