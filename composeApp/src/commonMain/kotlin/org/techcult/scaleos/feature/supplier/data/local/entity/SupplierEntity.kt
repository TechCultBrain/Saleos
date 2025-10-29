@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.supplier.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey val id: String=Uuid.random().toString(),
    val name: String,
    val contactNumber: String?,
    val email: String?,
    val gstNumber: String?,      // Optional - useful for GST billing
    val address: String?,
    val city: String?,
    val state: String?,
    val pincode: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String?,
    val updatedBy: String?,
    val isDeleted: Boolean = false
)