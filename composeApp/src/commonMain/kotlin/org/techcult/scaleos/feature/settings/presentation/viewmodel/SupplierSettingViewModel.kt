@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)

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
import org.techcult.scaleos.core.utils.codeCreator
import org.techcult.scaleos.feature.supplier.domain.model.Supplier
import org.techcult.scaleos.feature.supplier.domain.repository.SupplierRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SupplierSettingViewModel(val repo: SupplierRepository) : ViewModel() {


    private val filter = MutableStateFlow(AvailabilityFilter.ALL)
    private val searchQuery = MutableStateFlow<String?>(null)
    private val _state = MutableStateFlow(SupplierSettingState())
    val state = _state.asStateFlow()

    private val _event = Channel<SupplierSettingsEvents>()
    val event = _event.receiveAsFlow()

    init {
        getSupplierCount()

    }

    private fun getSupplierCount() {
        viewModelScope.launch {
            repo.getSupplierCount().collect {
                _state.update {
                    it.copy(supplierCount = it.supplierCount)
                }
            }
        }
    }

    val supplierList: StateFlow<List<Supplier>> = combine(searchQuery, filter) { f, q ->
        f to q
    }.onEach {
        _state.update {
            it.copy(isLoading = true)
        }
    }.flatMapLatest { (f, q) ->
        delay(1000)
        repo.observeFilteredSupplier(f, q)
    }.onEach {
        _state.update {
            it.copy(isLoading = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun onAction(action: SupplierSettingActions) {
        when (action) {
            is SupplierSettingActions.OnSupplierNameChange -> {
                if (!state.value.isEditMode) {
                    if (action.name.trim().length > 3) {
                        _state.update {
                            it.copy(
                                supplierName = action.name,
                                supplierCode = codeCreator(action.name, it.supplierCount)
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(supplierName = action.name, supplierCode = "")
                        }
                    }

                } else {
                    _state.update {
                        it.copy(supplierName = action.name)
                    }
                }
                _state.update {
                    it.copy(supplierName = action.name)
                }
            }
            is SupplierSettingActions.OnSupplierCodeChange -> _state.update { it.copy(supplierCode = action.code) }
            is SupplierSettingActions.OnSupplierTypeChange -> _state.update { it.copy(supplierType = action.type) }
            is SupplierSettingActions.OnSupplierNotesChange -> _state.update { it.copy(supplierNotes = action.notes) }
            is SupplierSettingActions.OnContactPersonChange -> _state.update { it.copy(contactPerson = action.person) }
            is SupplierSettingActions.OnContactNumberChange -> _state.update { it.copy(contactNumber = action.number) }
            is SupplierSettingActions.OnWssNumberChange -> _state.update { it.copy(wssNumber = action.wssNumber) }
            is SupplierSettingActions.OnUpiIdChange -> _state.update { it.copy(upiId = action.upiId) }
            is SupplierSettingActions.OnEmailChange -> _state.update { it.copy(email = action.email) }
            is SupplierSettingActions.OnGstNumberChange -> _state.update { it.copy(gstNumber = action.gstNumber) }
            is SupplierSettingActions.OnAddressChange -> _state.update { it.copy(address = action.address) }
            is SupplierSettingActions.OnCityChange -> _state.update { it.copy(city = action.city) }
            is SupplierSettingActions.OnStateChange -> _state.update { it.copy(state = action.state) }
            is SupplierSettingActions.OnPinCodeChange -> _state.update { it.copy(pinCode = action.pinCode) }
            is SupplierSettingActions.OnOpeningBalanceChange -> _state.update {
                it.copy(
                    openingBalance = action.balance
                )
            }

            is SupplierSettingActions.OnAvailabilityChange -> _state.update { it.copy(isAvailable = action.isAvailable) }
            SupplierSettingActions.OnSaveClick -> saveSupplier()
            is SupplierSettingActions.OnAddDialogClick -> _state.update { it.copy(isAddDialogOpen = action.isAddDialogOpen) }
            is SupplierSettingActions.OnEditOptionClick -> openEditDialog(action.supplier)
            is SupplierSettingActions.OnSearchQueryChange -> {
                searchQuery.value = action.query.ifBlank { null }
                _state.update { it.copy(searchQuery = action.query) }
            }

            is SupplierSettingActions.OnStatusFilterChange -> {
                filter.value = action.filter
                _state.update { it.copy(statusFilter = action.filter) }
            }
            is SupplierSettingActions.OnFilterButtonClick -> _state.update { it.copy(isStatusFilter = action.isStatusFiler) }
            SupplierSettingActions.OnDismissDialog -> _state.update { it.copy(isAddDialogOpen = false) }
            is SupplierSettingActions.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTab = action.tab)
                }

            }
            is SupplierSettingActions.OnPaymentTermsChange->
            {
                _state.update {
                    it.copy(selectedPaymentTerms = action.paymentTerms)
                }
            }
            else -> {}
        }
    }

    private fun saveSupplier() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState.supplierName.isBlank() || currentState.supplierCode.isBlank()) {

                return@launch
            }

            val supplier = if (currentState.isEditMode) {
                Supplier(
                    supplierId = currentState.supplierId!!,
                    supplierName = currentState.supplierName,
                    supplierCode = currentState.supplierCode,
                    supplierType = currentState.supplierType,
                    supplierNotes = currentState.supplierNotes,
                    contactPerson = currentState.contactPerson,
                    contactNumber = currentState.contactNumber,
                    wssNumber = currentState.wssNumber,
                    upiId = currentState.upiId,
                    email = currentState.email,
                    gstNumber = currentState.gstNumber,
                    address = currentState.address,
                    city = currentState.city,
                    state = currentState.state,
                    pinCode = currentState.pinCode,
                    openingBalance = currentState.openingBalance.toDouble(),
                    supplyingBrands = currentState.supplyingBrands,
                    isAvailable = currentState.isAvailable,
                    createdAt = currentState.createdDate,
                    updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    createdBy = null, // TODO: Replace with actual user
                    updatedBy = null, // TODO: Replace with actual user
                )
            } else {
                Supplier(
                    supplierId = Uuid.random().toString(),
                    supplierName = currentState.supplierName,
                    supplierCode = currentState.supplierCode,
                    supplierType = currentState.supplierType,
                    supplierNotes = currentState.supplierNotes,
                    contactPerson = currentState.contactPerson,
                    contactNumber = currentState.contactNumber,
                    wssNumber = currentState.wssNumber,
                    upiId = currentState.upiId,
                    email = currentState.email,
                    gstNumber = currentState.gstNumber,
                    address = currentState.address,
                    city = currentState.city,
                    state = currentState.state,
                    pinCode = currentState.pinCode,
                    openingBalance = currentState.openingBalance.toDouble(),
                    supplyingBrands = currentState.supplyingBrands,
                    isAvailable = currentState.isAvailable,
                    createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    createdBy = null, // TODO: Replace with actual user
                    updatedBy = null
                )
            }

             repo.upsertSupplier(supplier)
                 .onSuccess {
                     _event.send(SupplierSettingsEvents.OnSuccess("Supplier Saved"))

                 }
                 .onError {
                     _event.send(SupplierSettingsEvents.OnFailure(it.name))

                 }
            resetFields()

            _state.update { it.copy(isAddDialogOpen = false) }
        }
    }

    private fun openEditDialog(supplier: Supplier) {
        _state.update {
            it.copy(
                isAddDialogOpen = true,
                isEditMode = true,
                supplierId = supplier.supplierId,
                supplierName = supplier.supplierName,
                supplierCode = supplier.supplierCode,
                supplierType = supplier.supplierType,
                supplierNotes = supplier.supplierNotes,
                contactPerson = supplier.contactPerson,
                contactNumber = supplier.contactNumber,
                wssNumber = supplier.wssNumber,
                upiId = supplier.upiId,
                email = supplier.email,
                gstNumber = supplier.gstNumber,
                address = supplier.address,
                city = supplier.city,
                state = supplier.state,
                pinCode = supplier.pinCode,
                openingBalance ="",
                supplyingBrands = supplier.supplyingBrands,
                isAvailable = supplier.isAvailable,
                createdDate = supplier.createdAt
            )
        }
    }

    private fun resetFields() {
        _state.update {
            it.copy(
                isEditMode = false,
                supplierName = "",
                supplierCode = "",
                supplierType = "",
                supplierNotes = null,
                contactPerson = null,
                contactNumber = null,
                wssNumber = null,
                upiId = null,
                email = null,
                gstNumber = null,
                address = null,
                city = null,
                state = null,
                pinCode = null,
                openingBalance = "",
                supplyingBrands = null,
                isAvailable = true,
                supplierId = null,
                createdDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            )
        }
    }
}
