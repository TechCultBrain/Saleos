package com.techcult.pos.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductPriceEntity

@Dao
interface ProductPriceDao {

    // 🔹 Insert single price
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrice(price: ProductPriceEntity)

    // 🔹 Bulk insert (for sync/import)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(prices: List<ProductPriceEntity>)

    // 🔹 Update existing price
    @Update
    suspend fun updatePrice(price: ProductPriceEntity)

    // 🔹 Soft delete price
    @Query("""
        UPDATE product_prices 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeletePrice(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore deleted price
    @Query("""
        UPDATE product_prices 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restorePrice(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete price (optional)
    @Query("DELETE FROM product_prices WHERE id = :id")
    suspend fun deletePricePermanently(id: String)

    // 🔹 Get all active prices (non-deleted)
    @Query("SELECT * FROM product_prices WHERE isDeleted = 0 AND isActive = 1 ORDER BY updatedAt DESC")
    fun getAllPrices(): Flow<List<ProductPriceEntity>>

    // 🔹 Get prices for a specific variant
    @Query("""
        SELECT * FROM product_prices 
        WHERE variantId = :variantId AND isDeleted = 0 AND isActive = 1 
        ORDER BY minQty ASC
    """)
    fun getPricesByVariant(variantId: String): Flow<List<ProductPriceEntity>>

    // 🔹 Get all price tiers for billing calculation
    @Query("""
        SELECT * FROM product_prices 
        WHERE variantId = :variantId 
        AND isDeleted = 0 
        AND isActive = 1 
        AND (effectiveTo IS NULL OR effectiveTo >= :currentDate)
        ORDER BY minQty ASC
    """)
    suspend fun getValidPricesByVariant(variantId: String, currentDate: String): List<ProductPriceEntity>

    // 🔹 Get applicable price for billing (based on qty + type)
    @Query("""
        SELECT * FROM product_prices 
        WHERE variantId = :variantId 
        AND isDeleted = 0 
        AND isActive = 1 
        AND priceType = :priceType 
        AND minQty <= :quantity 
        AND (effectiveTo IS NULL OR effectiveTo >= :currentDate)
        ORDER BY minQty DESC 
        LIMIT 1
    """)
    suspend fun getApplicablePrice(
        variantId: String,
        priceType: String,
        quantity: Int,
        currentDate: String
    ): ProductPriceEntity?

    // 🔹 Search prices by price type or range
    @Query("""
        SELECT * FROM product_prices 
        WHERE isDeleted = 0 
        AND (priceType LIKE '%' || :query || '%' 
             OR sellingPrice LIKE '%' || :query || '%')
        ORDER BY updatedAt DESC
    """)
    fun searchPrices(query: String): Flow<List<ProductPriceEntity>>

    // 🔹 Get all for sync (including deleted)
    @Query("SELECT * FROM product_prices ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<ProductPriceEntity>

    // 🔹 Activate / Deactivate price tier
    @Query("""
        UPDATE product_prices 
        SET isActive = :isActive, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun setPriceActiveState(id: String, isActive: Boolean, updatedAt: String, updatedBy: String?)
}
