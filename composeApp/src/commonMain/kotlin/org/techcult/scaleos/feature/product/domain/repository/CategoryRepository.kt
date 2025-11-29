package org.techcult.scaleos.feature.product.domain.repository

import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.product.domain.model.Category
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter

interface CategoryRepository {

    // 🔹 Create or update a category
    suspend fun upsertCategory(category: Category): Result<String, DataError>

    // 🔹 Soft delete
    suspend fun deleteCategory(id: String, updatedBy: String?)

    // 🔹 Restore deleted category
    suspend fun restoreCategory(id: String, updatedBy: String?)

    // 🔹 Permanently delete (optional)
    suspend fun deleteCategoryPermanently(id: String)

    // 🔹 Get all active categories
     fun getAllCategories(): Flow<List<Category>>

    // 🔹 Search categories by name
    fun searchCategories(query: String): Flow<List<Category>>

    // 🔹 Get a single category by ID
    suspend fun getCategoryById(id: String): Category?

    // 🔹 Get all (including deleted) — for sync or admin
    suspend fun getAllForSync(): List<Category>

    fun observeCategoriesFiltered(
        availability: AvailabilityFilter,      // 0 = ALL, 1 = AVAILABLE, 2 = UNAVAILABLE
        query: String?          // nullable search text
    ): Flow<List<Category>>


}
