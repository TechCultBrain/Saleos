package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.BrandEntity
import org.techcult.scaleos.feature.product.data.local.entity.DepartmentEntity

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
        SET isAvailable = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteBrand(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore a soft-deleted brand
    @Query("""
        UPDATE brands 
        SET isAvailable = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreBrand(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete a brand (optional, use carefully)
    @Query("DELETE FROM brands WHERE id = :id")
    suspend fun deleteBrandPermanently(id: String)

    // 🔹 Get all active (non-deleted) brands
    @Query("SELECT * FROM brands WHERE isAvailable = 0 ORDER BY brandName ASC")
    fun getAllBrands(): Flow<List<BrandEntity>>

    // 🔹 Get brand by ID
    @Query("SELECT * FROM brands WHERE id = :id AND isAvailable = 0 LIMIT 1")
    suspend fun getBrandById(id: String): BrandEntity?

    // 🔹 Search brands by name
    @Query("""
        SELECT * FROM brands 
        WHERE isAvailable = 0 AND brandName LIKE '%' || :query || '%' 
        ORDER BY brandName ASC
    """)
    fun searchBrands(query: String): Flow<List<BrandEntity>>

    // 🔹 Get all including deleted (for sync or admin)
    @Query("SELECT * FROM brands ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<BrandEntity>

    @Query("""
        SELECT * FROM brands 
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
                OR LOWER(brandName) LIKE '%' || LOWER(:query) || '%' 
                OR LOWER(brandName) LIKE '%' || LOWER(:query) || '%'
            )
        ORDER BY brandName ASC
    """)
    fun observeBrandFiltered(availability: Int, query: String?): Flow<List<BrandEntity>>
}
