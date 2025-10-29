package org.techcult.scaleos.feature.product.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "categories")
data class CategoryEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String= Uuid.random().toString(),
    val categoryName: String,
    val parentCategoryId: String?=null,
    val description: String?,
    val imageUrl: String?,
    val isDeleted: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String,
    val createdBy: String)
