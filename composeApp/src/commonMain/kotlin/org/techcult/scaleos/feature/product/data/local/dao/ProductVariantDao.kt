package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductVariantEntity
import org.techcult.scaleos.feature.product.data.local.model.ProductVariantWithPrices

@Dao
interface ProductVariantDao {

    // 🔹 Insert a single variant
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariant(variant: ProductVariantEntity)

    // 🔹 Bulk insert (useful during sync or import)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariants(variants: List<ProductVariantEntity>)

    // 🔹 Update existing variant
    @Update
    suspend fun updateVariant(variant: ProductVariantEntity)

    // 🔹 Soft delete (mark as deleted)
    @Query(
        """
        UPDATE product_variants 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """
    )
    suspend fun softDeleteVariant(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore variant
    @Query(
        """
        UPDATE product_variants 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """
    )
    suspend fun restoreVariant(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete (optional)
    @Query("DELETE FROM product_variants WHERE id = :id")
    suspend fun deleteVariantPermanently(id: String)

    // 🔹 Get all active (non-deleted) variants
    @Query("SELECT * FROM product_variants WHERE isDeleted = 0 AND isActive = 1 ORDER BY updatedAt DESC")
    fun getAllVariants(): Flow<List<ProductVariantEntity>>

    // 🔹 Get variants by product
    @Query("SELECT * FROM product_variants WHERE productId = :productId AND isDeleted = 0 ORDER BY sellingPrice ASC")
    fun getVariantsByProduct(productId: String): Flow<List<ProductVariantEntity>>

    // 🔹 Get variant by ID
    @Query("SELECT * FROM product_variants WHERE id = :id AND isDeleted = 0 LIMIT 1")
    suspend fun getVariantById(id: String): ProductVariantEntity?

    // 🔹 Search variants by unit, color, size, or batch
    @Query(
        """
        SELECT * FROM product_variants 
        WHERE isDeleted = 0 AND (
            color LIKE '%' || :query || '%' OR 
            size LIKE '%' || :query || '%' OR 
            batchNo LIKE '%' || :query || '%'
        )
        ORDER BY updatedAt DESC
    """
    )
    fun searchVariants(query: String): Flow<List<ProductVariantEntity>>

    // 🔹 Relation Query — Variants + Prices
    @Transaction
    @Query("SELECT * FROM product_variants WHERE isDeleted = 0 AND productId = :productId")
    fun getVariantsWithPricesByProduct(productId: String): Flow<List<ProductVariantWithPrices>>

    // 🔹 Get all for sync (including deleted)
    @Query("SELECT * FROM product_variants ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<ProductVariantEntity>

    // 🔹 Deactivate / Activate variant
    @Query(
        """
        UPDATE product_variants 
        SET isActive = :isActive, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """
    )
    suspend fun setVariantActiveState(
        id: String,
        isActive: Boolean,
        updatedAt: String,
        updatedBy: String?
    )
}
