package org.techcult.scaleos.feature.auth.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import org.techcult.scaleos.feature.auth.data.local.entity.RoleEntity
import org.techcult.scaleos.feature.auth.data.local.entity.UserEntity

data class UserWithRole(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "roleId",
        entityColumn = "id"
    )
    val role: RoleEntity
)