package org.techcult.scaleos.feature.product.domain.model

import kotlinx.datetime.LocalDateTime

data class Department(
    val id: String,
    val departmentName: String,
    val description: String? = null,
    val isAvailable: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String? = null,
    val createdBy: String? = null,
    )
