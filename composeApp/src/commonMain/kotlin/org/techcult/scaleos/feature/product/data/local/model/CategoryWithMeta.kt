package org.techcult.scaleos.feature.product.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import org.techcult.scaleos.feature.product.data.local.entity.CategoryEntity

data class CategoryWithMeta(
    @Embedded val category: CategoryEntity,

    @ColumnInfo(name = "parentName")
    val parentName: String?,

    @ColumnInfo(name = "productCount")
    val productCount: Int
)
