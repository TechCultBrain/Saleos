@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.tax.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Entity(tableName = "tax_slabs")
data class TaxSlabEntity(
    @PrimaryKey val id: String=Uuid.random().toString(),
    val taxName: String,
    val description: String?,// e.g., "GST 18%", "Food 5%//
    val totalPercentage: Double,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String?,
    val updatedBy: String?,
    val isDeleted: Boolean = false
)