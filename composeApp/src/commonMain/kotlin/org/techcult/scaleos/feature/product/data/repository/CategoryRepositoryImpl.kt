@file:OptIn(ExperimentalTime::class)

package org.techcult.scaleos.feature.product.data.repository


import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.techcult.scaleos.feature.product.data.local.dao.CategoryDao
import org.techcult.scaleos.feature.product.data.mapper.toDomain
import org.techcult.scaleos.feature.product.data.mapper.toEntity
import org.techcult.scaleos.feature.product.domain.model.Category
import org.techcult.scaleos.feature.product.domain.repository.CategoryRepository
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {
    override suspend fun upsertCategory(category: Category): Result<String, DataError> {
        try {
            dao.insertCategory(
                category.toEntity()
            )
            return Result.Success(category.id)
        } catch (ex: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)

        }
    }


    override suspend fun deleteCategory(id: String, updatedBy: String?) {
        dao.softDeleteCategory(
            id,
            Clock.System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault()),
            updatedBy
        )
    }

    override suspend fun restoreCategory(id: String, updatedBy: String?) {
        dao.restoreCategory(
            id,
            Clock.System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault()),
            updatedBy
        )
    }

    override suspend fun deleteCategoryPermanently(id: String) {
        dao.deleteCategoryPermanently(id)
    }

    override fun getAllCategories(): Flow<List<Category>> =
        dao.getAllCategoriesWithMeta()
            .map { list -> list.map { it.toDomain() } }

    override fun searchCategories(query: String): Flow<List<Category>> =
        dao.getAllCategoriesWithMetaByName(query).map { list -> list.map { it.toDomain() } }

    override suspend fun getCategoryById(id: String): Category? =
        dao.getCategoryById(id)?.toDomain()

    override suspend fun getAllForSync(): List<Category> =
        dao.getAllForSync().map { it.toDomain() }

    override fun observeCategoriesFiltered(
        availability: AvailabilityFilter,
        query: String?
    ): Flow<List<Category>> {
        return dao.observeCategoriesFiltered(
            availability = availability.dbValue,
            query = query
        ).map { list -> list.map { it.toDomain() } }
    }

}
