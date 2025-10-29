@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.auth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "roles")
data class RoleEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val name: String,   // e.g. Admin, Manager, Cashier
    val description: String? = null
)

