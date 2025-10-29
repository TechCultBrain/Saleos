package org.techcult.scaleos.feature.auth.data.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.techcult.scaleos.feature.auth.data.local.entity.PermissionEntity
import org.techcult.scaleos.feature.auth.data.local.entity.RoleEntity
import org.techcult.scaleos.feature.auth.data.local.entity.RolePermissionCrossRef

data class RoleWithPermissions(
    @Embedded val role: RoleEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RolePermissionCrossRef::class,
            parentColumn = "roleId",
            entityColumn = "permissionId"
        )
    )
    val permissions: List<PermissionEntity>
)