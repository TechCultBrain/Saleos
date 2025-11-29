package org.techcult.scaleos.feature.product.data.mapper

import org.techcult.scaleos.feature.product.data.local.entity.UnitEntity
import org.techcult.scaleos.feature.product.domain.model.Uom

fun Uom.toEntity(): UnitEntity {
    return UnitEntity(
        id = id,
        name = name,
        symbol = symbol,
        description = description,
        isAvailable = isAvailable,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
    )
}

fun UnitEntity.toDomain(): Uom {
    return Uom(
        id = id,
        name = name,
        symbol = symbol,
        description = description,
        isAvailable = isAvailable,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
    )
}