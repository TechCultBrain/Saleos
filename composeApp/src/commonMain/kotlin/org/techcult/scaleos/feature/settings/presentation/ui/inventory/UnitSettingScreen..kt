@file:OptIn(ExperimentalMaterial3Api::class)

package org.techcult.scaleos.feature.settings.presentation.ui.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.techcult.scaleos.core.presentation.components.MyFilterChip
import org.techcult.scaleos.core.presentation.components.MyTextField
import org.techcult.scaleos.core.presentation.components.SearchBar
import org.techcult.scaleos.core.utils.DeviceConfiguration
import org.techcult.scaleos.core.utils.ObserveAsEvents
import org.techcult.scaleos.core.utils.toFormattedString
import org.techcult.scaleos.feature.product.domain.model.Uom
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.CompactPageHeader
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.WidePageHeader
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import org.techcult.scaleos.feature.settings.presentation.viewmodel.UnitSettingActions
import org.techcult.scaleos.feature.settings.presentation.viewmodel.UnitSettingState
import org.techcult.scaleos.feature.settings.presentation.viewmodel.UnitSettingViewModel
import org.techcult.scaleos.feature.settings.presentation.viewmodel.UnitSettingsEvents

@Composable
fun UnitSettingScreen(
    onBack: () -> Unit,
    viewModel: UnitSettingViewModel = koinViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uomList by viewModel.uomList.collectAsStateWithLifecycle()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.event) {
        when (it) {
            is UnitSettingsEvents.OnFailure -> {
                scope.launch {
                    snackBarHostState.showSnackbar(it.message)
                }
            }

            is UnitSettingsEvents.OnSuccess -> {
                scope.launch {
                    snackBarHostState.showSnackbar(it.message)
                }


            }
            else -> {}
        }
    }

    if (state.isAddDialogOpen) {
        AddUnitDialog(state = state, action = {
            when (it) {
                UnitSettingActions.OnNavigateBack ->
                    onBack()

                else -> viewModel.onAction(it)


            }
        })
    }
    if (state.isStatusFilter) {
        ModalBottomSheet(onDismissRequest = {
            viewModel.onAction(UnitSettingActions.OnFilterButtonClick(isStatusFiler = false))

        }, sheetState = sheetState)
        {
            AvailabilityFilter.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        viewModel.onAction(UnitSettingActions.OnStatusFilterChange(option))
                        viewModel.onAction(UnitSettingActions.OnFilterButtonClick(isStatusFiler = false))

                    })
            }


        }
    }

    Scaffold(snackbarHost = {
        androidx.compose.material3.SnackbarHost(hostState = snackBarHostState, modifier = Modifier.padding(bottom = 64.dp))
    }) {
        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT -> CompactScreenUi(
                state,
                viewModel::onAction,
                uomList
            )

            DeviceConfiguration.MOBILE_LANDSCAPE -> TODO()
            DeviceConfiguration.TABLET_PORTRAIT -> TODO()
            DeviceConfiguration.TABLET_LANDSCAPE -> TODO()
            DeviceConfiguration.DESKTOP -> WideScreenUi(state, viewModel::onAction, uomList)
        }


    }


}

@Composable
fun AddUnitDialog(state: UnitSettingState, action: (UnitSettingActions) -> Unit) {
    Dialog(onDismissRequest = { action(UnitSettingActions.OnDismissDialog) }) {
        Surface(modifier = Modifier, color = Color.White, shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                DialogHeader(
                    isEditMode = false,
                    onDismissRequest = { action(UnitSettingActions.OnDismissDialog) },
                    title = "Unit"
                )
                Spacer(modifier = Modifier.height(16.dp))

                //BasicInfoFields(state, action)
                MyTextField(
                    value = state.unitName,
                    placeholder = "Enter unit name",
                    label = "Unit Name *",
                    onValueChange = {
                        action(UnitSettingActions.OnUnitNameChange(it))

                    })
                Spacer(modifier = Modifier.height(16.dp))
                MyTextField(value = state.unitSymbol, onValueChange = {
                    action(UnitSettingActions.OnUnitSymbolChange(it))
                }, placeholder = "Enter unit symbol", label = "Unit Symbol *")
                Spacer(modifier = Modifier.height(16.dp))
                MyTextField(value = state.description, onValueChange = {
                    action(UnitSettingActions.OnDescriptionChange(it))

                }, placeholder = "Enter unit description", label = "Unit Description")
                Spacer(modifier = Modifier.height(16.dp))
                if (state.isEditMode) {
                    AvailabilityField(
                        state.isAvailable,
                        onChange = { action(UnitSettingActions.OnAvailabilityChange(it)) })
                }
                Spacer(modifier = Modifier.height(16.dp))
                DialogButtons(
                    onDismiss = { action(UnitSettingActions.OnDismissDialog) },
                    onCreate = { action(UnitSettingActions.OnSaveClick) },
                    isEditMode = state.isEditMode,
                    title = "Unit"
                )


            }
        }
    }
}

