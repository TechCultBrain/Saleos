@file:OptIn(ExperimentalMaterial3Api::class)

package org.techcult.scaleos.feature.settings.presentation.ui.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
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
import org.techcult.scaleos.feature.product.domain.model.Department
import org.techcult.scaleos.feature.product.domain.model.Uom
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.CompactPageHeader
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.WidePageHeader
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import org.techcult.scaleos.feature.settings.presentation.viewmodel.DepartmentSettingsViewModel
import org.techcult.scaleos.feature.settings.presentation.viewmodel.DeptSettingActions
import org.techcult.scaleos.feature.settings.presentation.viewmodel.DeptSettingState
import org.techcult.scaleos.feature.settings.presentation.viewmodel.DeptSettingsEvents
import org.techcult.scaleos.feature.settings.presentation.viewmodel.UnitSettingActions
import org.techcult.scaleos.feature.settings.presentation.viewmodel.UnitSettingState
import org.techcult.scaleos.feature.settings.presentation.viewmodel.UnitSettingsEvents

@Composable
fun DepartmentSettingScreen(
    onBack: () -> Unit,
    viewModel: DepartmentSettingsViewModel = koinViewModel()
) {

    val sheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val deptList by viewModel.deptList.collectAsStateWithLifecycle()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.event) {
        when (it) {
            is DeptSettingsEvents.OnFailure -> {
                scope.launch {
                    snackBarHostState.showSnackbar(it.message)
                }
            }

            is DeptSettingsEvents.OnSuccess -> {
                scope.launch {
                    snackBarHostState.showSnackbar(it.message)
                }


            }
        }
    }

    if (state.isAddDialogOpen) {
        AddDepartmentDialog(state = state, action = {
            when (it) {
                DeptSettingActions.OnNavigateBack ->
                    onBack()

                else -> viewModel.onAction(it)


            }
        })
    }
    if (state.isStatusFilter) {
        ModalBottomSheet(onDismissRequest = {
            viewModel.onAction(DeptSettingActions.OnFilterButtonClick(isStatusFiler = false))

        }, sheetState = sheetState)
        {
            AvailabilityFilter.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        viewModel.onAction(DeptSettingActions.OnStatusFilterChange(option))
                        viewModel.onAction(DeptSettingActions.OnFilterButtonClick(isStatusFiler = false))

                    })
            }


        }
    }

    Scaffold(snackbarHost = {
        androidx.compose.material3.SnackbarHost(hostState = snackBarHostState, modifier = Modifier.padding(bottom = 64.dp))
    }) {
        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT -> CompactDepartmentScreenUi(
                state,
                viewModel::onAction,
                deptList
            )

            DeviceConfiguration.MOBILE_LANDSCAPE -> TODO()
            DeviceConfiguration.TABLET_PORTRAIT -> TODO()
            DeviceConfiguration.TABLET_LANDSCAPE -> TODO()
            DeviceConfiguration.DESKTOP -> WideDepartmentScreenUi(state, viewModel::onAction, deptList)
        }


    }


}

@Composable
fun AddDepartmentDialog(state: DeptSettingState, action: (DeptSettingActions) -> Unit) {
    Dialog(onDismissRequest = { action(DeptSettingActions.OnDismissDialog) }) {
        Surface(modifier = Modifier, color = Color.White, shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                DialogHeader(
                    isEditMode = false,
                    onDismissRequest = { action(DeptSettingActions.OnDismissDialog) },
                    title = "Unit"
                )
                Spacer(modifier = Modifier.height(16.dp))

                //BasicInfoFields(state, action)
                MyTextField(
                    value = state.deptName,
                    placeholder = "Enter dept name",
                    label = "Department Name *",
                    onValueChange = {
                        action(DeptSettingActions.OnDeptNameChange(it))

                    })

                Spacer(modifier = Modifier.height(16.dp))
                MyTextField(value = state.description, onValueChange = {
                    action(DeptSettingActions.OnDescriptionChange(it))

                }, placeholder = "Enter department description", label = "Department Description")
                Spacer(modifier = Modifier.height(16.dp))
                if (state.isEditMode) {
                    AvailabilityField(
                        state.isAvailable,
                        onChange = { action(DeptSettingActions.OnAvailabilityChange(it)) })
                }
                Spacer(modifier = Modifier.height(16.dp))
                DialogButtons(
                    onDismiss = { action(DeptSettingActions.OnDismissDialog) },
                    onCreate = { action(DeptSettingActions.OnSaveClick) },
                    isEditMode = state.isEditMode,
                    title = "Department"
                )


            }
        }
    }
}

