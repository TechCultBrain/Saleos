@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.product.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "units")
data class UnitEntity(
    @PrimaryKey  val id: String= Uuid.random().toString(),
    val name: String,     // "Piece", "Box", "Case", etc.
    val symbol: String?,  // "pcs", "box", etc.
    val isDeleted: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String,
    val createdBy: String,
)