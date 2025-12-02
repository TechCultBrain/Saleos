package org.techcult.scaleos.feature.settings.presentation.viewmodel

import org.techcult.scaleos.feature.tax.domain.model.TaxComponent
import org.techcult.scaleos.feature.tax.domain.model.TaxSlab

data class TaxSettingsState(
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val isAddDialogOpen: Boolean = false,
    val isStatusFilter: Boolean = false,
    val searchQuery: String = "",
    val filter: AvailabilityFilter = AvailabilityFilter.ALL,
    val taxName: String = "",
    val taxCode: String = "",
    val description: String = "",
    val taxRate: String = "",
    val isAvailable: Boolean = true,
    val taxId: Long? = null,
    val taxComponentsList: MutableList<TaxComponent> = mutableListOf(),
    val taxNameError: String? = null,
    val taxCodeError: String? = null,
    val descriptionError: String? = null,
    val taxRateError: String? = null,
    val taxComponentName: String = "",
    val taxComponentRate: Double = 0.0,
    )


sealed interface TaxSettingsActions {
    object OnSaveClicked : TaxSettingsActions
    object OnCancelClicked : TaxSettingsActions
    object OnNavigateBack : TaxSettingsActions
    data class OnAddClicked(val isDialogOpen: Boolean) : TaxSettingsActions
    data class OnSearchQueryChange(val query: String) : TaxSettingsActions
    data class OnFilterButtonClick(val isStatusFiler: Boolean) : TaxSettingsActions
    data class OnEditOptionClick(val tax: TaxSlab) : TaxSettingsActions
    data class OnStatusFilterChange(val filter: AvailabilityFilter) : TaxSettingsActions
    data class OnTaxNameChange(val name: String) : TaxSettingsActions
    data class OnTaxCodeChange(val code: String) : TaxSettingsActions
    data class OnDescriptionChange(val description: String) : TaxSettingsActions
    data class OnTaxRateChange(val rate: String) : TaxSettingsActions
    data class OnTaxComponentChange(val component: TaxComponent) : TaxSettingsActions
    data object OnTaxComponentAddClick : TaxSettingsActions
    data class OnTaxComponentRemoveClick(val index: Int) : TaxSettingsActions
    data class OnTaxComponentAddNameChange(val name: String) : TaxSettingsActions
    data class OnTaxComponentRateChange(val rate: String) : TaxSettingsActions




}


sealed interface TaxSettingEvents {

    data class OnSuccess(val message: String) : TaxSettingEvents
    data class OnError(val message: String) : TaxSettingEvents


}

data class TaxComponentItem(val name: String, val value: String)