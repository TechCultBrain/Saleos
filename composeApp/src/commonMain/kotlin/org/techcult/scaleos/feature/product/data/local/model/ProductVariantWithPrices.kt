package org.techcult.scaleos.feature.product.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductPriceEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductVariantEntity

data class ProductVariantWithPrices(
    @Embedded val variant: ProductVariantEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "variantId"
    )
    val prices: List<ProductPriceEntity>
)