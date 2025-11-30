package org.techcult.scaleos.feature.supplier.domain.model

import kotlinx.datetime.LocalDateTime

data class Supplier(
    val supplierId: String,
    val supplierName: String,
    val supplierCode: String,
    val supplierType: String,
    val supplierNotes: String? = null,
    val contactPerson: String? = null,
    val contactNumber: String?,
    val wssNumber: String?,
    val upiId: String?,
    val email: String?,
    val gstNumber: String?,      // Optional - useful for GST billing
    val address: String?,
    val city: String?,
    val state: String?,
    val pinCode: String?,
    val openingBalance: Double = 0.0,
    val supplyingBrands: List<String>? = null,
    val isAvailable: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String?,
    val updatedBy: String?,
    val lastOrderDate: LocalDateTime? = null,
    val totalPurchases: Double = 0.0,
)
