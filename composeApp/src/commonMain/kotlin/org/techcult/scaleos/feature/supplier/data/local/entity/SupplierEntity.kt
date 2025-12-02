@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.supplier.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey val supplierId: String,
    val supplierName: String,
    val supplierCode: String,
    val supplierType: String,
    val supplierNotes: String? = null,
    // Contact Information
    val contactPerson: String?,
    val contactNumber: String?,
    val wssNumber: String?,
    val upiId: String?,
    val emailId: String?,
    val gstNumber: String?,
    // Address Details-
    val address: String?,
    val city: String?,
    val state: String?,
    val pinCode: String?,
    // Financial Details
    val openingBalance: Double = 0.0,
    val paymentTerms: String? = null,
    val creditLimit: Double=0.0,
    val supplyingBrands: List<String>? = null,
    val isAvailable: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String?,
    val updatedBy: String?,
)