package org.techcult.scaleos.feature.product.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductUnitConversionEntity

@Dao
interface ProductUnitConversionDao {

    // 🔹 Insert a new unit conversion
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversion(conversion: ProductUnitConversionEntity)

    // 🔹 Bulk insert (for import/sync)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversions(conversions: List<ProductUnitConversionEntity>)

    // 🔹 Update existing conversion
    @Update
    suspend fun updateConversion(conversion: ProductUnitConversionEntity)

    // 🔹 Soft delete (mark as deleted)
    @Query("""
        UPDATE product_unit_conversions 
        SET isDeleted = 1, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun softDeleteConversion(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Restore deleted conversion
    @Query("""
        UPDATE product_unit_conversions 
        SET isDeleted = 0, updatedAt = :updatedAt, updatedBy = :updatedBy 
        WHERE id = :id
    """)
    suspend fun restoreConversion(id: String, updatedAt: String, updatedBy: String?)

    // 🔹 Permanently delete (optional)
    @Query("DELETE FROM product_unit_conversions WHERE id = :id")
    suspend fun deleteConversionPermanently(id: String)

    // 🔹 Get all active conversions (non-deleted)
    @Query("SELECT * FROM product_unit_conversions WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAllConversions(): Flow<List<ProductUnitConversionEntity>>

    // 🔹 Get conversions for a specific product
    @Query("""
        SELECT * FROM product_unit_conversions 
        WHERE productId = :productId AND isDeleted = 0 
        ORDER BY conversionFactor ASC
    """)
    fun getConversionsByProduct(productId: String): Flow<List<ProductUnitConversionEntity>>

    // 🔹 Get conversion between specific units for a product
    @Query("""
        SELECT * FROM product_unit_conversions 
        WHERE productId = :productId 
        AND unitId = :unitId 
        AND baseUnitId = :baseUnitId 
        AND isDeleted = 0 
        LIMIT 1
    """)
    suspend fun getConversionForUnits(
        productId: String,
        unitId: String,
        baseUnitId: String
    ): ProductUnitConversionEntity?

    // 🔹 Get supplier-specific conversion (optional)
    @Query("""
        SELECT * FROM product_unit_conversions 
        WHERE productId = :productId 
        AND unitId = :unitId 
        AND supplierId = :supplierId 
        AND isDeleted = 0 
        LIMIT 1
    """)
    suspend fun getConversionForSupplier(
        productId: String,
        unitId: String,
        supplierId: String
    ): ProductUnitConversionEntity?

    // 🔹 Search conversions by unit name or supplier
    @Query("""
        SELECT * FROM product_unit_conversions 
        WHERE isDeleted = 0 
        AND (unitId LIKE '%' || :query || '%' OR supplierId LIKE '%' || :query || '%')
        ORDER BY updatedAt DESC
    """)
    fun searchConversions(query: String): Flow<List<ProductUnitConversionEntity>>

    // 🔹 Get all for sync (including deleted)
    @Query("SELECT * FROM product_unit_conversions ORDER BY updatedAt DESC")
    suspend fun getAllForSync(): List<ProductUnitConversionEntity>
}
