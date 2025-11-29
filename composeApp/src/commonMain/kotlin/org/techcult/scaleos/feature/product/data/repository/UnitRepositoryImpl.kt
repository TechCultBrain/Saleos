package org.techcult.scaleos.feature.product.data.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.techcult.scaleos.feature.product.data.local.dao.UnitDao
import org.techcult.scaleos.feature.product.data.mapper.toDomain
import org.techcult.scaleos.feature.product.data.mapper.toEntity
import org.techcult.scaleos.feature.product.domain.model.Uom
import org.techcult.scaleos.feature.product.domain.repository.UnitRepository
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter

class UnitRepositoryImpl(val unitDao: UnitDao) : UnitRepository {
    override suspend fun upsertUnit(unit: Uom): Result<String, DataError.Local> {
        try {
            unitDao.insertUnit(unit.toEntity())
            return Result.Success(unit.id)

        } catch (ex: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun observeUnitsFiltered(
        filter: AvailabilityFilter,
        query: String?
    ): Flow<List<Uom>> {
        return unitDao.observeUnitsFiltered(filter.dbValue, query)
            .map { list -> list.map { it.toDomain() } }

    }
}