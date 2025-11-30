package org.techcult.scaleos.feature.supplier.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import kotlinx.datetime.LocalDateTime

data class SupplierWithPurchaseStats(
    @Embedded val supplier: SupplierEntity,

    @ColumnInfo(name = "lastPurchaseDate")
    val lastPurchaseDate: LocalDateTime?,

    @ColumnInfo(name = "totalPurchaseAmount")
    val totalPurchaseAmount: Double
)