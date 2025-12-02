@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.tax.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi

@Entity(
    tableName = "tax_components",
    foreignKeys = [
        ForeignKey(
            entity = TaxSlabEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("slabId")]
)
data class TaxComponentEntity(
    @PrimaryKey val id: Long,
    val slabId: Long,
    val taxName: String,          // e.g., "CGST", "SGST", "IGST", "CESS"
    val rate: Double,             // e.g., 9.0, 5.0
    val isActive: Boolean = true,

    )