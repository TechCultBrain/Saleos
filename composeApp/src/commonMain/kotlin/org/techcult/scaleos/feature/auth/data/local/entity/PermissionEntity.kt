@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.auth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "permissions")
data class PermissionEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val key: String,    // e.g. "sales.create", "inventory.view"
    val name: String,   // Human readable label
    val description: String? = null
)
