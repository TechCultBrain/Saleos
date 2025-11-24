package org.techcult.scaleos.feature.product.domain.model

import kotlinx.datetime.LocalDateTime

data class Category(
    val id: String,
    val categoryName: String,
    val parentId: String? = null,
    val parentName: String? = null,
    val description: String? = null,
    val productCount: Int=0,
    val imageName: String? = null,
    val colorCode: Long? = null,
    val createdAt: LocalDateTime?=null,
    val updatedAt: LocalDateTime?=null,
    val createdBy: String?=null,
    val updatedBy: String?=null,
    val isAvailable: Boolean = false,
)