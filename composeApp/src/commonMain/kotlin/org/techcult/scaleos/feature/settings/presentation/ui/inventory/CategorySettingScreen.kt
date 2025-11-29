package org.techcult.scaleos.feature.settings.presentation.ui.inventory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.FilterList
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.techcult.scaleos.core.presentation.components.ConfirmationDialog
import org.techcult.scaleos.core.presentation.components.MyFilterChip
import org.techcult.scaleos.core.presentation.components.SearchBar
import org.techcult.scaleos.core.utils.DeviceConfiguration
import org.techcult.scaleos.core.utils.ObserveAsEvents
import org.techcult.scaleos.feature.product.domain.model.Category
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.CompactPageHeader
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.WidePageHeader
import org.techcult.scaleos.feature.settings.presentation.viewmodel.AvailabilityFilter
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategoryEvents
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsAction
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsState
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsViewModel
import scaleos.composeapp.generated.resources.Res
import scaleos.composeapp.generated.resources.allDrawableResources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySettingScreen(
    onBack: () -> Unit,
    viewModel: CategorySettingsViewModel = koinViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    if (state.showDialog) {
        CreateCategoryDialog(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
    if (state.showDeleteDialog) {
        ConfirmationDialog(
            title = "Delete Category",
            subtitle = "Are you sure you want to delete this category?",
            onConfirm = { viewModel.onEvent(CategorySettingsAction.DeleteCategory) },
            onDismiss = { viewModel.onEvent(CategorySettingsAction.DismissDeleteDialog) },
            isDestructive = true
        )
    }
    if (state.isBottomSheetOpen)
    {
        ModalBottomSheet(onDismissRequest = {
            viewModel.onEvent(CategorySettingsAction.OnFilterClick(isBottomSheetOpen = false))

        },sheetState = sheetState)
        {
            AvailabilityFilter.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        viewModel.setFilter(option)
                        viewModel.onEvent(CategorySettingsAction.OnFilterClick(isBottomSheetOpen = false))

                    })
            }


        }
    }

    ObserveAsEvents(viewModel.event)
    {
        when (it) {
            is CategoryEvents.OnError -> {
                scope.launch {
                    snackBarHostState.showSnackbar("Category Failed")
                }
            }

            is CategoryEvents.OnSuccess -> {
                scope.launch {

                    snackBarHostState.showSnackbar(it.message)

                }
            }
        }

    }



    Scaffold(
        snackbarHost = {
            androidx.compose.material3.SnackbarHost(hostState = snackBarHostState)
        },
        containerColor = Color(0xFFF9FAFB)
    ) { paddingValues ->

        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT -> {
                CompactScreenUi(
                    paddingValues = paddingValues,
                    state = state,
                    onAction = { event ->
                        when (event) {
                            CategorySettingsAction.OnBackClick -> {
                                onBack()

                            }

                            else -> viewModel.onEvent(event)


                        }
                    },
                    categories = categories
                )


            }

            DeviceConfiguration.MOBILE_LANDSCAPE -> {
                CompactScreenUi(
                    paddingValues = paddingValues,
                    state = state,
                    onAction = viewModel::onEvent,
                    categories = categories
                )


            }

            DeviceConfiguration.TABLET_PORTRAIT -> {
                WideScreenUi(
                    state = state,
                    onAction = viewModel::onEvent,
                    categories = categories
                )


            }

            DeviceConfiguration.TABLET_LANDSCAPE -> {
                WideScreenUi(
                    categories = categories,
                    state = state,
                    onAction = viewModel::onEvent
                )

            }

            DeviceConfiguration.DESKTOP -> {
                WideScreenUi(
                    state = state,
                    onAction = viewModel::onEvent, categories = categories
                )

            }
        }
    }
}

@Composable
fun WideScreenUi(
    categories: List<Category>,
    onAction: (CategorySettingsAction) -> Unit,
    state: CategorySettingsState
) {

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        WidePageHeader(
            title = "Category Management",
            subtitle = "Organize your products into categories for better management",
            onExport = { /*TODO*/ },
            onImport = { /*TODO*/ },
            onAdd = {
                onAction(CategorySettingsAction.OnAddCategoryClick)
            },
            addText = "Add Category"
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            SearchBar(
                searchText = state.searchText,
                onSearchTextChange = {
                    onAction(CategorySettingsAction.OnSearchTextChange(it))
                },
                placeholder = "Search categories by name or description...",
                modifier = Modifier.weight(5f)

            )
            Spacer(modifier = Modifier.width(16.dp))
            MyFilterChip(
                modifier = Modifier.weight(1f),
                options = AvailabilityFilter.entries,
                selectedOption = state.statusFilter,
                onOptionSelected = {
                    onAction(
                        CategorySettingsAction.OnStatusFilterChange(it)
                    )
                }
            )

        }
        Spacer(modifier = Modifier.height(24.dp))


        CategoryTable(state = state, action = onAction, categories)

    }

}


@Composable
fun CompactScreenUi(
    paddingValues: PaddingValues,
    onAction: (CategorySettingsAction) -> Unit,
    state: CategorySettingsState,
    categories: List<Category>
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        CompactPageHeader(
            title = "Category",
            subtitle = "Organize your products",
            onExport = { /*TODO*/ },
            onImport = { /*TODO*/ },
            onAdd = {
                onAction(CategorySettingsAction.OnAddCategoryClick)
            }, onBack = {
                onAction(CategorySettingsAction.OnBackClick)
            }


        )
        Row(modifier = Modifier.fillMaxWidth()) {
            SearchBar(
                searchText = state.searchText,
                onSearchTextChange = {
                    onAction(CategorySettingsAction.OnSearchTextChange(it))

                },
                modifier = Modifier.weight(3f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                onAction(CategorySettingsAction.OnFilterClick(isBottomSheetOpen = true))

            }){
                Icon(imageVector = Icons.Outlined.FilterList, contentDescription = "Edit")
            }


        }
        CategoryList(categories = categories, onAction = onAction,state)


    }

}


@Composable
fun CategoryList(categories: List<Category>, onAction: (CategorySettingsAction) -> Unit,state: CategorySettingsState) {
    if (state.isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            CircularProgressIndicator()
        }

    }else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (categories.isEmpty()) {
                item {
                    Text(text = "No Categories Found", modifier = Modifier.fillMaxWidth())
                }
            }
            itemsIndexed(categories) { index, category ->
                CategoryListItem(category = category, onAction = onAction)
                if (index < categories.lastIndex)
                    HorizontalDivider(thickness = 0.5.dp)


            }
        }
    }

}

@Composable
fun CategoryListItem(
    category: Category,
    onAction: (CategorySettingsAction) -> Unit
) {
    Row(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable() {
            onAction(CategorySettingsAction.OnEditCategoryClick(category))
        }.background(Color.White)
            .fillMaxWidth().padding(8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.allDrawableResources[category.imageName]!!),
            null,
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                .background(Color(category.colorCode!!).copy(0.2f)),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                Color(
                    category.colorCode
                )
            )
        )
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.Start,
            modifier = Modifier.padding(start = 16.dp).weight(1f)
        ) {

            Text(
                text = category.categoryName,
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            ParentChip(
                if (category.parentName.isNullOrEmpty()) "Parent" else (category.parentName),
                category.parentId == null
            )
        }
        StatusChip(status = category.isAvailable)
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            IconButton(onClick = { onAction(CategorySettingsAction.OnEditCategoryClick(category)) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            /*IconButton(onClick = { onAction(CategorySettingsAction.OnDeleteCategoryClick(category)) }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = Color.Red)
            }*/
        }


    }


}
