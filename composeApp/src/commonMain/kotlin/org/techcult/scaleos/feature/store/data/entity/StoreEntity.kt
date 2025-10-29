@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.store.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val name: String,
    val code: String? = null,          // Short code for POS display
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val postalCode: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val gstNumber: String? = null,
    val logoUrl: String? = null,
    val businessType: String,          // retail, restaurant, textile
    val currencyCode: String = "INR",
    val isActive: Boolean = true,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)
/*
| Design Area                                    | Decision                        |
| ---------------------------------------------- | ------------------------------- |
| Each store can have many users                 | ✅ Yes                           |
| Each user belongs to one store (default)       | ✅ Yes                           |
| Admins can optionally access multiple stores   | ⚙️ via `UserStoreCrossRef`      |
| Store settings separated                       | ✅ Yes (clean config separation) |
| Links cleanly to tax, inventory, receipt later | ✅ Ready                         |*/

