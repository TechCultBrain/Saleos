@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class, ExperimentalUuidApi::class)

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
import org.techcult.scaleos.feature.product.domain.model.Brand
import org.techcult.scaleos.feature.product.domain.repository.BrandRepository
import org.techcult.scaleos.feature.settings.presentation.viewmodel.BrandSettingsEvents.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class BrandSettingsViewModel(val repo: BrandRepository) : ViewModel() {


    private val filter = MutableStateFlow(AvailabilityFilter.ALL)
    private val searchQuery = MutableStateFlow<String?>(null)
    private val _state = MutableStateFlow(BrandSettingState())
    val state = _state.asStateFlow()

    private val _event = Channel<UnitSettingsEvents>()
    val event = _event.receiveAsFlow()

    val brandList: StateFlow<List<Brand>> = combine(filter, searchQuery) { f, q ->
        f to q
    }.onEach {
        _state.update {
            it.copy(isLoading = true)
        }
    }.flatMapLatest { (f, q) ->
        delay(1000)
        repo.observeBrands(f, q)
    }.onEach {
        _state.update {
            it.copy(isLoading = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )


    fun onAction(actions: BrandSettingActions) {
        when (actions) {
            is BrandSettingActions.OnAddDialogClick -> {

                _state.update {
                    it.copy(isAddDialogOpen = actions.isAddDialogOpen)
                }
            }

            is BrandSettingActions.OnDescriptionChange -> {
                _state.update {
                    it.copy(description = actions.description)
                }
            }

            BrandSettingActions.OnSaveClick -> {
                var brand: Brand?
                viewModelScope.launch {

                    if (validateFields()) {
                        if (state.value.isEditMode) {
                            brand = Brand(
                                id = state.value.brandId!!,
                                brandName = state.value.brandName,
                                brandImage = state.value.brandImage,
                                isAvailable = state.value.isAvailable,
                                createdAt = state.value.createdDate,
                                updatedAt = Clock.System.now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                            )

                        } else {
                            brand = Brand(
                                id = Uuid.random().toString(),
                                brandName = state.value.brandName,
                                brandImage = state.value.brandImage,
                                isAvailable = state.value.isAvailable,
                                createdAt = Clock.System.now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                                updatedAt = null
                            )

                        }
                        repo.upsertBrand(brand).onSuccess {
                            _state.update {
                                it.copy(isAddDialogOpen = false)
                            }
                            _event.send(OnSuccess(if (state.value.isEditMode) "Brand Updated" else "Brand Added"))
                            resetFields()

                        }
                            .onError { error ->
                                _state.update {
                                    it.copy(isAddDialogOpen = false)
                                }
                                _event.send(OnFailure(error.name))
                                resetFields()

                            }


                    }


                }

            }

            is BrandSettingActions.OnAvailabilityChange -> {
                _state.update {
                    it.copy(isAvailable = actions.isAvailable)
                }
            }

            BrandSettingActions.OnDismissDialog -> {
                _state.update {
                    it.copy(isAddDialogOpen = false)
                }
            }

            is BrandSettingActions.OnEditOptionClick -> {
                _state.update {
                    it.copy(
                        isEditMode = true,
                        isAddDialogOpen = true,
                        brandName = actions.brand.brandName,
                        brandImage = actions.brand.brandImage,
                        isAvailable = actions.brand.isAvailable,
                        brandId = actions.brand.id,
                        createdDate = actions.brand.createdAt,
                    )
                }
            }

            is BrandSettingActions.OnFilterButtonClick -> {
                _state.update {
                    it.copy(isStatusFilter = actions.isStatusFiler)
                }
            }

            is BrandSettingActions.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = actions.query)
                }
                searchQuery.value = actions.query.ifBlank { null }

            }

            is BrandSettingActions.OnStatusFilterChange -> {
                filter.value = actions.filter
                _state.update {
                    it.copy(statusFilter = actions.filter)
                }
            }

            is BrandSettingActions.OnBrandNameChange -> {
                _state.update {
                    it.copy(brandName = actions.name)
                }
            }

            is BrandSettingActions.OnBrandImageChange -> {
                _state.update {
                    it.copy(brandImage = actions.url, isAddImageDialogOpen = false)
                }
            }
            is BrandSettingActions.OnBrandImageAddClick->
            {
                _state.update {
                    it.copy(isAddImageDialogOpen = actions.isAddImage)
                }

            }
            is BrandSettingActions.OnImagePreviewClick -> {
                _state.update {
                    it.copy(isImagePreviewOpen = actions.isImagePreviewOpen, brandImage = actions.url)
                }

            }
            BrandSettingActions.OnBrandImageClear -> {
                _state.update {
                    it.copy(brandImage = null)
                }

            }

            BrandSettingActions.OnNavigateBack -> TODO()
        }
    }

    fun validateFields(): Boolean {

        return !(_state.value.brandName.isBlank())
    }

    fun resetFields() {
        _state.update {
            it.copy(
                isEditMode = false,
                brandName = "",
                brandImage = null,
                isAvailable = true,
                brandId = null,
                createdDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                description = ""
            )
        }
    }
}