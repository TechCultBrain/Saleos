package org.techcult.scaleos.feature.product.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductVariantEntity

data class ProductWithVariants(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId"
    )
    val variants: List<ProductVariantEntity>
)