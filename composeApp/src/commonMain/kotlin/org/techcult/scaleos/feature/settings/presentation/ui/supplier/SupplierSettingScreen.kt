package org.techcult.scaleos.feature.settings.presentation.ui.supplier

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import org.techcult.scaleos.core.presentation.components.MoneyTextField
import org.techcult.scaleos.core.presentation.components.MyFilterChip
import org.techcult.scaleos.core.presentation.components.MyMultiLineTextField
import org.techcult.scaleos.core.presentation.components.MyTextDropDown
import org.techcult.scaleos.core.presentation.components.MyTextField
import org.techcult.scaleos.core.presentation.components.SearchBar
import org.techcult.scaleos.core.presentation.components.StatusDropDown
import org.techcult.scaleos.core.utils.DeviceConfiguration
import org.techcult.scaleos.core.utils.ObserveAsEvents
import org.techcult.scaleos.core.utils.PaymentTerms
import org.techcult.scaleos.core.utils.formatCurrency
import org.techcult.scaleos.core.utils.toFormattedString
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.CompactPageHeader
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.WidePageHeader
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.DialogButtons
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.DialogHeader
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.StatusChip
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import org.techcult.scaleos.feature.settings.presentation.viewmodel.SupplierDialogTab
import org.techcult.scaleos.feature.settings.presentation.viewmodel.SupplierSettingActions
import org.techcult.scaleos.feature.settings.presentation.viewmodel.SupplierSettingState
import org.techcult.scaleos.feature.settings.presentation.viewmodel.SupplierSettingViewModel
import org.techcult.scaleos.feature.settings.presentation.viewmodel.SupplierSettingsEvents
import org.techcult.scaleos.feature.supplier.domain.model.Supplier
import org.techcult.scaleos.feature.supplier.utils.SupplierType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierSettingScreen(
    onBack: () -> Unit,
    viewModel: SupplierSettingViewModel = koinViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val supplierList by viewModel.supplierList.collectAsStateWithLifecycle()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.event) {
        when (it) {
            is SupplierSettingsEvents.OnFailure -> {
                scope.launch {
                    snackBarHostState.showSnackbar(it.message)
                }
            }

            is SupplierSettingsEvents.OnSuccess -> {
                scope.launch {
                    snackBarHostState.showSnackbar(it.message)
                }


            }

        }
    }

    if (state.isAddDialogOpen) {
        if (deviceConfiguration == DeviceConfiguration.DESKTOP || deviceConfiguration == DeviceConfiguration.TABLET_LANDSCAPE) {
            AddSupplierDialog(state = state, action = {
                when (it) {
                    is SupplierSettingActions.OnNavigateBack ->
                        onBack()

                    else -> viewModel.onAction(it)


                }
            })
        } else {
            AddCompactSupplierDialog(state = state, action = viewModel::onAction)
        }
    }
    if (state.isStatusFilter) {
        ModalBottomSheet(onDismissRequest = {
            viewModel.onAction(SupplierSettingActions.OnFilterButtonClick(isStatusFiler = false))

        }, sheetState = sheetState)
        {
            AvailabilityFilter.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        viewModel.onAction(SupplierSettingActions.OnStatusFilterChange(option))
                        viewModel.onAction(SupplierSettingActions.OnFilterButtonClick(isStatusFiler = false))

                    })
            }


        }
    }


    Scaffold(snackbarHost = {
        androidx.compose.material3.SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.padding(bottom = 64.dp)
        )
    }) {
        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT -> CompactSupplierScreenUi(
                state,
                action = {
                    when (it) {
                        is SupplierSettingActions.OnNavigateBack ->
                            onBack()

                        else -> viewModel.onAction(it)
                    }
                },
                supplierList
            )

            DeviceConfiguration.MOBILE_LANDSCAPE -> TODO()
            DeviceConfiguration.TABLET_PORTRAIT -> TODO()
            DeviceConfiguration.TABLET_LANDSCAPE -> {
                WideSupplierScreenUi(state, viewModel::onAction, supplierList)
            }

            DeviceConfiguration.DESKTOP -> WideSupplierScreenUi(
                state,
                viewModel::onAction,
                supplierList
            )
        }


    }


}

