package org.techcult.scaleos.feature.settings.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Folder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import io.ktor.client.request.invoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.techcult.scaleos.core.presentation.theme.ColorPalette
import org.techcult.scaleos.feature.product.domain.repository.CategoryRepository
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.CategoryItem

enum class CategoryTab {
    BasicInfo,
    VisualDesign,
    Settings
}

data class CategorySettingsState(
    val searchText: String = "",
    val statusFilter: String = "All Status",
    val sortFilter: String = "Name",
    val categories: List<CategoryItem> = emptyList(),
    val showDialog: Boolean = false,
    // Dialog state
    val currentStep: Int = 1,
    val selectedTab: CategoryTab = CategoryTab.BasicInfo,
    val categoryName: TextFieldValue = TextFieldValue(),
    val description: TextFieldValue = TextFieldValue(),
    val parentCategory: String = "None (Top Level Category)",
    val selectedIcon: ImageVector = Icons.Default.Folder,
    val selectedColor: Color = ColorPalette.materialColors.first(),
    val icons: List<ImageVector> = listOf(
        Icons.Default.Folder, Icons.Default.Category, Icons.Default.Bookmark,
    ),
    val colors: List<Color> = ColorPalette.materialColors
)

sealed interface CategorySettingsEvent {
    data class OnSearchTextChange(val text: String) : CategorySettingsEvent
    data class OnStatusFilterChange(val status: String) : CategorySettingsEvent
    data class OnSortFilterChange(val sort: String) : CategorySettingsEvent
    data object OnAddCategoryClick : CategorySettingsEvent
    data object OnDismissDialog : CategorySettingsEvent
    data class OnCategoryNameChange(val name: String) : CategorySettingsEvent
    data class OnDescriptionChange(val description: String) : CategorySettingsEvent
    data class OnParentCategoryChange(val parent: String) : CategorySettingsEvent
    data class OnIconChange(val icon: ImageVector) : CategorySettingsEvent
    data class OnColorChange(val color: Color) : CategorySettingsEvent
    data class OnTabChange(val tab: CategoryTab) : CategorySettingsEvent
    data object CreateCategory : CategorySettingsEvent
}

class CategorySettingsViewModel(val categoryRepository: CategoryRepository) : ViewModel() {

    private val _state = MutableStateFlow(CategorySettingsState())
    val state = _state.asStateFlow()

    init {
        // Sample data for now
        _state.update {
            it.copy(
                categories = listOf(
                    CategoryItem(1, "Bakery", "Fresh baked goods", "Food", 20,  "Active", "2024-01-20"),
                    CategoryItem(2, "Beverages", "All types of drinks and bever...", "Top Level", 25,  "Active", "2024-01-20", isTopLevel = true),
                    CategoryItem(3, "Coffee", "Coffee and coffee-based drin...", "Beverages", 15,  "Active", "2024-01-20"),
                    CategoryItem(4, "Dairy", "Milk, cheese, and dairy produ...", "Food", 15,  "Active", "2024-01-20")
                )
            )
        }
    }

    fun onEvent(event: CategorySettingsEvent) {
        when (event) {
            is CategorySettingsEvent.OnSearchTextChange -> _state.update { it.copy(searchText = event.text) }
            is CategorySettingsEvent.OnStatusFilterChange -> _state.update { it.copy(statusFilter = event.status) }
            is CategorySettingsEvent.OnSortFilterChange -> _state.update { it.copy(sortFilter = event.sort) }
            CategorySettingsEvent.OnAddCategoryClick -> _state.update { it.copy(showDialog = true) }
            CategorySettingsEvent.OnDismissDialog -> _state.update { it.copy(showDialog = false) }
            is CategorySettingsEvent.OnCategoryNameChange -> _state.update { it.copy(categoryName = TextFieldValue(text = event.name)) }
            is CategorySettingsEvent.OnDescriptionChange -> _state.update { it.copy(description = TextFieldValue(event.description)) }
            is CategorySettingsEvent.OnParentCategoryChange -> _state.update { it.copy(parentCategory = event.parent) }
            is CategorySettingsEvent.OnIconChange -> _state.update { it.copy(selectedIcon = event.icon) }
            is CategorySettingsEvent.OnColorChange -> _state.update { it.copy(selectedColor = event.color) }
            is CategorySettingsEvent.OnTabChange -> _state.update { it.copy(selectedTab = event.tab) }
            CategorySettingsEvent.CreateCategory -> {
                // TODO: Add category creation logic
                // For now, just dismiss the dialog
                _state.update { it.copy(showDialog = false) }
            }
        }
    }
}