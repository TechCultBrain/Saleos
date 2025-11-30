@file:OptIn(ExperimentalTime::class)

package org.techcult.scaleos.feature.settings.presentation.viewmodel

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.techcult.scaleos.feature.product.domain.model.Brand
import org.techcult.scaleos.feature.product.domain.model.Uom
import org.techcult.scaleos.feature.supplier.domain.model.Supplier
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class SupplierSettingState(
    val statusFilter: AvailabilityFilter = AvailabilityFilter.ALL,
    val isBottomSheetOpen: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isAddDialogOpen: Boolean = false,
    val isEditMode: Boolean = false,
    val supplierId: String? = null,
    val supplierName: String = "",
    val supplierCode: String = "",
    val supplierType: String = "",
    val supplierNotes: String? = null,
    val contactPerson: String? = null,
    val contactNumber: String? = null,
    val wssNumber: String? = null,
    val upiId: String? = null,
    val email: String? = null,
    val gstNumber: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val pinCode: String? = null,
    val openingBalance: Double = 0.0,
    val supplyingBrands: List<String>? = null,
    val isAvailable: Boolean = true,
    val isStatusFilter: Boolean = false,
    val createdDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
)

sealed interface SupplierSettingActions {
    data class OnSupplierNameChange(val name: String) : SupplierSettingActions
    data class OnSupplierCodeChange(val code: String) : SupplierSettingActions
    data class OnSupplierTypeChange(val type: String) : SupplierSettingActions
    data class OnSupplierNotesChange(val notes: String) : SupplierSettingActions
    data class OnContactPersonChange(val person: String) : SupplierSettingActions
    data class OnContactNumberChange(val number: String) : SupplierSettingActions
    data class OnWssNumberChange(val wssNumber: String) : SupplierSettingActions
    data class OnUpiIdChange(val upiId: String) : SupplierSettingActions
    data class OnEmailChange(val email: String) : SupplierSettingActions
    data class OnGstNumberChange(val gstNumber: String) : SupplierSettingActions
    data class OnAddressChange(val address: String) : SupplierSettingActions
    data class OnCityChange(val city: String) : SupplierSettingActions
    data class OnStateChange(val state: String) : SupplierSettingActions
    data class OnPinCodeChange(val pinCode: String) : SupplierSettingActions
    data class OnOpeningBalanceChange(val balance: String) : SupplierSettingActions
    data class OnAvailabilityChange(val isAvailable: Boolean) : SupplierSettingActions
    data object OnSaveClick : SupplierSettingActions
    data object OnDismissDialog : SupplierSettingActions
    data class OnAddDialogClick(val isAddDialogOpen: Boolean) : SupplierSettingActions
    data class OnEditOptionClick(val supplier: Supplier) : SupplierSettingActions
    data class OnSearchQueryChange(val query: String) : SupplierSettingActions
    data class OnStatusFilterChange(val filter: AvailabilityFilter) : SupplierSettingActions
    data class OnFilterButtonClick(val isStatusFiler: Boolean) : SupplierSettingActions
    data object OnNavigateBack : SupplierSettingActions
}

sealed interface SupplierSettingsEvents {

    data class OnSuccess(val message: String) : SupplierSettingsEvents
    data class OnFailure(val message: String) : SupplierSettingsEvents
}
