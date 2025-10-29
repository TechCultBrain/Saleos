@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.product.data.local.entity.product

import androidx.room.*
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "stock_transactions",
    foreignKeys = [
        ForeignKey(
            entity = ProductVariantEntity::class,
            parentColumns = ["id"],
            childColumns = ["variantId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StockBatchEntity::class,
            parentColumns = ["id"],
            childColumns = ["batchId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("variantId"), Index("batchId")]
)
data class StockTransactionEntity(
    @PrimaryKey val id: String,
    val variantId: String,
    val batchId: String?,                // optional (for non-batch items)
    val transactionType: String,         // e.g., PURCHASE, SALE, RETURN, ADJUSTMENT, TRANSFER
    val quantity: Double,
    val previousStock: Double,
    val newStock: Double,
    val referenceId: String?,            // PurchaseId / SaleId / AdjustmentId etc.
    val referenceType: String?,          // PURCHASE / SALE / ADJUSTMENT / TRANSFER
    val remarks: String?,
    val transactionDate: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String?,
    val updatedBy: String?,
    val isDeleted: Boolean = false
)

enum class StockTransactionType {
    PURCHASE,
    SALE,
    RETURN,
    ADJUSTMENT,
    DAMAGE
}
