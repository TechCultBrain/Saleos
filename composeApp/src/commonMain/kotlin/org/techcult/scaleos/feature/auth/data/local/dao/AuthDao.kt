package org.techcult.scaleos.feature.auth.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import org.techcult.scaleos.feature.auth.data.local.entity.PermissionEntity
import org.techcult.scaleos.feature.auth.data.local.entity.RoleEntity
import org.techcult.scaleos.feature.auth.data.local.entity.RolePermissionCrossRef
import org.techcult.scaleos.feature.auth.data.local.model.RoleWithPermissions
import org.techcult.scaleos.feature.auth.data.local.entity.UserEntity
import org.techcult.scaleos.feature.auth.data.local.model.UserWithRoleAndPermissions

@Dao
interface AuthDao {

    // ─────────────────────────────
    //  USER OPERATIONS
    // ─────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserWithRoleAndPermissions(userId: String): UserWithRoleAndPermissions?

    // ─────────────────────────────
    //  ROLE OPERATIONS
    // ─────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRole(role: RoleEntity)

    @Query("SELECT * FROM roles")
    suspend fun getAllRoles(): List<RoleEntity>

    @Query("SELECT * FROM roles WHERE id = :roleId LIMIT 1")
    suspend fun getRoleById(roleId: String): RoleEntity?

    @Transaction
    @Query("SELECT * FROM roles WHERE id = :roleId LIMIT 1")
    suspend fun getRoleWithPermissions(roleId: String): RoleWithPermissions?

    // ─────────────────────────────
    //  PERMISSION OPERATIONS
    // ─────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPermission(permission: PermissionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPermissions(permissions: List<PermissionEntity>)

    @Query("SELECT * FROM permissions")
    suspend fun getAllPermissions(): List<PermissionEntity>

    @Query("SELECT * FROM permissions WHERE id = :permissionId LIMIT 1")
    suspend fun getPermissionById(permissionId: String): PermissionEntity?

    // ─────────────────────────────
    //  ROLE ↔ PERMISSION CROSS REF
    // ─────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun assignPermissionToRole(crossRef: RolePermissionCrossRef)

    @Query("DELETE FROM role_permission_cross_ref WHERE roleId = :roleId")
    suspend fun clearRolePermissions(roleId: String)

    @Query("""
        SELECT p.* FROM permissions p
        INNER JOIN role_permission_cross_ref rpc ON p.id = rpc.permissionId
        WHERE rpc.roleId = :roleId
    """)
    suspend fun getPermissionsForRole(roleId: String): List<PermissionEntity>
}
