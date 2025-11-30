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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.BrandingWatermark
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.BrandingWatermark
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.techcult.scaleos.core.presentation.components.MyFilterChip
import org.techcult.scaleos.core.presentation.components.MyTextField
import org.techcult.scaleos.core.presentation.components.SearchBar
import org.techcult.scaleos.core.utils.DeviceConfiguration
import org.techcult.scaleos.core.utils.FilePicker
import org.techcult.scaleos.core.utils.ObserveAsEvents
import org.techcult.scaleos.core.utils.toFormattedString
import org.techcult.scaleos.feature.product.domain.model.Brand
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.CompactPageHeader
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.WidePageHeader
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import org.techcult.scaleos.feature.settings.presentation.viewmodel.BrandSettingActions
import org.techcult.scaleos.feature.settings.presentation.viewmodel.BrandSettingState
import org.techcult.scaleos.feature.settings.presentation.viewmodel.BrandSettingsEvents
import org.techcult.scaleos.feature.settings.presentation.viewmodel.BrandSettingsViewModel
import scaleos.composeapp.generated.resources.Res
import scaleos.composeapp.generated.resources.img_placeholder

@Composable
fun BrandSettingsScreen(onBack: () -> Unit, viewModel: BrandSettingsViewModel = koinViewModel()) {
    val sheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val brandList by viewModel.brandList.collectAsStateWithLifecycle()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.event) {
        when (it) {
            is BrandSettingsEvents.OnFailure -> {
                scope.launch {
                    snackBarHostState.showSnackbar(it.message)
                }
            }

            is BrandSettingsEvents.OnSuccess -> {
                scope.launch {
                    snackBarHostState.showSnackbar(it.message)
                }


            }

            else -> {}
        }
    }

    if (state.isAddDialogOpen) {
        AddBrandDialog(state = state, action = {
            when (it) {
                BrandSettingActions.OnNavigateBack ->
                    onBack()

                else -> viewModel.onAction(it)


            }
        })
    }
    if (state.isStatusFilter) {
        ModalBottomSheet(onDismissRequest = {
            viewModel.onAction(BrandSettingActions.OnFilterButtonClick(isStatusFiler = false))

        }, sheetState = sheetState)
        {
            AvailabilityFilter.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        viewModel.onAction(BrandSettingActions.OnStatusFilterChange(option))
                        viewModel.onAction(BrandSettingActions.OnFilterButtonClick(isStatusFiler = false))

                    })
            }


        }
    }
    if (state.isAddImageDialogOpen) {
        FilePicker { uri ->
            viewModel.onAction(BrandSettingActions.OnBrandImageChange(uri.toString()))

        }
    }
    if (state.isImagePreviewOpen) {
        Dialog(onDismissRequest = {
            viewModel.onAction(BrandSettingActions.OnImagePreviewClick("", false))
        }) {
            Surface(
                modifier = Modifier.size(300.dp),
                color = Color.White,
                shape = MaterialTheme.shapes.medium
            ) {
                Box(modifier = Modifier.fillMaxSize())
                {
                    AsyncImage(
                        model = state.brandImage,
                        contentDescription = "Brand Image",
                        modifier = Modifier.fillMaxSize(),
                        onError = { it ->  },
                        placeholder = painterResource(Res.drawable.img_placeholder),
                        error = painterResource(Res.drawable.img_placeholder)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            viewModel.onAction(BrandSettingActions.OnImagePreviewClick("", false))
                        }) {
                            Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "Edit")

                        }
                    }
                }
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
            DeviceConfiguration.MOBILE_PORTRAIT -> CompactBrandScreenUi(
                state,
                viewModel::onAction,
                brandList
            )

            DeviceConfiguration.MOBILE_LANDSCAPE -> TODO()
            DeviceConfiguration.TABLET_PORTRAIT -> TODO()
            DeviceConfiguration.TABLET_LANDSCAPE -> {
                WideBrandScreenUi(state, viewModel::onAction, brandList)
            }

            DeviceConfiguration.DESKTOP -> WideBrandScreenUi(state, viewModel::onAction, brandList)
        }


    }


}

