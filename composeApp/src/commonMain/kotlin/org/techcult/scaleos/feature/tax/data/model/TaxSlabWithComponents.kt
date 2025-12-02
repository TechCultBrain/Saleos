package org.techcult.scaleos.feature.tax.data.model

import androidx.room.Embedded
import androidx.room.Relation
import org.techcult.scaleos.feature.tax.data.local.entity.TaxComponentEntity
import org.techcult.scaleos.feature.tax.data.local.entity.TaxSlabEntity

data class TaxSlabWithComponents(
    @Embedded val slab: TaxSlabEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "slabId"
    )
    val components: List<TaxComponentEntity>
)