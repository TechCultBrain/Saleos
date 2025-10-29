package org.techcult.scaleos.feature.product.data.local.entity.product

import androidx.room.*
import kotlinx.datetime.LocalDateTime
import org.techcult.scaleos.feature.product.data.local.entity.UnitEntity

@Entity(
    tableName = "product_unit_conversions",
    foreignKeys = [
        ForeignKey(entity = ProductEntity::class, parentColumns = ["id"], childColumns = ["productId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UnitEntity::class, parentColumns = ["id"], childColumns = ["unitId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UnitEntity::class, parentColumns = ["id"], childColumns = ["baseUnitId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("productId"), Index("unitId"), Index("baseUnitId")]
)
data class ProductUnitConversionEntity(
    @PrimaryKey  val id: String,
    val productId: String,
    val unitId: String,
    val baseUnitId: String,
    val conversionFactor: Double,
    val supplierId: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String?,
    val updatedBy: String?,
    val isDeleted: Boolean = false
)