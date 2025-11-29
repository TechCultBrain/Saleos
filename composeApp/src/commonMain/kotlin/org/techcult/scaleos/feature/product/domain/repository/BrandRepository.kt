package org.techcult.scaleos.feature.product.domain.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.domain.model.Brand
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter

interface BrandRepository {


    suspend fun upsertBrand(brand: Brand): Result<String, DataError.Local>
    fun observeBrands(filter: AvailabilityFilter, query: String?): Flow<List<Brand>>


}