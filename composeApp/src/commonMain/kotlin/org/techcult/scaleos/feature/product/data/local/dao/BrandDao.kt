package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.BrandEntity

@Dao
interface BrandDao {

    // 🔹 Insert new brand
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: BrandEntity)

    // 🔹 Update existing brand details
    @Update
    suspend fun updateBrand(brand: BrandEntity)

    // 🔹 Soft delete brand
    @Query("""
        UPDATE brands 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteBrand(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore a soft-deleted brand
    @Query("""
        UPDATE brands 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreBrand(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete a brand (optional, use carefully)
    @Query("DELETE FROM brands WHERE id = :id")
    suspend fun deleteBrandPermanently(id: String)

    // 🔹 Get all active (non-deleted) brands
    @Query("SELECT * FROM brands WHERE isDeleted = 0 ORDER BY brandName ASC")
    fun getAllBrands(): Flow<List<BrandEntity>>

    // 🔹 Get brand by ID
    @Query("SELECT * FROM brands WHERE id = :id AND isDeleted = 0 LIMIT 1")
    suspend fun getBrandById(id: String): BrandEntity?

    // 🔹 Search brands by name
    @Query("""
        SELECT * FROM brands 
        WHERE isDeleted = 0 AND brandName LIKE '%' || :query || '%' 
        ORDER BY brandName ASC
    """)
    fun searchBrands(query: String): Flow<List<BrandEntity>>

    // 🔹 Get all including deleted (for sync or admin)
    @Query("SELECT * FROM brands ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<BrandEntity>
}
