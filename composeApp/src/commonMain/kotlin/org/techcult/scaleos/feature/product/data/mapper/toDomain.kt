package org.techcult.scaleos.feature.product.data.mapper

import org.techcult.scaleos.feature.product.data.local.entity.CategoryEntity
import org.techcult.scaleos.feature.product.domain.model.Category

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    categoryName = categoryName,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    createdBy = createdBy,
    updatedBy = updatedBy,
    isDeleted = isDeleted
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    categoryName = categoryName,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    createdBy = createdBy,
    updatedBy = updatedBy,
    isDeleted = isDeleted,
    imageUrl = imageUrl
)
