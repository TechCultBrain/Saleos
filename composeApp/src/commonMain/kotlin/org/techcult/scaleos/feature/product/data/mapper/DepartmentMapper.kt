package org.techcult.scaleos.feature.product.data.mapper

import org.techcult.scaleos.feature.product.data.local.entity.DepartmentEntity
import org.techcult.scaleos.feature.product.domain.model.Department


fun Department.toEntity(): DepartmentEntity {
    return DepartmentEntity(
        id = id,
        departmentName = departmentName,
        description = description,
        isAvailable = isAvailable,
        createdAt = createdAt,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        createdBy = createdBy,
    )
}


fun DepartmentEntity.toDomain(): Department {
    return Department(
        id = id,
        departmentName = departmentName,
        description = description,
        isAvailable = isAvailable,
        createdAt = createdAt,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        createdBy = createdBy,
    )
}