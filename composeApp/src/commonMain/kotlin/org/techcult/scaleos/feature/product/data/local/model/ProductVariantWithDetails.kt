package org.techcult.scaleos.feature.product.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import org.techcult.scaleos.feature.discount.data.local.entity.DiscountEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductPriceEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductVariantEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.StockBatchEntity
import org.techcult.scaleos.feature.tax.data.local.entity.TaxSlabEntity

data class ProductVariantWithDetails(
    @Embedded val variant: ProductVariantEntity,

    // 🔹 Prices related to this variant
    @Relation(
        parentColumn = "id",
        entityColumn = "variantId"
    )
    val prices: List<ProductPriceEntity>,

    // 🔹 Stock batches linked to this variant
    @Relation(
        parentColumn = "id",
        entityColumn = "variantId"
    )
    val batches: List<StockBatchEntity>,
    // Tax & Discount linked via foreign keys
    @Relation(
        parentColumn = "taxSlabId",
        entityColumn = "id"
    )
    val taxSlab: TaxSlabEntity? = null,

    @Relation(
        parentColumn = "discountId",
        entityColumn = "id"
    )
    val discount: DiscountEntity? = null
)