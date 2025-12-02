@file:OptIn(ExperimentalMaterial3Api::class)

package org.techcult.scaleos.feature.settings.presentation.ui.pricing

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.input.KeyboardType
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
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.CompactPageHeader
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.WidePageHeader
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.DialogButtons
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.DialogHeader
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import org.techcult.scaleos.feature.settings.presentation.viewmodel.TaxSettingEvents
import org.techcult.scaleos.feature.settings.presentation.viewmodel.TaxSettingsActions
import org.techcult.scaleos.feature.settings.presentation.viewmodel.TaxSettingsState
import org.techcult.scaleos.feature.settings.presentation.viewmodel.TaxSettingsViewModel
import org.techcult.scaleos.feature.tax.domain.model.TaxComponent
import org.techcult.scaleos.feature.tax.domain.model.TaxSlab

@Composable
fun TaxSettingScreen(onBack: () -> Unit, viewModel: TaxSettingsViewModel = koinViewModel()) {


    val sheetState = rememberModalBottomSheetState()
    val scop = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val state by viewModel.state.collectAsStateWithLifecycle()
    val taxList by viewModel.taxList.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) {
        when (it) {
            is TaxSettingEvents.OnError -> {
                scop.launch {
                    snackBarHostState.showSnackbar(it.message)
                }
            }

            is TaxSettingEvents.OnSuccess -> {
                scop.launch {
                    snackBarHostState.showSnackbar(it.message)
                }
            }
        }
    }
    if (state.isAddDialogOpen) {

        TaxAddDialog(state = state, action = viewModel::onAction)

    }
    if (state.isStatusFilter) {
        ModalBottomSheet(onDismissRequest = {
            viewModel.onAction(TaxSettingsActions.OnFilterButtonClick(isStatusFiler = false))

        }, sheetState = sheetState) {
            AvailabilityFilter.entries.forEach { option ->
                DropdownMenuItem(text = { Text(option.name) }, onClick = {
                    viewModel.onAction(TaxSettingsActions.OnStatusFilterChange(option))
                    viewModel.onAction(TaxSettingsActions.OnFilterButtonClick(isStatusFiler = false))

                })
            }


        }
    }


    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            CompactPageTaxUiScreen(state, viewModel::onAction, taxList)
        }

        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            CompactPageTaxUiScreen(state, viewModel::onAction, taxList)
        }

        DeviceConfiguration.TABLET_PORTRAIT -> {
            CompactPageTaxUiScreen(state, viewModel::onAction, taxList)

        }

        DeviceConfiguration.TABLET_LANDSCAPE -> {
            WidePageTaxUiScreen(state, viewModel::onAction, taxList)
        }

        DeviceConfiguration.DESKTOP -> {
            WidePageTaxUiScreen(state, action = {
                when (it) {
                    is TaxSettingsActions.OnNavigateBack -> onBack()
                    else -> viewModel.onAction(it)
                }

            }, taxList)
        }
    }


}

@Composable
fun TaxAddDialog(state: TaxSettingsState, action: (TaxSettingsActions) -> Unit) {
    Dialog(onDismissRequest = { action(TaxSettingsActions.OnCancelClicked) }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DialogHeader(
                    isEditMode = state.isEditMode,
                    onDismissRequest = { action(TaxSettingsActions.OnCancelClicked) },
                    title = "Tax"
                )
                TaxForm(state, action)
                Spacer(modifier = Modifier.height(8.dp))
                DialogButtons(
                    onDismiss = { action(TaxSettingsActions.OnCancelClicked) },
                    onCreate = { action(TaxSettingsActions.OnSaveClicked) },
                    isEditMode = state.isEditMode,
                    title = "Tax"
                )

            }
        }
    }
}

@Composable
fun TaxForm(state: TaxSettingsState, action: (TaxSettingsActions) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        MyTextField(
            modifier = Modifier.fillMaxWidth().weight(1f),
            value = state.taxName,
            placeholder = "Enter tax name",
            label = "Tax Name *",
            isError = state.taxNameError != null,
            supportingText = state.taxNameError,
            onValueChange = {
                action(TaxSettingsActions.OnTaxNameChange(it))
            })
        Spacer(modifier = Modifier.width(16.dp))
        MyTextField(
            modifier = Modifier.fillMaxWidth().weight(1f),
            value = state.taxCode,
            placeholder = "Enter tax code",
            label = "Tax Code *",
            isError = state.taxCodeError != null,
            supportingText = state.taxCodeError,
            onValueChange = {
                action(TaxSettingsActions.OnTaxCodeChange(it))
            })
    }
    MyTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.description,
        placeholder = "Enter description",
        label = "Description",
        isError = state.descriptionError != null,
        supportingText = state.descriptionError,
        onValueChange = {
            action(TaxSettingsActions.OnDescriptionChange(it))
        })
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {


        MyTextField(
            modifier = Modifier.fillMaxWidth().weight(1f),
            value = state.taxRate,
            placeholder = "Enter tax rate",
            label = "Tax Rate *",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = Icons.Outlined.Percent,
            isError = state.taxRateError != null,
            supportingText = state.taxRateError,
            onValueChange = {
                action(TaxSettingsActions.OnTaxRateChange(it))
            })

    }

    TaxComponentUi(state = state, action = {
        action(it)

    })

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Tax Components", style = MaterialTheme.typography.titleMedium)
        for (i in 0 until state.taxComponentsList.size) {
            TaxComponentListItem(state.taxComponentsList[i], onEdit = {
                action(TaxSettingsActions.OnTaxComponentChange(state.taxComponentsList[i]))
            })
        }
    }


}

