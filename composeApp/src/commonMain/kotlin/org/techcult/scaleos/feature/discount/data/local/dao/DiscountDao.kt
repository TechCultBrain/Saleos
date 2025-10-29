package org.techcult.scaleos.feature.discount.data.local.dao

import androidx.room.*
import org.techcult.scaleos.feature.discount.data.local.entity.DiscountEntity


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscountDao {

    // 🔹 Insert or replace discount
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscount(discount: DiscountEntity)

    // 🔹 Update existing discount
    @Update
    suspend fun updateDiscount(discount: DiscountEntity)

    // 🔹 Soft delete discount (mark as deleted)
    @Query("""
        UPDATE discounts 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteDiscount(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore a soft-deleted discount
    @Query("""
        UPDATE discounts 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreDiscount(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Activate / Deactivate discount
    @Query("""
        UPDATE discounts 
        SET isActive = :isActive, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun setDiscountActiveState(id: String, isActive: Boolean, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete discount (optional)
    @Query("DELETE FROM discounts WHERE id = :id")
    suspend fun deleteDiscountPermanently(id: String)

    // 🔹 Get all active, non-deleted discounts
    @Query("""
        SELECT * FROM discounts 
        WHERE isDeleted = 0 AND isActive = 1 
        ORDER BY effectiveFrom DESC
    """)
    fun getAllActiveDiscounts(): Flow<List<DiscountEntity>>

    // 🔹 Get all valid discounts (date-based)
    @Query("""
        SELECT * FROM discounts 
        WHERE isDeleted = 0 AND isActive = 1 
        AND (effectiveTo IS NULL OR effectiveTo >= :currentDate)
        ORDER BY effectiveFrom DESC
    """)
    fun getCurrentlyValidDiscounts(currentDate: String): Flow<List<DiscountEntity>>

    // 🔹 Get discount by ID
    @Query("SELECT * FROM discounts WHERE id = :id AND isDeleted = 0 LIMIT 1")
    suspend fun getDiscountById(id: String): DiscountEntity?

    // 🔹 Search discounts by name
    @Query("""
        SELECT * FROM discounts 
        WHERE isDeleted = 0 AND discName LIKE '%' || :query || '%' 
        ORDER BY effectiveFrom DESC
    """)
    fun searchDiscounts(query: String): Flow<List<DiscountEntity>>

    // 🔹 Get all (including deleted) for sync
    @Query("SELECT * FROM discounts ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<DiscountEntity>
}
