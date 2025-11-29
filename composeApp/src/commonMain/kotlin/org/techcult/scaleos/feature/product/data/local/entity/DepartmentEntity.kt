package org.techcult.scaleos.feature.product.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "departments")
data class DepartmentEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String,
    val departmentName: String,
    val description: String?,
    val isAvailable: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String?=null,
    val createdBy: String?=null,

)
