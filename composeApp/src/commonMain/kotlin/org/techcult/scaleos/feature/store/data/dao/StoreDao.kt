package org.techcult.scaleos.feature.store.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.techcult.scaleos.feature.store.data.entity.StoreEntity
import org.techcult.scaleos.feature.store.data.entity.StoreSettingsEntity

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: StoreEntity)

    @Update
    suspend fun updateStore(store: StoreEntity)

    @Query("SELECT * FROM stores WHERE id = :storeId LIMIT 1")
    suspend fun getStoreById(storeId: String): StoreEntity?

    @Query("SELECT * FROM stores")
    suspend fun getAllStores(): List<StoreEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoreSettings(settings: StoreSettingsEntity)

    @Query("SELECT * FROM store_settings WHERE storeId = :storeId LIMIT 1")
    suspend fun getStoreSettings(storeId: String): StoreSettingsEntity?
}
