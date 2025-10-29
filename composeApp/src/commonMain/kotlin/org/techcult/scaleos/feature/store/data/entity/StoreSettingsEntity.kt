package org.techcult.scaleos.feature.store.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "store_settings",
    foreignKeys = [ForeignKey(
        entity = StoreEntity::class,
        parentColumns = ["id"],
        childColumns = ["storeId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("storeId")]
)
data class StoreSettingsEntity @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val storeId: String,
    val printReceiptAutomatically: Boolean = true,
    val enableKOT: Boolean = false,
    val defaultTaxId: String? = null,
    val defaultDiscountId: String? = null,
    val defaultCurrency: String = "INR",
    val theme: String? = null,
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)