package org.techcult.scaleos.feature.product.domain.model

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class Product @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val name: String,
    val code: String? = null,
    val description: String? = null,
    val categoryId: String? = null,
    val departmentId: String? = null,
    val brandId: String? = null,
    val supplierId: String? = null,
    val barcode: String? = null,
    val productImage: String? = null,
    // Tax & Discount linkage
    val taxId: String? = null,
    val discountId: String? = null,
    // Variants & Status
    val hasVariants: Boolean = false,
    val isActive: Boolean = true,
    // Meta
    val createdAt: Long,
    val updatedAt: Long,
    val createdBy: String? = null,
    val updatedBy: String? = null


)