@Composable
fun TaxComponentListItem(x0: TaxComponent, onEdit: () -> Unit) {
    Surface(
        modifier = Modifier.padding(horizontal = 8.dp),
        shape = MaterialTheme.shapes.small,
        color = Color.White,
        border = BorderStroke(0.5.dp, color = MaterialTheme.colorScheme.outline)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().clickable() { onEdit() }.padding(8.dp)
        ) {
            Text(
                text = x0.name.capitalize(Locale.current),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = x0.rate.toString()+" %",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TaxComponentUi(state: TaxSettingsState, action: (TaxSettingsActions) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        MyTextField(
            modifier = Modifier.fillMaxWidth().weight(1f),
            value = state.taxComponentName,
            placeholder = "Enter tax name",
            label = "Tax Name *",
            isError = state.taxNameError != null,
            supportingText = state.taxNameError,
            onValueChange = {
                action(TaxSettingsActions.OnTaxComponentAddNameChange(it))
            })
        Spacer(modifier = Modifier.width(16.dp))
        MyTextField(
            modifier = Modifier.fillMaxWidth().weight(1f),
            value = state.taxComponentRate.toString(),
            placeholder = "Enter tax rate",
            label = "Tax Rate *",
            isError = state.taxCodeError != null,
            supportingText = state.taxCodeError,
            onValueChange = {
                action(TaxSettingsActions.OnTaxComponentRateChange(it))
            })
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = {
                action(
                    TaxSettingsActions.OnTaxComponentAddClick
                )
            }, modifier = Modifier.padding(top = 12.dp), shape = MaterialTheme.shapes.medium
        ) {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = "Remove")
        }


    }
}


@Composable
fun WidePageTaxUiScreen(
    state: TaxSettingsState, action: (TaxSettingsActions) -> Unit, taxList: List<TaxSlab>
) {

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        WidePageHeader(
            title = "Tax Settings",
            subtitle = "Manage tax slabs and assign to products",
            addText = "Add Tax",
            onImport = {},
            onExport = {},
            onAdd = { action(TaxSettingsActions.OnAddClicked(true)) },
            icon = {
                Icon(imageVector = Icons.Outlined.CurrencyRupee, contentDescription = "Add")
            })
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            org.techcult.scaleos.core.presentation.components.SearchBar(
                searchText = state.searchQuery, onSearchTextChange = {
                    action(TaxSettingsActions.OnSearchQueryChange(it))
                }, modifier = Modifier.weight(5f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            MyFilterChip(
                modifier = Modifier.weight(1f),
                options = AvailabilityFilter.entries,
                selectedOption = state.filter.name,
                onOptionSelected = {
                    action(
                        TaxSettingsActions.OnStatusFilterChange(AvailabilityFilter.valueOf(it.name))
                    )
                })
        }
        Spacer(modifier = Modifier.height(24.dp))
        TaxTable(state, action, taxList)

    }

}

@Composable
fun TaxTable(state: TaxSettingsState, action: (TaxSettingsActions) -> Unit, list: List<TaxSlab>) {
    Column(
        modifier = Modifier.clip(shape = MaterialTheme.shapes.medium).fillMaxWidth()
            .background(Color.White),
    ) {
        TaxTableHeader()
        HorizontalDivider(thickness = 0.5.dp)
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {

                item {
                    if (list.isEmpty()) {
                        Text("No Tax Found")
                    }
                }
                itemsIndexed(list, key = { index, tax -> tax.id }) { index, tax ->
                    WideTaxListItem(tax, onEdit = {
                        action(TaxSettingsActions.OnEditOptionClick(tax))
                    })
                }
            }
        }

    }
}

@Composable
fun WideTaxListItem(x0: TaxSlab, onEdit: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {


    }
}

@Composable
fun TaxTableHeader() {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "TaxSlab",
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Percentage",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Status",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Updated",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Actions",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun CompactPageTaxUiScreen(
    state: TaxSettingsState, action: (TaxSettingsActions) -> Unit, taxList: List<TaxSlab>
) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        CompactPageHeader(
            title = "Tax Management",
            subtitle = "Organize your products into categories for better management",
            onExport = {},
            onImport = {},
            onAdd = {
                action(TaxSettingsActions.OnAddClicked(isDialogOpen = true))

            },
            onBack = {
                action(TaxSettingsActions.OnNavigateBack)

            })
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchBar(
                modifier = Modifier.weight(1f),
                searchText = state.searchQuery,
                onSearchTextChange = {

                    action(TaxSettingsActions.OnSearchQueryChange(it))

                })
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                action(TaxSettingsActions.OnFilterButtonClick(isStatusFiler = true))
            }) {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Edit")

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CompactTaxTable(action, state, list = taxList)
    }

}

@Composable
fun CompactTaxTable(
    actions: (TaxSettingsActions) -> Unit, state: TaxSettingsState, list: List<TaxSlab>
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

    } else {
        LazyColumn(
            modifier = Modifier.clip(shape = MaterialTheme.shapes.medium).fillMaxWidth()
                .background(Color.White)
        ) {

            item {
                if (list.isEmpty()) {
                    Text("No Tax Found")
                }
            }
            itemsIndexed(list, key = { index, tax -> tax.id }) { index, tax ->
                CompactTaxListItem(tax, onEdit = {
                    actions(TaxSettingsActions.OnEditOptionClick(tax))
                })
            }
        }
    }
}

@Composable
fun CompactTaxListItem(x0: TaxSlab, onEdit: (TaxSlab) -> Unit) {
    TODO("Not yet implemented")
}

