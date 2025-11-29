package org.techcult.scaleos.feature.product.domain.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import org.techcult.scaleos.feature.product.domain.model.Uom
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter

interface UnitRepository {

    suspend fun upsertUnit(unit: Uom): Result<String, DataError.Local>
    fun observeUnitsFiltered(
        filter: AvailabilityFilter,
        query: String?
    ): kotlinx.coroutines.flow.Flow<List<Uom>>


}