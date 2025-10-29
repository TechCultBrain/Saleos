package org.techcult.scaleos.feature.purchase.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "purchase_item",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseEntity::class,
            parentColumns = ["purchaseUuid"],
            childColumns = ["purchaseUuid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("purchaseUuid"), Index("productUuid")]
)
data class PurchaseItemEntity @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class) constructor(
    @PrimaryKey val purchaseItemUuid: String = Uuid.random().toString(),
    val purchaseUuid: String,

    val productUuid: String,                // Linked to Product
    val variantUuid: String? = null,        // Optional, if product has variants

    val quantity: Double,
    val unitPrice: Double,                  // Purchase rate before discount/tax
    val discountAmount: Double = 0.0,
    val taxAmount: Double = 0.0,
    val totalAmount: Double,                // Final line total after discounts and tax

    val batchNumber: String? = null,        // Optional for tracked stock
    val expiryDate: Long? = null,

    val taxSlabUuid: String? = null,        // Linked to Tax if applicable
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
