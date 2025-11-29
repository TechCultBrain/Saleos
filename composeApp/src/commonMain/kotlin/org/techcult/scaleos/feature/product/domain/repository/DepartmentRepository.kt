package org.techcult.scaleos.feature.product.domain.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.domain.model.Department
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter

interface DepartmentRepository {
    suspend fun upsertDepartment(department: Department): Result<String, DataError.Local>
    fun observeFilteredDepartment(filter: AvailabilityFilter, query: String?): Flow<List<Department>>

}