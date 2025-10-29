@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.product.data.local.entity.product

import androidx.room.*
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "stock_batches",
    foreignKeys = [
        ForeignKey(entity = ProductVariantEntity::class, parentColumns = ["id"], childColumns = ["variantId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("variantId"), Index("batchNo")]
)
data class StockBatchEntity(
    @PrimaryKey val id: String= Uuid.random().toString(),
    val variantId: String,
    val batchNo: String?,
    val purchasePrice: Double,
    val sellingPrice: Double?,
    val mrp: Double?,
    val expiryDate: LocalDateTime?,
    val openingQty: Double,          // Opening balance
    val currentQty: Double,          // Current balance
    val unitId: String?,             // unit of stock (e.g., pcs, kg)
    val warehouseId: String?,        // optional for multi-location
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String?,
    val updatedBy: String?,
    val isDeleted: Boolean = false,
    val isActive: Boolean = true,

    )