@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package org.techcult.scaleos.feature.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techcult.salesman.core.domain.onError
import com.techcult.salesman.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.techcult.scaleos.feature.supplier.domain.model.Supplier
import org.techcult.scaleos.feature.supplier.domain.repository.SupplierRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SupplierSettingViewModel(val repo: SupplierRepository) : ViewModel() {

    private val _state = MutableStateFlow(SupplierSettingState())
    val state = _state.asStateFlow()

    fun onAction(action: SupplierSettingActions) {
        when (action) {
            is SupplierSettingActions.OnSupplierNameChange -> _state.update { it.copy(supplierName = action.name) }
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
                    openingBalance = action.balance.toDoubleOrNull() ?: 0.0
                )
            }

            is SupplierSettingActions.OnAvailabilityChange -> _state.update { it.copy(isAvailable = action.isAvailable) }
            SupplierSettingActions.OnSaveClick -> saveSupplier()
            is SupplierSettingActions.OnAddDialogClick -> _state.update { it.copy(isAddDialogOpen = action.isAddDialogOpen) }
            is SupplierSettingActions.OnEditOptionClick -> openEditDialog(action.supplier)
            is SupplierSettingActions.OnSearchQueryChange -> _state.update { it.copy(searchQuery = action.query) }
            is SupplierSettingActions.OnStatusFilterChange -> _state.update { it.copy(statusFilter = action.filter) }
            is SupplierSettingActions.OnFilterButtonClick -> _state.update { it.copy(isStatusFilter = action.isStatusFiler) }
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
                    openingBalance = currentState.openingBalance,
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
                    openingBalance = currentState.openingBalance,
                    supplyingBrands = currentState.supplyingBrands,
                    isAvailable = currentState.isAvailable,
                    createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    createdBy = null, // TODO: Replace with actual user
                    updatedBy = null
                )
            }

             repo.upsertSupplier(supplier)
                 .onSuccess {

                 }
                 .onError {

                 }

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
                openingBalance = supplier.openingBalance,
                supplyingBrands = supplier.supplyingBrands,
                isAvailable = supplier.isAvailable,
                createdDate = supplier.createdAt
            )
        }
    }
}
