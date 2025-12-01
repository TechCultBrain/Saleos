package org.techcult.scaleos.feature.supplier.data.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import org.techcult.scaleos.feature.supplier.data.local.dao.SupplierDao
import org.techcult.scaleos.feature.supplier.data.mapper.toSupplier
import org.techcult.scaleos.feature.supplier.data.mapper.toSupplierEntity
import org.techcult.scaleos.feature.supplier.domain.model.Supplier
import org.techcult.scaleos.feature.supplier.domain.repository.SupplierRepository

class SupplierRepositoryImpl(val supplierDao: SupplierDao) : SupplierRepository {
    override suspend fun upsertSupplier(supplier: Supplier): Result<String, DataError.Local> {
        try {
            supplierDao.insertSupplier(supplier.toSupplierEntity())
            return Result.Success(supplier.supplierId)

        } catch (e: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)

        }
    }

    override fun observeFilteredSupplier(
        query: String?,
        statusFilter: AvailabilityFilter
    ): Flow<List<Supplier>> {
        return supplierDao.observeSuppliersWithStatsFiltered(statusFilter.dbValue, query).map { s ->
            s.map {
                it.toSupplier()
            }

        }

    }

    override fun getSupplierCount(): Flow<Int> {
        return supplierDao.getSupplierCount()
        }
}