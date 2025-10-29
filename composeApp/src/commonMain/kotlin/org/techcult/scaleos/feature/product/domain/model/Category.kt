package org.techcult.scaleos.feature.product.domain.model

import kotlinx.datetime.LocalDateTime

data class Category(
    val id: String,
    val categoryName: String,
    val description: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val createdBy: String,
    val updatedBy: String,
    val isDeleted: Boolean = false,
    val imageUrl: String?=null
)