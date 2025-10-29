package org.techcult.scaleos.feature.store.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.techcult.scaleos.feature.auth.data.local.entity.UserEntity

@Entity(
    tableName = "user_store_cross_ref",
    primaryKeys = ["userId", "storeId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = StoreEntity::class,
            parentColumns = ["id"],
            childColumns = ["storeId"],
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("userId"), Index("storeId")]
)
data class UserStoreCrossRef(
    val userId: String,
    val storeId: String
)