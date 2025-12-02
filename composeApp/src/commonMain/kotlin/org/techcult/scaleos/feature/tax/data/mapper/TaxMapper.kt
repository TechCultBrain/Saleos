package org.techcult.scaleos.feature.tax.data.mapper

import kotlinx.datetime.LocalDateTime
import org.techcult.scaleos.feature.tax.data.local.entity.TaxComponentEntity
import org.techcult.scaleos.feature.tax.data.local.entity.TaxSlabEntity
import org.techcult.scaleos.feature.tax.data.model.TaxSlabWithComponents
import org.techcult.scaleos.feature.tax.domain.model.TaxComponent
import org.techcult.scaleos.feature.tax.domain.model.TaxSlab

fun TaxSlabWithComponents.toDomain(): TaxSlab =
    TaxSlab(
        id = slab.id,
        name = slab.taxName,
        code = slab.taxCode,
        isActive = slab.isActive,
        description = slab.description,
        rate = slab.totalPercentage,
        components = components.map {
            TaxComponent(
                id = it.id,
                taxId = it.slabId,
                name = it.taxName,
                rate = it.rate,
                isActive = it.isActive
            )
        },
        createdAt = slab.createdAt,
        updatedAt = slab.updatedAt,
        createdBy = slab.createdBy,
        updatedBy = slab.updatedBy,

        )


fun TaxSlab.toEntity(): TaxSlabEntity {
    return TaxSlabEntity(
        id = this.id,
        taxName = this.name,
        taxCode = this.code,
        isActive = this.isActive,
        createdAt = this.createdAt,
        updatedAt = null,
        createdBy = null,
        updatedBy = null,
        description = this.description,
        totalPercentage = this.rate// update on save
    )
}

fun TaxComponent.toEntity(finalSlabId: Long): TaxComponentEntity {
    return TaxComponentEntity(
        id = this.id,
        slabId = finalSlabId,
        taxName = this.name,
        rate = this.rate,
        isActive = true
    )
}




