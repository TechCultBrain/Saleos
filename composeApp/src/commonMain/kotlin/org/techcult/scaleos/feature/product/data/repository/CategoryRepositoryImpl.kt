@file:OptIn(ExperimentalTime::class)

package org.techcult.scaleos.feature.product.data.repository


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.techcult.scaleos.feature.product.data.local.dao.CategoryDao
import org.techcult.scaleos.feature.product.data.mapper.toDomain
import org.techcult.scaleos.feature.product.data.mapper.toEntity
import org.techcult.scaleos.feature.product.domain.model.Category
import org.techcult.scaleos.feature.product.domain.repository.CategoryRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {
    override suspend fun upsertCategory(category: Category) {
        dao.insertCategory(
            category.toEntity().copy(
                updatedAt = Clock.System.now()
                    .toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
            )
        )
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
        dao.getAllCategories().map { list -> list.map { it.toDomain() } }

    override fun searchCategories(query: String): Flow<List<Category>> =
        dao.searchCategories(query).map { list -> list.map { it.toDomain() } }

    override suspend fun getCategoryById(id: String): Category? =
        dao.getCategoryById(id)?.toDomain()

    override suspend fun getAllForSync(): List<Category> =
        dao.getAllForSync().map { it.toDomain() }
}
