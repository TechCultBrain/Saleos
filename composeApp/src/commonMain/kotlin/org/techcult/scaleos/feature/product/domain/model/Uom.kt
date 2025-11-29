package org.techcult.scaleos.feature.product.domain.model

import kotlinx.datetime.LocalDateTime

data class Uom(
    val id: String,
    val name: String,
    val symbol: String,
    val description: String? = null,
    val isAvailable: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val createdBy: String? = null,
    val updatedBy: String? = null
)