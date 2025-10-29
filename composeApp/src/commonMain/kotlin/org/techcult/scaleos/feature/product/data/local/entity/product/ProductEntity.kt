package org.techcult.scaleos.feature.product.data.local.entity.product

import androidx.room.*
import kotlinx.datetime.LocalDateTime
import org.techcult.scaleos.feature.product.data.local.entity.BrandEntity
import org.techcult.scaleos.feature.product.data.local.entity.CategoryEntity
import org.techcult.scaleos.feature.product.data.local.entity.DepartmentEntity
import org.techcult.scaleos.feature.supplier.data.local.entity.SupplierEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["categoryId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = DepartmentEntity::class, parentColumns = ["id"], childColumns = ["departmentId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = BrandEntity::class, parentColumns = ["id"], childColumns = ["brandId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = SupplierEntity::class, parentColumns = ["id"], childColumns = ["supplierId"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [
        Index("categoryId"), Index("departmentId"), Index("brandId"), Index("supplierId")
    ]
)
data class ProductEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String= Uuid.random().toString(),
    val productName: String,
    val description: String?,
    val productCode: String?,
    val hsnCode: String?,
    val barcode: String?,
    val categoryId: String?,
    val departmentId: String?,
    val brandId: String?,

    val supplierId: String?,
    val imageUrl: List<String>?,
    val tags: String?, // Stored as comma-separated values
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String,
    val updatedBy: String,
    val isDeleted: Boolean,
)
