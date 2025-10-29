@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.purchase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "purchase")
data class PurchaseEntity(
    @PrimaryKey val purchaseUuid: String = Uuid.random().toString(),

    val invoiceNumber: String? = null,          // Bill/Invoice number
    val supplierUuid: String? = null,           // Linked to Supplier module
    val storeUuid: String? = null,              // If multi-store setup
    val purchaseDate: Long = Clock.System.now().toEpochMilliseconds(),
    val dueDate: Long? = null,

    val totalAmount: Double = 0.0,              // Sum of all items (after discount & tax)
    val totalTax: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val netAmount: Double = 0.0,                // Final payable amount

    val paymentStatus: String = "PENDING",      // PENDING, PAID, PARTIAL
    val purchaseStatus: String = "ACTIVE",      // ACTIVE, RETURNED, CANCELLED

    val notes: String? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)