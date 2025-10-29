package org.techcult.scaleos.feature.discount.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "discounts")
data class DiscountEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String=Uuid.random().toString(),
    val discName: String,                    // e.g., "Festival Discount"
    val type: String,                    // "PERCENTAGE" or "FIXED"
    val value: Double,                   // 10.0 means 10% or ₹10 depending on type
    val effectiveFrom: LocalDateTime,
    val effectiveTo: LocalDateTime?,
    val applicableTo: String?,           // e.g., "Retail", "Wholesale", "All"
    val minPurchaseAmount: Double?,      // optional: min bill for discount
    val isActive: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String?,
    val updatedBy: String?,
    val isDeleted: Boolean = false
)