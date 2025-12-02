package org.techcult.scaleos.feature.tax.domain.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import org.techcult.scaleos.feature.tax.domain.model.TaxSlab

interface TaxRepository {


    suspend fun upsertTaxSlab(taxSlab: TaxSlab): Result<Long, DataError.Local>

    fun observeFilteredActiveTax(isActive: AvailabilityFilter,query: String?): kotlinx.coroutines.flow.Flow<List<TaxSlab>>


}