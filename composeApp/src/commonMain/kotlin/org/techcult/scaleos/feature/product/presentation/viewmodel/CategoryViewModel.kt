package org.techcult.scaleos.feature.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.techcult.scaleos.feature.product.domain.model.Category
import org.techcult.scaleos.feature.product.domain.repository.CategoryRepository

class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {


    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect {

            }
        }

    }
}