@Composable
fun AddBrandDialog(state: BrandSettingState, action: (BrandSettingActions) -> Unit) {
    Dialog(onDismissRequest = { action(BrandSettingActions.OnDismissDialog) }) {
        Surface(modifier = Modifier, color = Color.White, shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                DialogHeader(
                    isEditMode = false,
                    onDismissRequest = { action(BrandSettingActions.OnDismissDialog) },
                    title = "Brand"
                )
                Spacer(modifier = Modifier.height(16.dp))

                //BasicInfoFields(state, action)
                MyTextField(
                    value = state.brandName,
                    placeholder = "Enter brand name",
                    label = "Brand Name *",
                    onValueChange = {
                        action(BrandSettingActions.OnBrandNameChange(it))

                    })
                Spacer(modifier = Modifier.height(16.dp))
                Text("Brand Image", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = MaterialTheme.shapes.large,

                        ) {

                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = state.brandImage,
                            contentDescription = "Brand Image",
                            onError = { it ->
                                // it.result.throwable.printStackTrace()

                            },
                            contentScale = ContentScale.FillBounds,
                            placeholder = painterResource(Res.drawable.img_placeholder),
                            error = painterResource(Res.drawable.img_placeholder)


                        )

                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Button(onClick = {
                            action(BrandSettingActions.OnBrandImageAddClick(true))

                        }, shape = RoundedCornerShape(8.dp)) {
                            Text("Upload Image")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedButton(onClick = {
                            action(BrandSettingActions.OnBrandImageClear)

                        }, shape = RoundedCornerShape(8.dp)) {
                            Text("Clear Image")
                        }
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))
                if (state.isEditMode) {
                    AvailabilityField(
                        state.isAvailable,
                        onChange = { action(BrandSettingActions.OnAvailabilityChange(it)) })
                }
                Spacer(modifier = Modifier.height(16.dp))
                DialogButtons(
                    onDismiss = { action(BrandSettingActions.OnDismissDialog) },
                    onCreate = { action(BrandSettingActions.OnSaveClick) },
                    isEditMode = state.isEditMode,
                    title = "Brand"
                )


            }
        }
    }
}

@Composable
fun CompactBrandScreenUi(
    state: BrandSettingState,
    action: (BrandSettingActions) -> Unit,
    list: List<Brand>
) {

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        CompactPageHeader(
            title = "Brand Management",
            subtitle = "Organize your products into categories for better management",
            onExport = {},
            onImport = {},
            onAdd = {
                action(BrandSettingActions.OnAddDialogClick(isAddDialogOpen = true))

            }, onBack = {
                action(BrandSettingActions.OnNavigateBack)

            })
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchBar(
                modifier = Modifier.weight(1f),
                searchText = state.searchQuery,
                onSearchTextChange = {

                    action(BrandSettingActions.OnSearchQueryChange(it))

                })
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                action(BrandSettingActions.OnFilterButtonClick(isStatusFiler = true))
            })
            {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Edit")

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CompactBrandTable(action, state, list)

    }

}

@Composable
fun CompactBrandTable(
    action: (BrandSettingActions) -> Unit,
    state: BrandSettingState,
    list: List<Brand>
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
                        Text(text = "No Brands Found")

                    }
                }
            }
            itemsIndexed(list) { index, brand ->
                CompactBrandListItem(brand = brand, onEdit = {
                    action(BrandSettingActions.OnEditOptionClick(it))

                })
                if (index < list.lastIndex) {
                    HorizontalDivider(thickness = 0.5.dp)
                }


            }
        }
    }

}