@Composable
fun CompactDepartmentScreenUi(
    state: DeptSettingState,
    action: (DeptSettingActions) -> Unit,
    list: List<Department>
) {

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        CompactPageHeader(
            title = "Department Management",
            subtitle = "Organize your products into department for better management",
            onExport = {},
            onImport = {},
            onAdd = {
                action(DeptSettingActions.OnAddDialogClick(isAddDialogOpen = true))

            }, onBack = {
                action(DeptSettingActions.OnNavigateBack)

            })
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchBar(
                modifier = Modifier.weight(1f),
                searchText = state.searchQuery,
                onSearchTextChange = {

                    action(DeptSettingActions.OnSearchQueryChange(it))

                })
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                action(DeptSettingActions.OnFilterButtonClick(isStatusFiler = true))
            })
            {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Edit")

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CompactDepartmentTable(action, state, list)

    }

}

@Composable
fun CompactDepartmentTable(
    action: (DeptSettingActions) -> Unit,
    state: DeptSettingState,
    list: List<Department>
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
                        Text(text = "No Departments Found")

                    }
                }
            }
            itemsIndexed(list) { index, dept ->
                CompactdeptListItem(department = dept, onEdit = {
                    action(DeptSettingActions.OnEditOptionClick(it))

                })
                if (index < list.lastIndex) {
                    HorizontalDivider(thickness = 0.5.dp)
                }


            }
        }
    }

}

@Composable
fun CompactdeptListItem(department: Department, onEdit: (Department) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(department.departmentName.capitalize(Locale.current))
            Spacer(modifier = Modifier.height(4.dp))
            department.description?.let {
                Text(
                    it.uppercase(),
                    style = MaterialTheme.typography.bodySmall
                )
            }


        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StatusChip(department.isAvailable)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Edit",
                modifier = Modifier.clickable {
                    onEdit(department)
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }

    }
}


@Composable
fun WideDepartmentScreenUi(
    state: DeptSettingState,
    onAction: (DeptSettingActions) -> Unit,
    deptList: List<Department>
) {
    Column(modifier = Modifier.fillMaxSize().padding(all = 16.dp)) {
        WidePageHeader(
            title = "Department Management",
            subtitle = "Organize your business into department for better management",
            onExport = { /*TODO*/ },
            onImport = { /*TODO*/ },
            onAdd = {
                onAction(DeptSettingActions.OnAddDialogClick(isAddDialogOpen = true))

            },
            addText = "Add Unit"
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchBar(searchText = state.searchQuery, onSearchTextChange = {
                onAction(DeptSettingActions.OnSearchQueryChange(it))
            }, modifier = Modifier.weight(5f))
            Spacer(modifier = Modifier.width(16.dp))
            MyFilterChip(
                modifier = Modifier.weight(1f),
                options = AvailabilityFilter.entries,
                selectedOption = state.statusFilter.name,
                onOptionSelected = {
                    onAction(
                        DeptSettingActions.OnStatusFilterChange(it)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        UnitTable(state = state, action = onAction, deptList)


    }


}

@Composable
fun UnitTable(state: DeptSettingState, action: (DeptSettingActions) -> Unit, x2: List<Department>) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .background(color = androidx.compose.ui.graphics.Color.White)
    ) {
        DepartmentListHeader()
        HorizontalDivider()
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
            {
                CircularProgressIndicator()
            }
        } else {
            DepartmentList(deptList = x2, onEdit = {
                action(DeptSettingActions.OnEditOptionClick(it))
            })
        }

    }
}

@Composable
fun DepartmentListHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            "Department Name",
            modifier = Modifier.weight(4f),
            style = MaterialTheme.typography.titleMedium
        )

        Text("Status", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text(
            text = "CreatedAt",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "Actions",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun DepartmentList(deptList: List<Department>, onEdit: (Department) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        if (deptList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Units Found")
                }
            }
        }

        itemsIndexed(deptList) { index, dept ->
            DepartmentListItem(dept = dept, onEdit)
            if (index < deptList.lastIndex) {
                HorizontalDivider(thickness = 0.5.dp)
            }

        }

    }
}

@Composable
fun DepartmentListItem(dept: Department, onEdit: (Department) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(4f)) {
            Text(dept.departmentName.capitalize(Locale.current), modifier = Modifier)
            dept.description?.let {
                Text(
                    it.capitalize(Locale.current),
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Row(modifier = Modifier.weight(1f)) {
            StatusChip(dept.isAvailable)
        }
        Text(
            dept.createdAt.toFormattedString().toString(),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.weight(1f)) {
            IconButton(onClick = {
                onEdit(dept)
            }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}