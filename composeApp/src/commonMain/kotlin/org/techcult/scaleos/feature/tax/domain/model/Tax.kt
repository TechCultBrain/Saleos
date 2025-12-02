package org.techcult.scaleos.feature.tax.domain.model

import kotlinx.datetime.LocalDateTime

data class TaxSlab(
    val id: Long,
    val name: String,
    val code: String?,
    val description: String?,
    val rate: Double,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val createdBy: String?,
    val updatedBy: String?,
    val components: List<TaxComponent>
)

data class TaxComponent(
    val id: Long,
    val taxId: Long,
    var name: String,
    var rate: Double,
    val isActive: Boolean
)
