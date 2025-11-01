package org.techcult.scaleos.feature.settings.presentation.ui.inventory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.techcult.scaleos.core.presentation.components.MyFilterChip
import org.techcult.scaleos.core.presentation.components.SearchBar
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.PageHeader
import org.techcult.scaleos.feature.settings.presentation.ui.common.components.SettingsTopAppbar
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsEvent
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySettingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: CategorySettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.showDialog) {
        CreateCategoryDialog(
            state = state,
            onEvent = viewModel::onEvent
        )
    }

    Scaffold(topBar = {
        SettingsTopAppbar(title = "Category Settings", isBackNavigation = true, onBack = onBack)

    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            PageHeader(
                title = "Category Management",
                subtitle = "Organize your products into categories for better management",
                onExport = { /*TODO*/ },
                onImport = { /*TODO*/ },
                onAdd = { viewModel.onEvent(CategorySettingsEvent.OnAddCategoryClick) },
                addText = "Add Category"
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                SearchBar(
                    searchText = state.searchText,
                    onSearchTextChange = { viewModel.onEvent(CategorySettingsEvent.OnSearchTextChange(it)) },
                    placeholder = "Search categories by name or description...",
                    modifier = Modifier.padding(horizontal = 16.dp).weight(5f)

                )
                MyFilterChip(
                    modifier = Modifier.weight(1f),
                    options = listOf("All Status", "Active", "Inactive"),
                    selectedOption = state.statusFilter,
                    onOptionSelected = { viewModel.onEvent(CategorySettingsEvent.OnStatusFilterChange(it)) }
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            CategoryTable(categories = state.categories)
        }
    }
}
