package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import org.techcult.scaleos.feature.product.data.local.entity.CategoryEntity
import org.techcult.scaleos.feature.product.data.local.model.CategoryWithMeta

@Dao
interface CategoryDao {

    // 🔹 Insert new category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    // 🔹 Update existing category details
    @Update
    suspend fun updateCategory(category: CategoryEntity)

    // 🔹 Soft delete (mark as deleted)
    @Query("UPDATE categories SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy WHERE id = :id")
    suspend fun softDeleteCategory(id: String, updatedAt: LocalDateTime, updatedBy: String?)

    // 🔹 Restore soft-deleted category
    @Query("UPDATE categories SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy WHERE id = :id")
    suspend fun restoreCategory(id: String, updatedAt: LocalDateTime, updatedBy: String?)

    // 🔹 Permanently delete (only if required)
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryPermanently(id: String)

    // 🔹 Get all active (non-deleted) categories
    @Query("SELECT * FROM categories WHERE isDeleted = 0 ORDER BY categoryName ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    // 🔹 Get category by ID
    @Query("SELECT * FROM categories WHERE id = :id AND isDeleted = 0 LIMIT 1")
    suspend fun getCategoryById(id: String): CategoryEntity?

    // 🔹 Search categories by name
    @Query("SELECT * FROM categories WHERE isDeleted = 0 AND categoryName LIKE '%' || :query || '%' ORDER BY categoryName ASC")
    fun searchCategories(query: String): Flow<List<CategoryEntity>>

    // 🔹 Fetch all including deleted (useful for syncing)
    @Query("SELECT * FROM categories ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<CategoryEntity>

    @Query("""
    SELECT 
        c.*, 
        p.categoryName AS parentName,
        (
            SELECT COUNT(*) 
            FROM products pr 
            WHERE pr.categoryId = c.id AND pr.isDeleted = 1
        ) AS productCount
    FROM categories c
    LEFT JOIN categories p ON c.parentCategoryId = p.id
    ORDER BY c.categoryName COLLATE NOCASE
""")
     fun getAllCategoriesWithMeta(): Flow<List<CategoryWithMeta>>

    @Query("""
    SELECT 
        c.*, 
        p.categoryName AS parentName,
        (
            SELECT COUNT(*) 
            FROM products pr 
            WHERE pr.categoryId = c.id AND pr.isDeleted = 1
        ) AS productCount
    FROM categories c
    LEFT JOIN categories p ON c.parentCategoryId = p.id
    WHERE c.id = :categoryId
""")
    suspend fun getCategoryWithMeta(categoryId: Long): CategoryWithMeta?

    @Query("""
    SELECT 
        c.*, 
        p.categoryName AS parentName,
        (
            SELECT COUNT(*) 
            FROM products pr 
            WHERE pr.categoryId = c.id AND pr.isDeleted = 1
        ) AS productCount
    FROM categories c
    LEFT JOIN categories p ON c.parentCategoryId = p.id
    WHERE c.categoryName LIKE '%' || :query || '%'
    ORDER BY c.categoryName COLLATE NOCASE
""")
    fun getAllCategoriesWithMetaByName(query: String): Flow<List<CategoryWithMeta>>


    @Query("""
        SELECT 
            c.*, 
            p.categoryName AS parentName,
            (
                SELECT COUNT(*) 
                FROM products pr 
                WHERE pr.categoryId = c.id AND pr.isDeleted = 1
            ) AS productCount
        FROM categories c
        LEFT JOIN categories p ON c.parentCategoryId = p.id
        WHERE 
            -- availability filter using CASE
            CASE 
                WHEN :availability = 0 THEN 1                            -- ALL
                WHEN :availability = 1 THEN c.isDeleted = 1               -- AVAILABLE
                WHEN :availability = 2 THEN c.isDeleted = 0               -- UNAVAILABLE
            END
            AND (
                :query IS NULL 
                OR :query = '' 
                OR LOWER(c.categoryName) LIKE '%' || LOWER(:query) || '%' 
                OR LOWER(p.categoryName) LIKE '%' || LOWER(:query) || '%'
            )
        ORDER BY c.categoryName COLLATE NOCASE
    """)
    fun observeCategoriesFiltered(
        availability: Int,      // 0 = ALL, 1 = AVAILABLE, 2 = UNAVAILABLE
        query: String?          // nullable search text
    ): Flow<List<CategoryWithMeta>>

}
