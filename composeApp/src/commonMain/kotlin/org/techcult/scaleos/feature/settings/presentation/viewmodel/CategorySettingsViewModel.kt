@file:OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)

package org.techcult.scaleos.feature.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techcult.salesman.core.domain.onError
import com.techcult.salesman.core.domain.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.techcult.scaleos.core.presentation.theme.ColorPalette
import org.techcult.scaleos.feature.product.domain.model.Category
import org.techcult.scaleos.feature.product.domain.repository.CategoryRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class CategorySettingsViewModel(val repo: CategoryRepository) : ViewModel() {


    private val filter = MutableStateFlow(AvailabilityFilter.ALL)
    private val searchQuery = MutableStateFlow<String?>(null)

    private val _state = MutableStateFlow(CategorySettingsState())
    val state = _state.asStateFlow()

    private val _event = Channel<CategoryEvents>()
    val event = _event.receiveAsFlow()

    val categories: StateFlow<List<Category>> = combine(filter, searchQuery) { f, q ->
        f to q
    }.onEach {
        _state.update {
            it.copy(isLoading = true)
        }
    }.flatMapLatest { (f, q) ->
        delay(1000)
        repo.observeCategoriesFiltered(f, q)
    }.onEach {
        _state.update {
            it.copy(isLoading = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun setFilter(newFilter: AvailabilityFilter) {
        filter.value = newFilter
    }


    fun setSearchQuery(text: String) {
        searchQuery.value = text.ifBlank { null }
    }


    init {

        loadCategory()
        // Sample data for now
    }

    private fun loadCategory() {
        viewModelScope.launch {
            delay(3000)
            repo.getAllCategories().collect { categories ->
                if (categories.isNotEmpty()) {
                    val parentCategories = categories.filter { it.parentId == null }
                    val mappedList = parentCategories.map { parent ->
                        mapOf(
                            "id" to parent.id, "name" to parent.categoryName
                        )

                    }.toMutableList()
                    mappedList.add(
                        0, mapOf("id" to "null", "name" to "None (Top Level Category)")
                    )

                    _state.update {
                        it.copy(
                            parentCategories = mappedList, isLoading = false
                        )
                    }


                } else {

                    val parentCategory = listOf(
                        mapOf(
                            "id" to "null", "name" to "None (Top Level Category)"
                        )
                    ).toMutableList()
                    _state.update {
                        it.copy(
                            parentCategories = parentCategory, isLoading = false
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun onEvent(event: CategorySettingsAction) {
        when (event) {


            is CategorySettingsAction.OnStatusFilterChange -> {

                setFilter(event.status)
                _state.update {
                    it.copy(
                        statusFilter = event.status.name
                    )
                }

            }

            is CategorySettingsAction.OnFilterClick -> {
                _state.update {
                    it.copy(
                        isBottomSheetOpen = event.isBottomSheetOpen
                    )
                }

            }

            is CategorySettingsAction.OnSearchTextChange -> {
                _state.update {
                    it.copy(
                        searchText = event.text
                    )
                }
                setSearchQuery(event.text)

            }


            CategorySettingsAction.OnAddCategoryClick -> {
                resetFields()
                _state.update {
                    it.copy(
                        showDialog = true, isEditMode = false
                    )
                }
            }

            CategorySettingsAction.OnDismissDialog -> _state.update { it.copy(showDialog = false) }
            is CategorySettingsAction.OnCategoryNameChange -> _state.update {
                it.copy(
                    categoryName = event.name
                )
            }

            is CategorySettingsAction.OnDescriptionChange -> _state.update {
                it.copy(
                    description = event.description
                )
            }

            is CategorySettingsAction.OnParentCategoryChange -> _state.update {
                it.copy(
                    parentId = event.id, parentCategory = event.name

                )
            }

            is CategorySettingsAction.OnIconChange -> _state.update { it.copy(selectedIcon = event.icon) }
            is CategorySettingsAction.OnColorChange -> _state.update { it.copy(selectedColor = event.color) }
            is CategorySettingsAction.OnAvailabilityChange -> _state.update {
                it.copy(
                    isAvailable = event.isAvailable
                )
            }

            CategorySettingsAction.CreateCategory -> {


                _state.update { it.copy(showDialog = false) }
                var category: Category?



                viewModelScope.launch {
                    try {
                        if (validateCategory()) {
                            if (!_state.value.isEditMode) {
                                category = Category(
                                    id = Uuid.random().toString(),
                                    categoryName = _state.value.categoryName,
                                    description = _state.value.description,
                                    parentId = if (_state.value.parentId == "null") null else _state.value.parentId,
                                    parentName = _state.value.parentCategory,
                                    imageName = _state.value.selectedIcon,
                                    colorCode = _state.value.selectedColor,
                                    createdAt = Clock.System.now()
                                        .toLocalDateTime(timeZone = TimeZone.currentSystemDefault()),
                                    updatedAt = Clock.System.now()
                                        .toLocalDateTime(timeZone = TimeZone.currentSystemDefault()),
                                    isAvailable = _state.value.isAvailable,
                                )

                            } else {
                                category = Category(
                                    id = _state.value.selectedCategoryId!!,
                                    categoryName = _state.value.categoryName,
                                    description = _state.value.description,
                                    parentId = if (_state.value.parentId == "null") null else _state.value.parentId,
                                    parentName = _state.value.parentCategory,
                                    imageName = _state.value.selectedIcon,
                                    colorCode = _state.value.selectedColor,
                                    updatedAt = Clock.System.now()
                                        .toLocalDateTime(timeZone = TimeZone.currentSystemDefault()),
                                    isAvailable = _state.value.isAvailable,
                                )
                            }
                                repo.upsertCategory(category = category).onSuccess {
                                    _event.send(CategoryEvents.OnSuccess(if (state.value.isEditMode) "Category Updated Successfully" else "Category Created Successfully"))

                                }.onError {
                                    _event.send(CategoryEvents.OnError("Error Occurred"))

                                }
                                resetFields()

                            }

                    } catch (e: Exception) {
                        viewModelScope.launch {
                            _event.send(CategoryEvents.OnError("Error Occurred"))
                        }
                    }


                }
            }



        is CategorySettingsAction.OnEditCategoryClick -> {
            _state.update {
                it.copy(
                    selectedCategoryId = event.category.id,
                    showDialog = true,
                    isEditMode = true,
                    categoryName = event.category.categoryName,
                    description = event.category.description.toString(),
                    parentCategory = if (event.category.parentId == null) "None (Top Level Category)" else event.category.parentName.toString(),
                    selectedIcon = event.category.imageName.toString(),
                    selectedColor = event.category.colorCode!!.toLong(),
                    isAvailable = event.category.isAvailable,
                    parentId = event.category.parentId.toString()
                )
            }


        }

        is CategorySettingsAction.OnDeleteCategoryClick -> {
            _state.update { it.copy(showDeleteDialog = true) }

        }

        CategorySettingsAction.DismissDeleteDialog -> _state.update {
            it.copy(
                showDeleteDialog = false
            )
        }

        CategorySettingsAction.DeleteCategory -> {
            // TODO: Delete category logic
            _state.update { it.copy(showDeleteDialog = false) }
            }


        else -> Unit
        }
    }


private fun resetFields() {
    _state.update {
        it.copy(
            categoryName = "",
            description = "",
            parentCategory = "None (Top Level Category)",
            parentId = null,
            selectedIcon = "bakery",
            selectedColor = ColorPalette.materialColors.first(),
            isAvailable = true,
            showDialog = false,
            isEditMode = false,
            selectedCategoryId = null,
            showDeleteDialog = false,
            searchText = "",


            )
    }
}


private fun validateCategory(): Boolean {
    val categoryName = _state.value.categoryName
    val description = _state.value.description
    val parentCategory = _state.value.parentCategory
    val selectedIcon = _state.value.selectedIcon
    return !(categoryName.isBlank() || description.isBlank() || parentCategory.isBlank() || selectedIcon.isBlank())

}
}