@Composable
fun AddCompactSupplierDialog(
    state: SupplierSettingState,
    action: (SupplierSettingActions) -> Unit
) {
    val isBasicVisible = remember {
        mutableStateOf(true)

    }
    val isContactVisible = remember {
        mutableStateOf(true)

    }
    val isTermsVisible = remember {
        mutableStateOf(true)

    }
    Dialog(onDismissRequest = { action(SupplierSettingActions.OnDismissDialog) }) {
        Surface(
            modifier = Modifier,
            color = Color.White,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp).verticalScroll(state = rememberScrollState())
            ) {
                DialogHeader(
                    isEditMode = false,
                    onDismissRequest = { action(SupplierSettingActions.OnDismissDialog) },
                    title = "Supplier"
                )
                CardHeaderWithDropDown(
                    title = "Basic Information",
                    isVisible = isBasicVisible.value,
                    onDropDownClick = {
                        isBasicVisible.value = !isBasicVisible.value
                    }
                )
                AnimatedVisibility(isBasicVisible.value)
                {
                    CompactBasicFields(state, action)

                }
                CardHeaderWithDropDown(
                    title = "Contact Information",
                    isVisible = isContactVisible.value,
                    onDropDownClick = {
                        isContactVisible.value = !isContactVisible.value
                    })
                AnimatedVisibility(isContactVisible.value)
                {
                    CompactContactFields(state, action)
                }

                CardHeaderWithDropDown(
                    title = "Business Terms",
                    isVisible = isTermsVisible.value,
                    onDropDownClick = {
                        isTermsVisible.value = !isTermsVisible.value
                    }
                )
                AnimatedVisibility(isTermsVisible.value)
                {
                    CompactBusinessTermsFields(state, action)
                }
                Spacer(modifier = Modifier.height(16.dp))

                DialogButtons(
                    onDismiss = { action(SupplierSettingActions.OnDismissDialog) },
                    onCreate = { action(SupplierSettingActions.OnSaveClick) },
                    isEditMode = state.isEditMode,
                    title = "Supplier"
                )
            }
        }
    }
}

@Composable
fun CompactBasicFields(state: SupplierSettingState, action: (SupplierSettingActions) -> Unit) {
    val textPattern = remember { Regex("^[a-zA-Z\\s]*$") } // Allows letters and spaces
    val alphanumericPattern = remember { Regex("^[a-zA-Z0-9\\s\\n]*$") }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        MyTextField(
            modifier = Modifier,
            value = state.supplierName,
            placeholder = "Enter supplier name",
            label = "Supplier Name *",
            isError = state.supplierNameError != null,
            supportingText = state.supplierNameError,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(textPattern)) {
                    action(SupplierSettingActions.OnSupplierNameChange(newValue))
                }

            }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )
        MyTextField(
            readOnly = true,
            modifier = Modifier,
            value = state.supplierCode,
            placeholder = "Enter supplier code",
            label = "Supplier Code",
            onValueChange = {
                action(SupplierSettingActions.OnSupplierCodeChange(it))

            }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )
        MyTextField(
            modifier = Modifier,
            value = state.contactPerson ?: "",
            placeholder = "Enter contact person name",
            label = "Contact Person *",
            onValueChange = {
                action(SupplierSettingActions.OnContactPersonChange(it))

            })

        MyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.supplierNotes ?: "",
            placeholder = "Enter notes about this supplier",
            label = "Notes",
            onValueChange = {
                action(SupplierSettingActions.OnSupplierNotesChange(it))
            },
            singleLine = false,
            maxLines = 4
        )
        StatusDropDown(selectedValue = state.isAvailable, onValueChange = {
            action(SupplierSettingActions.OnAvailabilityChange(it))
        }, modifier = Modifier)
    }

}

