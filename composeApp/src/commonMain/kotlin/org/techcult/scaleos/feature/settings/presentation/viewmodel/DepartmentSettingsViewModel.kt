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
import org.techcult.scaleos.feature.product.domain.model.Department
import org.techcult.scaleos.feature.product.domain.repository.DepartmentRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DepartmentSettingsViewModel(val repo: DepartmentRepository) : ViewModel() {
    private val filter = MutableStateFlow(AvailabilityFilter.ALL)
    private val searchQuery = MutableStateFlow<String?>(null)
    private val _state = MutableStateFlow(DeptSettingState())
    val state = _state.asStateFlow()

    private val _event = Channel<DeptSettingsEvents>()
    val event = _event.receiveAsFlow()

    val deptList: StateFlow<List<Department>> = combine(filter, searchQuery) { f, q ->
        f to q
    }.onEach {
        _state.update {
            it.copy(isLoading = true)
        }
    }.flatMapLatest { (f, q) ->
        delay(1000)
        repo.observeFilteredDepartment(f, q)
    }.onEach {
        _state.update {
            it.copy(isLoading = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )


    fun onAction(actions: DeptSettingActions) {
        when (actions) {
            is DeptSettingActions.OnAddDialogClick -> {

                _state.update {
                    it.copy(isAddDialogOpen = actions.isAddDialogOpen)
                }
            }

            is DeptSettingActions.OnDescriptionChange -> {
                _state.update {
                    it.copy(description = actions.description)
                }
            }

            DeptSettingActions.OnSaveClick -> {
                var department: Department?
                viewModelScope.launch {

                    if (validateFields()) {
                        if (state.value.isEditMode) {
                            department = Department(
                                id = state.value.deptId.toString(),
                                departmentName = state.value.deptName,
                                description = state.value.description,
                                isAvailable = state.value.isAvailable,
                                createdAt = state.value.createdDate,
                                updatedAt = Clock.System.now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                            )

                        } else {
                            department = Department(
                                id = Uuid.random().toString(),
                                departmentName = state.value.deptName,

                                description = state.value.description,
                                createdAt = Clock.System.now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                                updatedAt = null
                            )

                        }
                        repo.upsertDepartment(department).onSuccess {
                            _state.update {
                                it.copy(isAddDialogOpen = false)
                            }
                            _event.send(DeptSettingsEvents.OnSuccess(if (state.value.isEditMode) "Unit Updated" else "Unit Added"))
                            resetFields()

                        }
                            .onError { error ->
                                _state.update {
                                    it.copy(isAddDialogOpen = false)
                                }
                                _event.send(DeptSettingsEvents.OnFailure(error.name))
                                resetFields()

                            }


                    }


                }

            }

            is DeptSettingActions.OnAvailabilityChange -> {
                _state.update {
                    it.copy(isAvailable = actions.isAvailable)
                }
            }

            DeptSettingActions.OnDismissDialog -> {
                _state.update {
                    it.copy(isAddDialogOpen = false)
                }
            }

            is DeptSettingActions.OnEditOptionClick -> {
                _state.update {
                    it.copy(
                        isEditMode = true,
                        isAddDialogOpen = true,
                        deptName = actions.uom.departmentName,
                        isAvailable = actions.uom.isAvailable,
                        deptId = actions.uom.id,
                        createdDate = actions.uom.createdAt,
                        description = actions.uom.description.toString()
                    )
                }
            }

            is DeptSettingActions.OnFilterButtonClick -> {
                _state.update {
                    it.copy(isStatusFilter = actions.isStatusFiler)
                }
            }

            is DeptSettingActions.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = actions.query)
                }
                searchQuery.value = actions.query.ifBlank { null }

            }

            is DeptSettingActions.OnStatusFilterChange -> {
                filter.value = actions.filter
                _state.update {
                    it.copy(statusFilter = actions.filter)
                }
            }

            is DeptSettingActions.OnDeptNameChange -> {
                _state.update {
                    it.copy(deptName = actions.name)
                }
            }


            else -> Unit
        }
    }

    fun validateFields(): Boolean {

        return !(_state.value.deptName.isBlank() || _state.value.description.isBlank())
    }

    fun resetFields() {
        _state.update {
            it.copy(
                isEditMode = false,
                deptName = "",
                isAvailable = true,
                deptId = null,
                createdDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                description = ""
            )
        }
    }
}