@Composable
fun CompactScreenUi(
    state: UnitSettingState,
    action: (UnitSettingActions) -> Unit,
    list: List<Uom>
) {

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        CompactPageHeader(
            title = "Unit Management",
            subtitle = "Organize your products into categories for better management",
            onExport = {},
            onImport = {},
            onAdd = {
                action(UnitSettingActions.OnAddDialogClick(isAddDialogOpen = true))

            }, onBack = {
                action(UnitSettingActions.OnNavigateBack)

            })
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchBar(
                modifier = Modifier.weight(1f),
                searchText = state.searchQuery,
                onSearchTextChange = {

                    action(UnitSettingActions.OnSearchQueryChange(it))

                })
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                action(UnitSettingActions.OnFilterButtonClick(isStatusFiler = true))
            })
            {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Edit")

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CompactUnitTable(action, state, list)

    }

}

@Composable
fun CompactUnitTable(
    action: (UnitSettingActions) -> Unit,
    state: UnitSettingState,
    list: List<Uom>
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()

        }
    } else {
        LazyColumn(modifier = Modifier.background(Color.White).clip(RoundedCornerShape(8.dp))) {
            if (list.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No Units Found")

                    }
                }
            }
                itemsIndexed(list) { index, uom ->
                    CompactUomListItem(uom = uom, onEdit = {
                        action(UnitSettingActions.OnEditOptionClick(it))

                    })
                    if (index < list.lastIndex) {
                        HorizontalDivider(thickness = 0.5.dp)
                    }


                }
            }
        }

}

@Composable
fun CompactUomListItem(uom: Uom, onEdit: (Uom) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(uom.name.capitalize(Locale.current))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                uom.symbol.uppercase(),
                style = MaterialTheme.typography.bodySmall
            )


        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StatusChip(uom.isAvailable)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Edit",
                modifier = Modifier.clickable {
                    onEdit(uom)
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }

    }
}


@Composable
fun WideScreenUi(
    state: UnitSettingState,
    onAction: (UnitSettingActions) -> Unit,
    uomList: List<Uom>
) {
    Column(modifier = Modifier.fillMaxSize().padding(all = 16.dp)) {
        WidePageHeader(
            title = "Unit Management",
            subtitle = "Organize your products into categories for better management",
            onExport = { /*TODO*/ },
            onImport = { /*TODO*/ },
            onAdd = {
                onAction(UnitSettingActions.OnAddDialogClick(isAddDialogOpen = true))

            },
            addText = "Add Unit",
            icon = {
                Icon(imageVector = Icons.Outlined.Scale, contentDescription = "Unit")
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchBar(searchText = state.searchQuery, onSearchTextChange = {
                onAction(UnitSettingActions.OnSearchQueryChange(it))
            }, modifier = Modifier.weight(5f))
            Spacer(modifier = Modifier.width(16.dp))
            MyFilterChip(
                modifier = Modifier.weight(1f),
                options = AvailabilityFilter.entries,
                selectedOption = state.statusFilter.name,
                onOptionSelected = {
                    onAction(
                        UnitSettingActions.OnStatusFilterChange(it)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        UnitTable(state = state, action = onAction, uomList)


    }


}

@Composable
fun UnitTable(state: UnitSettingState, action: (UnitSettingActions) -> Unit, x2: List<Uom>) {

    Column(
        modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium)
            .background(color = androidx.compose.ui.graphics.Color.White)
    ) {
        ListHeader()
        HorizontalDivider(thickness = 0.5.dp)
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
            {
                CircularProgressIndicator()
            }
        } else {
            UomList(uomList = x2, onEdit = {
                action(UnitSettingActions.OnEditOptionClick(it))
            })
        }

    }
}

@Composable
fun ListHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            "Uom Name",
            modifier = Modifier.weight(3f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Uom Symbol",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold

        )
        Text(
            "Status",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "CreatedAt",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold

        )
        Text(
            "Actions",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold

        )
    }
}

@Composable
fun UomList(uomList: List<Uom>, onEdit: (Uom) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        if (uomList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Units Found")
                }
            }
        }

        itemsIndexed(uomList) { index, uom ->
            UomListItem(uom = uom, onEdit)
            if (index < uomList.lastIndex) {
                HorizontalDivider(thickness = 0.5.dp)
            }

        }

    }
}

@Composable
fun UomListItem(uom: Uom, onEdit: (Uom) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(3f)) {
            Text(uom.name.capitalize(Locale.current), modifier = Modifier)
            uom.description?.let {
                Text(
                    it.capitalize(Locale.current),
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Row(modifier = Modifier.weight(1f)) {

            Text(
                uom.symbol,
                modifier = Modifier.clip(shape = MaterialTheme.shapes.small)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer).padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(modifier = Modifier.weight(1f)) {
            StatusChip(uom.isAvailable)
        }
        Text(
            uom.createdAt.toFormattedString().toString(),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.weight(1f)) {
            IconButton(onClick = {
                onEdit(uom)
            }) {
                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
            }
        }
    }
}