@Composable
fun CompactContactFields(state: SupplierSettingState, action: (SupplierSettingActions) -> Unit) {
    val alphanumericPattern = remember { Regex("^[a-zA-Z0-9\\s\\n]*$") }
    val emailPattern = remember { Regex("^[a-zA-Z0-9\\s\\n@.]*$") }
    val textPattern = remember { Regex("^[a-zA-Z\\s]*$") } // Allows letters and spaces

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MyTextField(
            modifier = Modifier,
            value = state.email ?: "",
            placeholder = "Enter email address",
            label = "Email",
            isError = state.emailError != null,
            supportingText = state.emailError,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(emailPattern))
                    action(SupplierSettingActions.OnEmailChange(newValue))
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        MyTextField(
            modifier = Modifier,
            value = state.contactNumber ?: "",
            placeholder = "Enter phone number",
            label = "Phone *",
            isError = state.contactNumberError != null,
            supportingText = state.contactNumberError,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^[0-9]*$")) && newValue.length <= 10) {
                    action(SupplierSettingActions.OnContactNumberChange(newValue))
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )

        MyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.gstNumber ?: "",
            placeholder = "Enter GST number",
            label = "GST No",
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(alphanumericPattern) && newValue.length <= 16) {
                    action(SupplierSettingActions.OnGstNumberChange(newValue.uppercase()))
                }
            }
        )
        Text(
            "Address Information",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        MyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.address ?: "",
            placeholder = "Enter street address",
            label = "Street Address *",
            isError = state.addressError != null,
            supportingText = state.addressError,
            onValueChange = { action(SupplierSettingActions.OnAddressChange(it)) }
        )
        MyTextField(
            modifier = Modifier,
            value = state.city ?: "",
            placeholder = "Enter city",
            label = "City *",
            isError = state.cityError != null,
            supportingText = state.cityError,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(textPattern))
                    action(SupplierSettingActions.OnCityChange(newValue))
            }
        )
        MyTextField(
            modifier = Modifier,
            value = state.state ?: "",
            placeholder = "Enter state or province",
            label = "State/Province *",
            isError = state.stateError != null,
            supportingText = state.stateError,

            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(textPattern)) {
                    action(SupplierSettingActions.OnStateChange(newValue))
                }
            }
        )

        MyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.pinCode ?: "",
            placeholder = "Enter ZIP or postal code",
            label = "ZIP/Postal Code *",
            isError = state.pinCodeError != null,
            supportingText = state.pinCodeError,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^[0-9]*$")) && newValue.length <= 6)
                    action(SupplierSettingActions.OnPinCodeChange(newValue))
            }
        )
    }
}


@Composable
fun CompactBusinessTermsFields(
    state: SupplierSettingState,
    action: (SupplierSettingActions) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MyTextDropDown(
            label = "Supplier Type",
            options = SupplierType.entries.map { supplierType -> supplierType.name },
            selectedValue = state.supplierType,
            onValueChange = { a1 ->
                action(SupplierSettingActions.OnSupplierTypeChange(a1))
            }, modifier = Modifier
        )
        Spacer(modifier = Modifier.width(16.dp))



        MoneyTextField(
            prefix = "₹",
            modifier = Modifier,
            value = state.openingBalance.toString(),
            placeholder = "0.00",
            label = "Opening Balance",
            onValueChange = { newValue ->
                if (newValue.matches(Regex("^[0-9]*\\.?[0-9]*$")))
                    action(SupplierSettingActions.OnOpeningBalanceChange(newValue))
            },
        )


        MyTextDropDown(
            label = "Payment Terms *",
            options = PaymentTerms.entries.map { paymentTerms -> paymentTerms.value },
            selectedValue = state.selectedPaymentTerms,
            onValueChange = { a1 ->
                action(SupplierSettingActions.OnPaymentTermsChange(a1))
            }
        )
        MoneyTextField(
            prefix = "₹",
            modifier = Modifier,
            value = state.openingBalance.toString(),
            placeholder = "0.00",
            label = "Credit Limit",
            onValueChange = { action(SupplierSettingActions.OnOpeningBalanceChange(it)) },
        )
    }
}


