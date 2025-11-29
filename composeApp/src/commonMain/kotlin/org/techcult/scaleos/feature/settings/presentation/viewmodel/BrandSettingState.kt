@file:OptIn(ExperimentalTime::class)

package org.techcult.scaleos.feature.settings.presentation.viewmodel

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.techcult.scaleos.feature.product.domain.model.Brand
import org.techcult.scaleos.feature.product.domain.model.Uom
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class BrandSettingState(
    val statusFilter: AvailabilityFilter= AvailabilityFilter.ALL,
    val isBottomSheetOpen: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isAddDialogOpen: Boolean = false,
    val isAddImageDialogOpen: Boolean = false,
    val isEditMode: Boolean = false,
    val isImagePreviewOpen: Boolean = false,
    val brandId: String? = null,
    val brandName: String = "",
    val brandImage: String? = null,
    val description: String = "",
    val isAvailable: Boolean = true,
    val isStatusFilter: Boolean = false,
    val createdDate: LocalDateTime= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
)
sealed interface BrandSettingActions {

    data class OnBrandNameChange(val name: String) : BrandSettingActions
    data class OnBrandImageChange(val url: String) : BrandSettingActions
    data class OnAvailabilityChange(val isAvailable: Boolean) : BrandSettingActions
    data class OnBrandImageAddClick(val isAddImage: Boolean) : BrandSettingActions
    data object OnBrandImageClear: BrandSettingActions
    data object OnSaveClick : BrandSettingActions
    data object OnDismissDialog : BrandSettingActions
    data class OnAddDialogClick(val isAddDialogOpen: Boolean) : BrandSettingActions
    data class OnEditOptionClick(val brand: Brand) : BrandSettingActions
    data class OnSearchQueryChange(val query: String) : BrandSettingActions
    data class OnStatusFilterChange(val filter: AvailabilityFilter) : BrandSettingActions
    data class OnFilterButtonClick(val isStatusFiler: Boolean) : BrandSettingActions
    data class OnDescriptionChange(val description: String): BrandSettingActions
    data object OnNavigateBack: BrandSettingActions

    data class OnImagePreviewClick(val url: String,val isImagePreviewOpen: Boolean) : BrandSettingActions


}

sealed interface BrandSettingsEvents {

    data class OnSuccess(val message: String) : UnitSettingsEvents
    data class OnFailure(val message: String) : UnitSettingsEvents
}