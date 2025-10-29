package org.techcult.scaleos.feature.purchase.data.local.dao

import androidx.room.*
import org.techcult.scaleos.feature.purchase.data.local.entity.PurchaseEntity
import org.techcult.scaleos.feature.purchase.data.local.entity.PurchaseItemEntity

@Dao
interface PurchaseDao {

    // ─────────────── Purchase ───────────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: PurchaseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchases(list: List<PurchaseEntity>)

    @Query("SELECT * FROM purchase ORDER BY purchaseDate DESC")
    suspend fun getAllPurchases(): List<PurchaseEntity>

    @Query("SELECT * FROM purchase WHERE purchaseUuid = :uuid")
    suspend fun getPurchaseById(uuid: String): PurchaseEntity?

    @Query("DELETE FROM purchase WHERE purchaseUuid = :uuid")
    suspend fun deletePurchaseById(uuid: String)


    // ─────────────── Purchase Items ───────────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseItem(item: PurchaseItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseItems(list: List<PurchaseItemEntity>)

    @Query("SELECT * FROM purchase_item WHERE purchaseUuid = :purchaseUuid")
    suspend fun getItemsByPurchaseId(purchaseUuid: String): List<PurchaseItemEntity>

    @Query("DELETE FROM purchase_item WHERE purchaseUuid = :purchaseUuid")
    suspend fun deleteItemsByPurchaseId(purchaseUuid: String)
}
