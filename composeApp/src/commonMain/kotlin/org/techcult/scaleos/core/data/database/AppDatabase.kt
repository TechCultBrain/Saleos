package org.techcult.scaleos.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.techcult.pos.data.dao.ProductPriceDao
import org.techcult.scaleos.feature.discount.data.local.dao.DiscountDao
import org.techcult.scaleos.feature.discount.data.local.entity.DiscountEntity
import org.techcult.scaleos.feature.product.data.local.dao.BrandDao
import org.techcult.scaleos.feature.product.data.local.dao.CategoryDao
import org.techcult.scaleos.feature.product.data.local.dao.DepartmentDao
import org.techcult.scaleos.feature.product.data.local.dao.ProductDao
import org.techcult.scaleos.feature.product.data.local.dao.ProductUnitConversionDao
import org.techcult.scaleos.feature.product.data.local.dao.ProductVariantDao
import org.techcult.scaleos.feature.product.data.local.dao.StockBatchDao
import org.techcult.scaleos.feature.product.data.local.dao.StockTransactionDao
import org.techcult.scaleos.feature.product.data.local.dao.UnitDao
import org.techcult.scaleos.feature.product.data.local.entity.BrandEntity
import org.techcult.scaleos.feature.product.data.local.entity.CategoryEntity
import org.techcult.scaleos.feature.product.data.local.entity.DepartmentEntity
import org.techcult.scaleos.feature.product.data.local.entity.UnitEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductPriceEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductUnitConversionEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.ProductVariantEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.StockBatchEntity
import org.techcult.scaleos.feature.product.data.local.entity.product.StockTransactionEntity
import org.techcult.scaleos.feature.supplier.data.local.dao.SupplierDao
import org.techcult.scaleos.feature.supplier.data.local.entity.SupplierEntity
import org.techcult.scaleos.feature.tax.data.local.dao.TaxSlabDao
import org.techcult.scaleos.feature.tax.data.local.entity.TaxComponentEntity
import org.techcult.scaleos.feature.tax.data.local.entity.TaxSlabEntity


@Database(
    entities = [
        CategoryEntity::class,
        DepartmentEntity::class,
        BrandEntity::class,
        SupplierEntity::class,
        UnitEntity::class,
        TaxSlabEntity::class,
        TaxComponentEntity::class,
        DiscountEntity::class,
        ProductEntity::class,
        ProductVariantEntity::class,
        ProductPriceEntity::class,
        ProductUnitConversionEntity::class,
        StockBatchEntity::class,
        StockTransactionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun brandDao(): BrandDao
    abstract fun supplierDao(): SupplierDao
    abstract fun unitDao(): UnitDao
    abstract fun taxSlabDao(): TaxSlabDao
    abstract fun discountDao(): DiscountDao
    abstract fun productDao(): ProductDao
    abstract fun productVariantDao(): ProductVariantDao
    abstract fun productPriceDao(): ProductPriceDao
    abstract fun productUnitConversionDao(): ProductUnitConversionDao
    abstract fun stockBatchDao(): StockBatchDao
    abstract fun stockTransactionDao(): StockTransactionDao

    companion object {
        const val DB_NAME = "scaleos11.db"
    }
}