@Composable
fun AddSupplierDialog(state: SupplierSettingState, action: (SupplierSettingActions) -> Unit) {

    Dialog(onDismissRequest = { action(SupplierSettingActions.OnDismissDialog) }) {
        Surface(
            modifier = Modifier,
            color = Color.White,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DialogHeader(
                    isEditMode = false,
                    onDismissRequest = { action(SupplierSettingActions.OnDismissDialog) },
                    title = "Supplier"
                )
                Spacer(modifier = Modifier.height(16.dp))

                SecondaryTabRow(
                    state.selectedTab.ordinal,
                    Modifier,
                    TabRowDefaults.primaryContainerColor,
                    TabRowDefaults.primaryContentColor,
                    @Composable {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(
                                state.selectedTab.ordinal,
                                matchContentSize = false
                            )
                        )
                    },
                    @Composable { HorizontalDivider() }) {
                    SupplierDialogTab.entries.forEach { tab ->
                        Tab(
                            selected = state.selectedTab == tab,
                            onClick = { action(SupplierSettingActions.OnTabSelected(tab)) },
                            text = { Text(text = tab.name.replace("_", " ")) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    when (state.selectedTab) {
                        SupplierDialogTab.BASIC_INFO -> BasicInfoFields(state, action)
                        SupplierDialogTab.CONTACT_ADDRESS -> SupplierContactFields(state, action)
                        SupplierDialogTab.BUSINESS_TERMS -> BusinessTermsFields(state, action)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                DialogButtons(
                    onDismiss = { action(SupplierSettingActions.OnDismissDialog) },
                    onCreate = { action(SupplierSettingActions.OnSaveClick) },
                    isEditMode = state.isEditMode,
                    title = "Supplier"
                )
            }
        }
    }
}

@Composable
fun CardHeaderWithDropDown(title: String, isVisible: Boolean, onDropDownClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Text(text = title)
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = onDropDownClick)
        {
            Icon(
                imageVector = if (isVisible) Icons.Outlined.ExpandMore else Icons.Outlined.ExpandLess,
                contentDescription = "Edit"
            )

        }
    }
}

@Composable
fun BasicInfoFields(state: SupplierSettingState, action: (SupplierSettingActions) -> Unit) {
    val textPattern = remember { Regex("^[a-zA-Z\\s]*$") } // Allows letters and spaces
    val alphanumericPattern = remember { Regex("^[a-zA-Z0-9\\s\\n]*$") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row {
            MyTextField(
                modifier = Modifier.weight(1f),
                value = state.supplierName,
                placeholder = "Enter supplier name",
                label = "Supplier Name *",
                isError = state.supplierNameError != null,
                supportingText = state.supplierNameError,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(textPattern)) {
                        action(SupplierSettingActions.OnSupplierNameChange(newValue))
                    }

                }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.width(16.dp))
            MyTextField(
                readOnly = true,
                modifier = Modifier.weight(1f),
                value = state.supplierCode,
                placeholder = "Enter supplier code",
                label = "Supplier Code",
                onValueChange = {
                    action(SupplierSettingActions.OnSupplierCodeChange(it))

                }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextField(
                modifier = Modifier.weight(1f),
                value = state.contactPerson ?: "",
                placeholder = "Enter contact person name",
                label = "Contact Person",
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(textPattern)) {
                        action(SupplierSettingActions.OnContactPersonChange(newValue))
                    }

                })
            Spacer(modifier = Modifier.width(16.dp))

            StatusDropDown(selectedValue = state.isAvailable, onValueChange = {
                action(SupplierSettingActions.OnAvailabilityChange(it))
            }, modifier = Modifier.weight(1f))
        }


        MyMultiLineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.supplierNotes ?: "",
            placeholder = "Enter notes about this supplier",
            label = "Notes",
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(alphanumericPattern)) {
                    action(SupplierSettingActions.OnSupplierNotesChange(newValue))
                }
            },
        )
    }
}

