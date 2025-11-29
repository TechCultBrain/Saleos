package org.techcult.scaleos.feature.product.data.mapper

import org.techcult.scaleos.feature.product.data.local.entity.BrandEntity
import org.techcult.scaleos.feature.product.data.local.entity.UnitEntity
import org.techcult.scaleos.feature.product.domain.model.Brand
import org.techcult.scaleos.feature.product.domain.model.Uom


fun Brand.toEntity(): BrandEntity{
    return BrandEntity(
        id = id,
        brandName = brandName,
        brandDescription = brandDescription,
        brandImage = brandImage,
        isAvailable = isAvailable,
        createdAt = createdAt,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        createdBy = createdBy,
    )
}

fun BrandEntity.toDomain(): Brand {
    return Brand(
        id = id,
        brandName = brandName,
        brandDescription = brandDescription,
        brandImage = brandImage,
        isAvailable = isAvailable,
        createdAt = createdAt,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        createdBy = createdBy,
    )




}