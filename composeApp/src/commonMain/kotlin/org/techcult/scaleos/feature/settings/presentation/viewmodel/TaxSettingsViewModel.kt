@file:OptIn(ExperimentalCoroutinesApi::class)

package org.techcult.scaleos.feature.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import org.techcult.scaleos.feature.tax.domain.model.TaxComponent
import org.techcult.scaleos.feature.tax.domain.model.TaxSlab
import org.techcult.scaleos.feature.tax.domain.repository.TaxRepository

class TaxSettingsViewModel(val repository: TaxRepository) : ViewModel() {


    private val filter = MutableStateFlow(AvailabilityFilter.ALL)
    private val searchQuery = MutableStateFlow<String?>(null)


    private val _state = MutableStateFlow(TaxSettingsState())
    val state: StateFlow<TaxSettingsState> = _state.asStateFlow()

    private val _event = Channel<TaxSettingEvents>()
    val event = _event.receiveAsFlow()

    val taxList: StateFlow<List<TaxSlab>> = combine(filter, searchQuery) { f, q ->
        f to q
    }.onEach {
        _state.update {
            it.copy(isLoading = true)
        }
    }.flatMapLatest { (f, q) ->
        delay(1000)
        repository.observeFilteredActiveTax(f, q)
    }.onEach {
        _state.update {
            it.copy(isLoading = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )


    fun onAction(actions: TaxSettingsActions) {
        when (actions) {
            is TaxSettingsActions.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = actions.query)
                }
                searchQuery.value = actions.query.ifBlank { null }
            }

            is TaxSettingsActions.OnAddClicked -> {
                _state.update {
                    it.copy(isAddDialogOpen = actions.isDialogOpen)
                }
            }

            TaxSettingsActions.OnCancelClicked -> {
                _state.update {
                    it.copy(isAddDialogOpen = false)
                }
            }

            TaxSettingsActions.OnSaveClicked -> {
                _state.update {
                    it.copy(isAddDialogOpen = false)
                }
                if (validate()) {

                }

            }

            is TaxSettingsActions.OnStatusFilterChange -> {
                filter.value = actions.filter
                _state.update {
                    it.copy(filter = actions.filter)
                }

            }

            is TaxSettingsActions.OnFilterButtonClick -> {
                _state.update {
                    it.copy(isStatusFilter = actions.isStatusFiler)
                }

            }

            TaxSettingsActions.OnTaxComponentAddClick -> {

                val newList = _state.value.taxComponentsList

                if (_state.value.taxComponentName.isNotBlank() && _state.value.taxComponentRate != 0.0) {
                    newList.add(
                        TaxComponent(
                            name = _state.value.taxComponentName,
                            rate = _state.value.taxComponentRate,
                            taxId = 0,
                            isActive = true,
                            id = 0
                        )
                    )

                }
                _state.update {
                    it.copy(taxComponentsList = newList, taxComponentName = "", taxComponentRate = 0.0)
                }

            }

            is TaxSettingsActions.OnTaxComponentRemoveClick -> {
                val newList = _state.value.taxComponentsList.toMutableList()
                newList.removeAt(actions.index)
                _state.update {
                    it.copy(taxComponentsList = newList)
                }


            }

            is TaxSettingsActions.OnTaxComponentAddNameChange -> {

                _state.update {
                    it.copy(taxComponentName = actions.name)
                }
            }

            is TaxSettingsActions.OnTaxComponentRateChange -> {

                _state.update {
                    it.copy(taxComponentRate = actions.rate.toDouble())

                }

            }

            is TaxSettingsActions.OnTaxNameChange -> {
                _state.update {
                    it.copy(taxName = actions.name, taxNameError = null)
                }

            }

            is TaxSettingsActions.OnTaxCodeChange -> {
                _state.update {
                    it.copy(taxCode = actions.code, taxCodeError = null)
                }
            }

            is TaxSettingsActions.OnDescriptionChange -> {
                _state.update {
                    it.copy(description = actions.description, descriptionError = null)
                }

            }

            is TaxSettingsActions.OnTaxRateChange -> {
                _state.update {
                    it.copy(taxRate = actions.rate, taxRateError = null)
                }

            }
            is TaxSettingsActions.OnTaxComponentChange -> {
                _state.update {
                    it.copy(taxComponentName = actions.component.name, taxComponentRate = actions.component.rate)
                }
            }





            else -> {}
        }
    }

    private fun validate(): Boolean {
        return false
    }


}