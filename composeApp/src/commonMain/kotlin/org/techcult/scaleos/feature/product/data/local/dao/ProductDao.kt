package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.*

import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductEntity
import org.techcult.scaleos.feature.product.data.local.model.ProductWithFullDetails
import org.techcult.scaleos.feature.product.data.local.model.ProductWithVariants

@Dao
interface ProductDao {

    // 🔹 Insert or update product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    // 🔹 Bulk insert (for sync or import)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    // 🔹 Update product info
    @Update
    suspend fun updateProduct(product: ProductEntity)

    // 🔹 Soft delete product
    @Query("""
        UPDATE products 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteProduct(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore product
    @Query("""
        UPDATE products 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreProduct(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete (optional)
    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductPermanently(id: String)

    // 🔹 Get all active (non-deleted) products
    @Query("SELECT * FROM products WHERE isDeleted = 0 ORDER BY productName ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    // 🔹 Get product by ID
    @Query("SELECT * FROM products WHERE id = :id AND isDeleted = 0 LIMIT 1")
    suspend fun getProductById(id: String): ProductEntity?

    // 🔹 Search products by name, barcode, or tags
    @Query("""
        SELECT * FROM products 
        WHERE isDeleted = 0 
        AND (productName LIKE '%' || :query || '%' 
        OR barcode LIKE '%' || :query || '%' 
        OR tags LIKE '%' || :query || '%')
        ORDER BY productName ASC
    """)
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    // 🔹 Get all products by category
    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND isDeleted = 0 ORDER BY productName ASC")
    fun getProductsByCategory(categoryId: String): Flow<List<ProductEntity>>

    // 🔹 Get all products by brand
    @Query("SELECT * FROM products WHERE brandId = :brandId AND isDeleted = 0 ORDER BY productName ASC")
    fun getProductsByBrand(brandId: String): Flow<List<ProductEntity>>

    // 🔹 Get all products by supplier
    @Query("SELECT * FROM products WHERE supplierId = :supplierId AND isDeleted = 0 ORDER BY productName ASC")
    fun getProductsBySupplier(supplierId: String): Flow<List<ProductEntity>>

    // 🔹 Get all for sync (including deleted)
    @Query("SELECT * FROM products ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<ProductEntity>

    // 🔹 Relation query: Product + Variants
    @Transaction
    @Query("SELECT * FROM products WHERE isDeleted = 0 ORDER BY productName ASC")
    fun getProductsWithVariants(): Flow<List<ProductWithVariants>>

    // 🔹 Get specific product with variants
    @Transaction
    @Query("SELECT * FROM products WHERE id = :productId AND isDeleted = 0 LIMIT 1")
    suspend fun getProductWithVariantsById(productId: String): ProductWithVariants?

    @Transaction
    @Query("SELECT * FROM products WHERE isDeleted = 0")
    fun getAllProductsWithFullDetails(): Flow<List<ProductWithFullDetails>>

    @Transaction
    @Query("SELECT * FROM products WHERE id = :productId AND isDeleted = 0 LIMIT 1")
    suspend fun getProductWithFullDetailsById(productId: String): ProductWithFullDetails?

}
