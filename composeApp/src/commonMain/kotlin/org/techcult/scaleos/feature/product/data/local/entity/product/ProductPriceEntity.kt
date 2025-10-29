package org.techcult.scaleos.feature.product.data.local.entity.product

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "product_prices",
    foreignKeys = [
        ForeignKey(
            entity = ProductVariantEntity::class,
            parentColumns = ["id"],
            childColumns = ["variantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("variantId")]
)
data class ProductPriceEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey  val id: String= Uuid.random().toString(),
    val variantId: String,
    val priceType: String, // "RETAIL", "WHOLESALE", "DEALER"
    val minQty: Int = 1,
    val sellingPrice: Double,
    val inclusiveTax: Boolean = true,
    val effectiveFrom: LocalDateTime,
    val effectiveTo: LocalDateTime?,
    val isActive: Boolean = true,
     val createdAt: LocalDateTime,
     val updatedAt: LocalDateTime,
     val createdBy: String?,
     val updatedBy: String?,
     val isDeleted: Boolean = false
)