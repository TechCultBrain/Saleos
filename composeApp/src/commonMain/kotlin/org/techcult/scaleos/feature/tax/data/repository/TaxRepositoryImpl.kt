package org.techcult.scaleos.feature.tax.data.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import org.techcult.scaleos.feature.tax.data.local.dao.TaxSlabDao
import org.techcult.scaleos.feature.tax.data.mapper.toDomain
import org.techcult.scaleos.feature.tax.data.mapper.toEntity
import org.techcult.scaleos.feature.tax.domain.model.TaxSlab
import org.techcult.scaleos.feature.tax.domain.repository.TaxRepository

class TaxRepositoryImpl(val taxSlabDao: TaxSlabDao) : TaxRepository {
    override suspend fun upsertTaxSlab(taxSlab: TaxSlab): Result<Long, DataError.Local> {
        try {


            val slabEntity = taxSlab.toEntity()

            // components will get correct slabId inside DAO, but we still map them here
            val componentEntities = taxSlab.components.map { comp ->
                // slabId will be overridden to final id in DAO
                comp.toEntity(finalSlabId = slabEntity.id.takeIf { it != 0L } ?: 0L)
            }

             taxSlabDao.upsertSlabWithComponents(
                slab = slabEntity,
                components = componentEntities
            )
            return Result.Success(slabEntity.id)
        } catch (ex: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)

        }
    }

    override fun observeFilteredActiveTax(
        isActive: AvailabilityFilter,
        query: String?
    ): Flow<List<TaxSlab>> {
        return taxSlabDao.observeTaxSlabsWithComponentsFiltered(isActive.dbValue, query).map { taxSlabs ->
            taxSlabs.map { it.toDomain() }

        }
    }
}