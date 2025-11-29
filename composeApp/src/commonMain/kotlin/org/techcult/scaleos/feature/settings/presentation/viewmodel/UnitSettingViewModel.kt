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
import org.techcult.scaleos.feature.product.domain.model.Uom
import org.techcult.scaleos.feature.product.domain.repository.UnitRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UnitSettingViewModel(val repo: UnitRepository) : ViewModel() {


    private val filter = MutableStateFlow(AvailabilityFilter.ALL)
    private val searchQuery = MutableStateFlow<String?>(null)
    private val _state = MutableStateFlow(UnitSettingState())
    val state = _state.asStateFlow()

    private val _event = Channel<UnitSettingsEvents>()
    val event = _event.receiveAsFlow()

    val uomList: StateFlow<List<Uom>> = combine(filter, searchQuery) { f, q ->
        f to q
    }.onEach {
        _state.update {
            it.copy(isLoading = true)
        }
    }.flatMapLatest { (f, q) ->
        delay(1000)
        repo.observeUnitsFiltered(f, q)
    }.onEach {
        _state.update {
            it.copy(isLoading = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )


    fun onAction(actions: UnitSettingActions) {
        when (actions) {
            is UnitSettingActions.OnAddDialogClick -> {

                _state.update {
                    it.copy(isAddDialogOpen = actions.isAddDialogOpen)
                }
            }

            is UnitSettingActions.OnDescriptionChange -> {
                _state.update {
                    it.copy(description = actions.description)
                }
            }

            UnitSettingActions.OnSaveClick -> {
                var uom: Uom?
                viewModelScope.launch {

                    if (validateFields()) {
                        if (state.value.isEditMode) {
                            uom = Uom(
                                id = state.value.unitId!!,
                                name = state.value.unitName,
                                symbol = state.value.unitSymbol,
                                description = state.value.description,
                                isAvailable = state.value.isAvailable,
                                createdAt = state.value.createdDate,
                                updatedAt = Clock.System.now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                            )

                        } else {
                            uom = Uom(
                                id = Uuid.random().toString(),
                                name = state.value.unitName,
                                symbol = state.value.unitSymbol,
                                description = state.value.description,
                                createdAt = Clock.System.now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                                updatedAt = null
                            )

                        }
                        repo.upsertUnit(uom).onSuccess {
                            _state.update {
                                it.copy(isAddDialogOpen = false)
                            }
                            _event.send(UnitSettingsEvents.OnSuccess(if (state.value.isEditMode) "Unit Updated" else "Unit Added"))
                            resetFields()

                        }
                            .onError { error ->
                                _state.update {
                                    it.copy(isAddDialogOpen = false)
                                }
                                _event.send(UnitSettingsEvents.OnFailure(error.name))
                                resetFields()

                            }


                    }


                }

            }

            is UnitSettingActions.OnAvailabilityChange -> {
                _state.update {
                    it.copy(isAvailable = actions.isAvailable)
                }
            }

            UnitSettingActions.OnDismissDialog -> {
                _state.update {
                    it.copy(isAddDialogOpen = false)
                }
            }

            is UnitSettingActions.OnEditOptionClick -> {
                _state.update {
                    it.copy(
                        isEditMode = true,
                        isAddDialogOpen = true,
                        unitName = actions.uom.name,
                        unitSymbol = actions.uom.symbol,
                        isAvailable = actions.uom.isAvailable,
                        unitId = actions.uom.id,
                        createdDate = actions.uom.createdAt,
                        description = actions.uom.description.toString()
                    )
                }
            }

            is UnitSettingActions.OnFilterButtonClick -> {
                _state.update {
                    it.copy(isStatusFilter = actions.isStatusFiler)
                }
            }

            is UnitSettingActions.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = actions.query)
                }
                searchQuery.value = actions.query.ifBlank { null }

            }

            is UnitSettingActions.OnStatusFilterChange -> {
                filter.value = actions.filter
                _state.update {
                    it.copy(statusFilter = actions.filter)
                }
            }

            is UnitSettingActions.OnUnitNameChange -> {
                _state.update {
                    it.copy(unitName = actions.name)
                }
            }

            is UnitSettingActions.OnUnitSymbolChange -> {
                _state.update {
                    it.copy(unitSymbol = actions.symbol)
                }
            }
            else -> Unit
        }
    }

    fun validateFields(): Boolean {

        return !(_state.value.unitName.isBlank() || _state.value.unitSymbol.isBlank())
    }

     fun resetFields() {
        _state.update {
            it.copy(
                isEditMode = false,
                unitName = "",
                unitSymbol = "",
                isAvailable = true,
                unitId = null,
                createdDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                description = ""
            )
        }
    }
}



