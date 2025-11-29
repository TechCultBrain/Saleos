package org.techcult.scaleos.feature.product.data.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.techcult.scaleos.feature.product.data.local.dao.BrandDao
import org.techcult.scaleos.feature.product.data.mapper.toDomain
import org.techcult.scaleos.feature.product.data.mapper.toEntity
import org.techcult.scaleos.feature.product.domain.model.Brand
import org.techcult.scaleos.feature.product.domain.repository.BrandRepository
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter

class BrandRepositoryImpl(val brandDao: BrandDao): BrandRepository {
    override suspend fun upsertBrand(brand: Brand): Result<String, DataError.Local> {
        try {
            brandDao.insertBrand(brand.toEntity())
            return Result.Success(brand.id)
        } catch (e: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun observeBrands(
        filter: AvailabilityFilter,
        query: String?
    ): Flow<List<Brand>> {
        return brandDao.observeBrandFiltered(filter.dbValue,query).map {list->
            list.map { it.toDomain() }
        }
    }


}