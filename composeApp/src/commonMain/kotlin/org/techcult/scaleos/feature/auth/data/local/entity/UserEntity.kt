@file:OptIn(ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.auth.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = RoleEntity::class,
            parentColumns = ["id"],
            childColumns = ["roleId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("roleId")]
)
data class UserEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val username: String,
    val passwordHash: String,
    val email: String? = null,
    val phone: String? = null,
    val roleId: String,  // single role
    val storeId: String,
    val isActive: Boolean = true,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)

/*.........
| Aspect                                 | Design        |
| -------------------------------------- | ------------- |
| Each user has one role                 | ✅ Yes         |
| Each role has many permissions         | ✅ Yes         |
| Cross-ref between role and permissions | ✅ Implemented |
| DAO supports CRUD and relation queries | ✅ Complete    |
| Suitable for KMP Room + Sync           | ✅ Yes         |*/
