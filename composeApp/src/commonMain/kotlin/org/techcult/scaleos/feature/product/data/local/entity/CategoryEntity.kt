package org.techcult.scaleos.feature.product.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
@Entity(tableName = "categories")
data class CategoryEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String,
    val categoryName: String,
    val parentCategoryId: String? = null,
    val description: String?,
    val imageId: String?,
    val colorCode: Long? = null,
    val isDeleted: Boolean = false,
    val createdAt: LocalDateTime?=null,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String? =null,
    val createdBy: String? =null
)
