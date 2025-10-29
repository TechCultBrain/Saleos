@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.product.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "brands")
data class BrandEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val brandName: String,
    val brandDescription: String? = null,
    val brandImage: String? = null,
    val isDeleted: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String,
    val createdBy: String,
)