@Composable
fun SupplierContactFields(state: SupplierSettingState, action: (SupplierSettingActions) -> Unit) {
    val alphanumericPattern = remember { Regex("^[a-zA-Z0-9\\s\\n]*$") }
    val emailPattern = remember { Regex("^[a-zA-Z0-9\\s\\n@.]*$") }
    val textPattern = remember { Regex("^[a-zA-Z\\s]*$") } // Allows letters and spaces


    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            MyTextField(
                modifier = Modifier.weight(1f),
                value = state.email ?: "",
                placeholder = "Enter email address",
                label = "Email",
                isError = state.emailError != null,
                supportingText = state.emailError,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(emailPattern))
                        action(SupplierSettingActions.OnEmailChange(newValue))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.width(16.dp))
            MyTextField(
                modifier = Modifier.weight(1f),
                value = state.contactNumber ?: "",
                placeholder = "Enter phone number",
                label = "Phone *",
                isError = state.contactNumberError != null,
                supportingText = state.contactNumberError,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^[0-9]*$")) && newValue.length <= 10) {
                        action(SupplierSettingActions.OnContactNumberChange(newValue))
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
            )
        }
        MyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.gstNumber ?: "",
            placeholder = "Enter GST number",
            label = "GST No",
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(alphanumericPattern) && newValue.length <= 16) {
                    action(SupplierSettingActions.OnGstNumberChange(newValue.uppercase()))
                }
            }
        )
        Text(
            "Address Information",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        MyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.address ?: "",
            placeholder = "Enter street address",
            label = "Street Address *",
            isError = state.addressError != null,
            supportingText = state.addressError,
            onValueChange = { action(SupplierSettingActions.OnAddressChange(it)) }
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            MyTextField(
                modifier = Modifier.weight(1f),
                value = state.city ?: "",
                placeholder = "Enter city",
                label = "City *",
                isError = state.cityError != null,
                supportingText = state.cityError,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(textPattern))
                        action(SupplierSettingActions.OnCityChange(newValue))
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            MyTextField(
                modifier = Modifier.weight(1f),
                value = state.state ?: "",
                placeholder = "Enter state or province",
                label = "State/Province *",
                isError = state.stateError != null,
                supportingText = state.stateError,

                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(textPattern)) {
                        action(SupplierSettingActions.OnStateChange(newValue))
                    }
                }
            )
        }
        MyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.pinCode ?: "",
            placeholder = "Enter ZIP or postal code",
            label = "ZIP/Postal Code *",
            isError = state.pinCodeError != null,
            supportingText = state.pinCodeError,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^[0-9]*$")) && newValue.length <= 6)
                    action(SupplierSettingActions.OnPinCodeChange(newValue))
            }
        )
    }
}

@Composable
fun BusinessTermsFields(state: SupplierSettingState, action: (SupplierSettingActions) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            MyTextDropDown(
                label = "Supplier Type",
                options = SupplierType.entries.map { supplierType -> supplierType.name },
                selectedValue = state.supplierType,
                onValueChange = { a1 ->
                    action(SupplierSettingActions.OnSupplierTypeChange(a1))
                }, modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))



            MoneyTextField(
                prefix = "₹",
                modifier = Modifier.weight(1f),
                value = state.openingBalance.toString(),
                placeholder = "0.00",
                label = "Opening Balance",
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^[0-9]*\\.?[0-9]*$")))
                        action(SupplierSettingActions.OnOpeningBalanceChange(newValue))
                },
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            MyTextDropDown(
                label = "Payment Terms",
                options = PaymentTerms.entries.map { paymentTerms -> paymentTerms.name },
                selectedValue = state.selectedPaymentTerms,
                onValueChange = { a1 ->
                    action(SupplierSettingActions.OnPaymentTermsChange(a1))
                }, modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))



            MoneyTextField(
                prefix = "₹",
                modifier = Modifier.weight(1f),
                value = state.creditLimit.toString(),
                placeholder = "0.00",
                label = "Credit Limit",
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^[0-9]*\\.?[0-9]*$")))
                        action(SupplierSettingActions.OnCreditLimitChange(newValue))
                },
            )
        }
    }
}


@Composable
fun CompactSupplierScreenUi(
    state: SupplierSettingState,
    action: (SupplierSettingActions) -> Unit,
    supplierList: List<Supplier>
) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        CompactPageHeader(
            title = "Supplier Management",
            subtitle = "Organize your products into categories for better management",
            onExport = {},
            onImport = {},
            onAdd = {
                action(SupplierSettingActions.OnAddDialogClick(isAddDialogOpen = true))

            }, onBack = {
                action(SupplierSettingActions.OnNavigateBack)

            })
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchBar(
                modifier = Modifier.weight(1f),
                searchText = state.searchQuery,
                onSearchTextChange = {

                    action(SupplierSettingActions.OnSearchQueryChange(it))

                })
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                action(SupplierSettingActions.OnFilterButtonClick(isStatusFiler = true))
            })
            {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Edit")

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CompactSupplierTable(action, state, list = supplierList)

    }


}

