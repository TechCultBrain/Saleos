package org.techcult.scaleos.feature.product.data.mapper

import org.techcult.scaleos.feature.product.data.local.entity.CategoryEntity
import org.techcult.scaleos.feature.product.data.local.model.CategoryWithMeta
import org.techcult.scaleos.feature.product.domain.model.Category

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    categoryName = categoryName,
    parentId = parentCategoryId,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    createdBy = createdBy,
    updatedBy = updatedBy,
    isAvailable = isDeleted,
    colorCode = colorCode,
    imageName = imageId,
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    categoryName = categoryName,
    parentCategoryId = parentId,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    createdBy = createdBy,
    updatedBy = updatedBy,
    isDeleted = isAvailable,
    imageId = imageName,
    colorCode = colorCode
)

 fun CategoryWithMeta.toDomain(): Category {
    return Category(
        id = category.id,
        categoryName = category.categoryName,
        parentId = category.parentCategoryId,
        parentName =parentName,
        imageName = category.imageId,
        colorCode = category.colorCode,
        description = category.description,
        productCount = productCount,
        isAvailable = category.isDeleted,
        createdAt = category.createdAt,
        updatedAt = category.updatedAt,
        createdBy = category.createdBy,
        updatedBy = category.updatedBy,
    )
}