@Composable
fun CompactBrandListItem(brand: Brand, onEdit: (Brand) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = MaterialTheme.shapes.large,

                ) {

                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = brand.brandImage,
                    contentDescription = "Brand Image",
                    onError = { it ->
                        // it.result.throwable.printStackTrace()

                    },
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(Res.drawable.img_placeholder),
                    error = painterResource(Res.drawable.img_placeholder)


                )

            }
            Column(horizontalAlignment = Alignment.Start) {
                Text(brand.brandName.capitalize(Locale.current))
                Spacer(modifier = Modifier.height(4.dp))


            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StatusChip(brand.isAvailable)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Edit",
                modifier = Modifier.clickable {
                    onEdit(brand)
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }

    }
}


@Composable
fun WideBrandScreenUi(
    state: BrandSettingState,
    onAction: (BrandSettingActions) -> Unit,
    brandList: List<Brand>
) {
    Column(modifier = Modifier.fillMaxSize().padding(all = 16.dp)) {
        WidePageHeader(
            title = "Brand Management",
            subtitle = "Organize your brands for better management",
            onExport = { /*TODO*/ },
            onImport = { /*TODO*/ },
            onAdd = {
                onAction(BrandSettingActions.OnAddDialogClick(isAddDialogOpen = true))

            },
            addText = "Add Brand",
            icon = {
                Icon(imageVector = Icons.AutoMirrored.Outlined.BrandingWatermark, contentDescription = "Brand")
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchBar(searchText = state.searchQuery, onSearchTextChange = {
                onAction(BrandSettingActions.OnSearchQueryChange(it))
            }, modifier = Modifier.weight(5f))
            Spacer(modifier = Modifier.width(16.dp))
            MyFilterChip(
                modifier = Modifier.weight(1f),
                options = AvailabilityFilter.entries,
                selectedOption = state.statusFilter.name,
                onOptionSelected = {
                    onAction(
                        BrandSettingActions.OnStatusFilterChange(it)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        BrandTable(state = state, action = onAction, brandList)


    }


}

@Composable
fun BrandTable(state: BrandSettingState, action: (BrandSettingActions) -> Unit, x2: List<Brand>) {

    Column(
        modifier = Modifier.fillMaxWidth().clip(shape = MaterialTheme.shapes.medium)
            .background(color = androidx.compose.ui.graphics.Color.White)
    )
    {
        BrandListHeader()
        HorizontalDivider(thickness = 0.4.dp)
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
            {
                CircularProgressIndicator()
            }
        } else {
            BrandList(brandList = x2, onEdit = {
                action(BrandSettingActions.OnEditOptionClick(it))
            }, onPreview = {
                action(BrandSettingActions.OnImagePreviewClick(it, true))
            })
        }

    }
}

@Composable
fun BrandListHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            "Brand Name",
            modifier = Modifier.weight(3f),
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
fun BrandList(brandList: List<Brand>, onEdit: (Brand) -> Unit, onPreview: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        if (brandList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Brands Found")
                }
            }
        }

        itemsIndexed(brandList) { index, brand ->
            BrandListItem(brand = brand, onEdit, onPreview = onPreview)
            if (index < brandList.lastIndex) {
                HorizontalDivider(thickness = 0.5.dp)
            }

        }

    }
}

@Composable
fun BrandListItem(brand: Brand, onEdit: (Brand) -> Unit, onPreview: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(3f)
        ) {
            AsyncImage(
                model = brand.brandImage,
                modifier = Modifier.clip(MaterialTheme.shapes.medium).width(40.dp)
                    .height(40.dp).clickable() {
                        onPreview(brand.brandImage.toString())
                    },
                contentDescription = "Brand Image",
                onError = {
                    it.result.throwable.printStackTrace()
                },
                contentScale = ContentScale.FillBounds,
                error = painterResource(Res.drawable.img_placeholder),
                placeholder = painterResource(Res.drawable.img_placeholder)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = brand.brandName.capitalize(Locale.current),
                style = MaterialTheme.typography.bodyMedium
            )


        }


        Row(modifier = Modifier.weight(1f)) {
            StatusChip(brand.isAvailable)
        }
        Text(
            brand.createdAt.toFormattedString().toString(),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.weight(1f)) {
            IconButton(onClick = {
                onEdit(brand)
            }) {
                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
            }
        }
    }
}