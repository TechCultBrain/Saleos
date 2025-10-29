package org.techcult.scaleos.feature.auth.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "role_permission_cross_ref",
    primaryKeys = ["roleId", "permissionId"],
    foreignKeys = [
        ForeignKey(
            entity = RoleEntity::class,
            parentColumns = ["id"],
            childColumns = ["roleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PermissionEntity::class,
            parentColumns = ["id"],
            childColumns = ["permissionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("roleId"), Index("permissionId")]
)
data class RolePermissionCrossRef(
    val roleId: String,
    val permissionId: String
)