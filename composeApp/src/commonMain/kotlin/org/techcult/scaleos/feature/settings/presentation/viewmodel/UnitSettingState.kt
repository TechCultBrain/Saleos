package org.techcult.scaleos.feature.settings.presentation.viewmodel

import kotlinx.datetime.LocalDateTime
import org.techcult.scaleos.feature.product.domain.model.Uom

data class UnitSettingState(
    val statusFilter: AvailabilityFilter= AvailabilityFilter.ALL,
    val isBottomSheetOpen: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isAddDialogOpen: Boolean = false,
    val isEditMode: Boolean = false,
    val unitId: String? = null,
    val unitName: String = "",
    val unitSymbol: String = "",
    val description: String = "",
    val isAvailable: Boolean = true,
    val isStatusFilter: Boolean = false,
    val createdDate: LocalDateTime= LocalDateTime(1970, 1, 1, 0, 0),
)


sealed interface UnitSettingActions {

    data class OnUnitNameChange(val name: String) : UnitSettingActions
    data class OnUnitSymbolChange(val symbol: String) : UnitSettingActions
    data class OnAvailabilityChange(val isAvailable: Boolean) : UnitSettingActions
    data object OnSaveClick : UnitSettingActions
    data object OnDismissDialog : UnitSettingActions
    data class OnAddDialogClick(val isAddDialogOpen: Boolean) : UnitSettingActions
    data class OnEditOptionClick(val uom: Uom) : UnitSettingActions
    data class OnSearchQueryChange(val query: String) : UnitSettingActions
    data class OnStatusFilterChange(val filter: AvailabilityFilter) : UnitSettingActions
    data class OnFilterButtonClick(val isStatusFiler: Boolean) : UnitSettingActions

    data class OnDescriptionChange(val description: String): UnitSettingActions

    data object OnNavigateBack: UnitSettingActions

}

sealed interface UnitSettingsEvents {

    data class OnSuccess(val message: String) : UnitSettingsEvents
    data class OnFailure(val message: String) : UnitSettingsEvents
}