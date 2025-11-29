package org.techcult.scaleos.feature.product.data.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.techcult.scaleos.feature.product.data.local.dao.DepartmentDao
import org.techcult.scaleos.feature.product.data.mapper.toDomain
import org.techcult.scaleos.feature.product.data.mapper.toEntity
import org.techcult.scaleos.feature.product.domain.model.Department
import org.techcult.scaleos.feature.product.domain.repository.DepartmentRepository
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter

class DepartmentRepositoryImpl(private val departmentDao: DepartmentDao) : DepartmentRepository {
    override suspend fun upsertDepartment(department: Department): Result<String, DataError.Local> {
        try {
            departmentDao.insertDepartment(department.toEntity())
            return Result.Success(department.id)

        } catch (e: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun observeFilteredDepartment(
        filter: AvailabilityFilter,
        query: String?
    ): Flow<List<Department>> {
        return departmentDao.observeDepartmentFiltered(filter.dbValue, query).map { departments ->
            departments.map { it.toDomain() }

        }

    }
}