@Composable
fun CompactSupplierTable(
    action: (SupplierSettingActions) -> Unit,
    state: SupplierSettingState,
    list: List<Supplier>
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()

        }
    } else {
        LazyColumn(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.White)) {
            if (list.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No suppliers Found")

                    }
                }
            }
            itemsIndexed(list) { index, supplier ->
                CompactsupplierListItem(supplier = supplier, onEdit = {
                    action(SupplierSettingActions.OnEditOptionClick(it))

                })
                if (index < list.lastIndex) {
                    HorizontalDivider(thickness = 0.5.dp)
                }


            }
        }
    }

}

@Composable
fun CompactsupplierListItem(supplier: Supplier, onEdit: (Supplier) -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    supplier.supplierName.capitalize(Locale.current),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    supplier.supplierCode,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )


            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Spacer(modifier = Modifier.weight(1f))
        StatusChip(supplier.isAvailable)
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Edit",
                modifier = Modifier.clickable {
                    onEdit(supplier)
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }

    }

}

@Composable
fun WideSupplierScreenUi(
    state: SupplierSettingState,
    onAction: (SupplierSettingActions) -> Unit,
    supplierList: List<Supplier>
) {
    Column(modifier = Modifier.fillMaxSize().padding(all = 16.dp)) {
        WidePageHeader(
            title = "Supplier Management",
            subtitle = "Organize your suppliers for better management",
            onExport = { /*TODO*/ },
            onImport = { /*TODO*/ },
            onAdd = {
                onAction(SupplierSettingActions.OnAddDialogClick(isAddDialogOpen = true))

            },
            addText = "Add Supplier",
            icon = {
                Icon(
                    imageVector = Icons.Outlined.SupervisorAccount,
                    contentDescription = "Supplier"
                )
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchBar(searchText = state.searchQuery, onSearchTextChange = {
                onAction(SupplierSettingActions.OnSearchQueryChange(it))
            }, modifier = Modifier.weight(5f))
            Spacer(modifier = Modifier.width(16.dp))
            MyFilterChip(
                modifier = Modifier.weight(1f),
                options = AvailabilityFilter.entries,
                selectedOption = state.statusFilter.name,
                onOptionSelected = {
                    onAction(
                        SupplierSettingActions.OnStatusFilterChange(AvailabilityFilter.valueOf(it.name))
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        SupplierTable(state = state, action = onAction, supplierList)


    }


}

@Composable
fun SupplierTable(
    state: SupplierSettingState,
    action: (SupplierSettingActions) -> Unit,
    x2: List<Supplier>
) {

    Column(
        modifier = Modifier.fillMaxWidth().clip(shape = MaterialTheme.shapes.medium)
            .background(color = androidx.compose.ui.graphics.Color.White)
    )
    {
        SupplierListHeader()
        HorizontalDivider(thickness = 0.4.dp)
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
            {
                CircularProgressIndicator()
            }
        } else {
            SupplierList(supplierList = x2, onEdit = {
                action(SupplierSettingActions.OnEditOptionClick(it))
            })
        }

    }
}

@Composable
fun SupplierListHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            "Supplier",
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Contact",
            modifier = Modifier.weight(2f),
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
            "TotalPurchase",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "LastOrder",
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
fun SupplierList(supplierList: List<Supplier>, onEdit: (Supplier) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        if (supplierList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Suppliers Found")
                }
            }
        }

        itemsIndexed(supplierList) { index, supplier ->
            SupplierListItem(supplier = supplier, onEdit)
            if (index < supplierList.lastIndex) {
                HorizontalDivider(thickness = 0.5.dp)
            }

        }

    }
}

@Composable
fun SupplierListItem(supplier: Supplier, onEdit: (Supplier) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = supplier.supplierName.capitalize(Locale.current),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = supplier.supplierCode,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }



        Column(modifier = Modifier.weight(2f)) {
            Text(
                text = supplier.contactNumber.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = supplier.email.toString(),
                style = MaterialTheme.typography.bodySmall, color = Color.Gray
            )
        }
        Row(modifier = Modifier.weight(1f)) {
            StatusChip(status = supplier.isAvailable)

        }
        Text(
            text = "₹ " + formatCurrency(supplier.totalPurchases.toString()),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
            Text(
                text = if (supplier.lastOrderDate == null) "N/A" else supplier.lastOrderDate.toFormattedString(),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

        Row(modifier = Modifier.weight(1f)) {
            IconButton(onClick = {
                onEdit(supplier)
            }) {
                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
            }
        }

    }


}
