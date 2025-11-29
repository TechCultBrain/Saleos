package org.techcult.scaleos.feature.product.domain.model

import kotlinx.datetime.LocalDateTime

data class Brand(
    val id: String,
    val brandName: String,
    val brandDescription: String? = null,
    val brandImage: String? = null,
    val isAvailable: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String? = null,
    val createdBy: String? = null,
)
