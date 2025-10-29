@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.tax.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "tax_components",
    foreignKeys = [
        ForeignKey(
            entity = TaxSlabEntity::class,
            parentColumns = ["id"],
            childColumns = ["taxSlabId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("taxSlabId")]
)
data class TaxComponentEntity(
    @PrimaryKey val id: String,
    val taxSlabId: String,
    val taxName: String,          // e.g., "CGST", "SGST", "IGST", "CESS"
    val rate: Double,             // e.g., 9.0, 5.0
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String?,
    val updatedBy: String?,
    val isDeleted: Boolean = false
)