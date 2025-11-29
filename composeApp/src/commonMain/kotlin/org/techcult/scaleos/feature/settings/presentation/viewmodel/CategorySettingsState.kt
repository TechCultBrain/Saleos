package org.techcult.scaleos.feature.settings.presentation.viewmodel

import org.techcult.scaleos.core.presentation.theme.AppIcon
import org.techcult.scaleos.core.presentation.theme.AppIcons
import org.techcult.scaleos.core.presentation.theme.ColorPalette
import org.techcult.scaleos.feature.product.domain.model.Category

data class CategorySettingsState(
    val selectedCategoryId:String?=null,
    val searchText: String = "",
    val statusFilter: String = "All Status",
    val categories: List<Category> = emptyList(),
    val parentCategories: MutableList<Map<String, String>> = mutableListOf(),
    val showDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isEditMode: Boolean = false,
    // Dialog state
    val categoryName: String = "",
    val description: String = "",
    val parentId: String? = null,
    val parentCategory: String = "None (Top Level Category)",
    val selectedIcon: String = "bakery",
    val selectedColor: Long = ColorPalette.materialColors.first(),
    val isAvailable: Boolean = true,
    val icons: List<AppIcon> = AppIcons.allIcons,
    val colors: List<Long> = ColorPalette.materialColors,
    val isLoading: Boolean = false,
    val isBottomSheetOpen: Boolean = false
)

sealed interface CategorySettingsAction {
    data class OnSearchTextChange(val text: String) : CategorySettingsAction
    data class OnStatusFilterChange(val status: AvailabilityFilter) : CategorySettingsAction
    data class OnSortFilterChange(val sort: String) : CategorySettingsAction
    data object OnAddCategoryClick : CategorySettingsAction
    data class OnFilterClick(val isBottomSheetOpen: Boolean) : CategorySettingsAction
    data object OnDismissDialog : CategorySettingsAction
    data class OnCategoryNameChange(val name: String) : CategorySettingsAction
    data class OnDescriptionChange(val description: String) : CategorySettingsAction
    data class OnParentCategoryChange(val id: String, val name: String) : CategorySettingsAction
    data class OnIconChange(val icon: String) : CategorySettingsAction
    data class OnColorChange(val color: Long) : CategorySettingsAction
    data class OnAvailabilityChange(val isAvailable: Boolean) : CategorySettingsAction
    data object CreateCategory : CategorySettingsAction
    data object OnBackClick : CategorySettingsAction
    data class OnEditCategoryClick(val category: Category) : CategorySettingsAction
    data class OnDeleteCategoryClick(val category: Category) : CategorySettingsAction
    data object DismissDeleteDialog : CategorySettingsAction
    data object DeleteCategory : CategorySettingsAction


}

enum class AvailabilityFilter(val dbValue: Int) {
    ALL(0),
    ACTIVE(1),
    INACTIVE(2)
}

sealed interface CategoryEvents{
    data class OnSuccess(val message: String) : CategoryEvents
    data class OnError(val message: String) : CategoryEvents


}