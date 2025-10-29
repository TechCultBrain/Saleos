package org.techcult.scaleos.feature.product.data.local.entity.product

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import org.techcult.scaleos.feature.discount.data.local.entity.DiscountEntity
import org.techcult.scaleos.feature.tax.data.local.entity.TaxSlabEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "product_variants",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TaxSlabEntity::class,
            parentColumns = ["id"],
            childColumns = ["taxSlabId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = DiscountEntity::class,
            parentColumns = ["id"],
            childColumns = ["discountId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["productId"]),
        Index(value = ["taxSlabId"]),
        Index(value = ["discountId"])
    ]
)
data class ProductVariantEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val productId: String,
    val unitId: String?,
    val size: String?,
    val color: String?,
    val batchNo: String?,
    val expiryDate: LocalDateTime?,
    val purchasePrice: Double,
    val mrp: Double,
    val profitPercentage: Double,
    val productRate: Double,
    val sellingPrice: Double,
    val inclusiveTax: Boolean,
    val taxSlabId: String?,
    val discountId: String?,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String?,
    val updatedBy: String?,
    val isDeleted: Boolean = false
)
