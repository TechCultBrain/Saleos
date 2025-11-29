@file:OptIn(ExperimentalTime::class)

package org.techcult.scaleos.feature.settings.presentation.viewmodel

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.techcult.scaleos.feature.product.domain.model.Department
import org.techcult.scaleos.feature.product.domain.model.Uom
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class DeptSettingState(
    val statusFilter: AvailabilityFilter= AvailabilityFilter.ALL,
    val isBottomSheetOpen: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isAddDialogOpen: Boolean = false,
    val isEditMode: Boolean = false,
    val deptId: String? = null,
    val deptName: String = "",
    val deptSymbol: String = "",
    val description: String = "",
    val isAvailable: Boolean = true,
    val isStatusFilter: Boolean = false,
    val createdDate: LocalDateTime= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
)


sealed interface DeptSettingActions {

    data class OnDeptNameChange(val name: String) : DeptSettingActions
    data class OnDeptSymbolChange(val symbol: String) : DeptSettingActions
    data class OnAvailabilityChange(val isAvailable: Boolean) : DeptSettingActions
    data object OnSaveClick : DeptSettingActions
    data object OnDismissDialog : DeptSettingActions
    data class OnAddDialogClick(val isAddDialogOpen: Boolean) : DeptSettingActions
    data class OnEditOptionClick(val uom: Department) : DeptSettingActions
    data class OnSearchQueryChange(val query: String) : DeptSettingActions
    data class OnStatusFilterChange(val filter: AvailabilityFilter) : DeptSettingActions
    data class OnFilterButtonClick(val isStatusFiler: Boolean) : DeptSettingActions

    data class OnDescriptionChange(val description: String): DeptSettingActions

    data object OnNavigateBack: DeptSettingActions

}

sealed interface DeptSettingsEvents {

    data class OnSuccess(val message: String) : DeptSettingsEvents
    data class OnFailure(val message: String) : DeptSettingsEvents
}