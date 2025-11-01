package org.techcult.scaleos.feature.settings.presentation.ui.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.ktor.client.request.invoke
import org.techcult.scaleos.core.presentation.components.MyDropDown
import org.techcult.scaleos.core.presentation.components.MyTextField
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsEvent
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsState
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategoryTab


@Composable
fun CreateCategoryDialog(
    state: CategorySettingsState,
    onEvent: (CategorySettingsEvent) -> Unit,
) {

    Dialog(onDismissRequest = { onEvent(CategorySettingsEvent.OnDismissDialog) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                DialogHeader { onEvent(CategorySettingsEvent.OnDismissDialog) }
                Stepper(state.currentStep)
                Spacer(modifier = Modifier.height(24.dp))
                CategoryTabs(state.selectedTab) { onEvent(CategorySettingsEvent.OnTabChange(it)) }
                Spacer(modifier = Modifier.height(24.dp))
                when (state.selectedTab) {
                    CategoryTab.BasicInfo -> BasicInfoTab(state, onEvent)
                    CategoryTab.VisualDesign -> VisualDesignTab(state, onEvent)
                    CategoryTab.Settings -> { /* TODO */
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                DialogButtons(onDismiss = { onEvent(CategorySettingsEvent.OnDismissDialog) }, onCreate = { onEvent(CategorySettingsEvent.CreateCategory) })
            }
        }
    }
}

@Composable
private fun DialogHeader(onDismissRequest: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("Create New Category", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Add a new category to organize your products.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        IconButton(onClick = onDismissRequest) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }
    }
}

@Composable
private fun Stepper(currentStep: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        (1..3).forEach { step ->
            Step(number = step, isActive = step == currentStep)
            if (step < 3) {
                HorizontalDivider(
                    modifier = Modifier.width(40.dp),
                    color = if (step < currentStep) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.12f
                    )
                )
            }
        }
    }
}

@Composable
private fun Step(number: Int, isActive: Boolean) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CategoryTabs(selectedTab: CategoryTab, onTabSelected: (CategoryTab) -> Unit) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
    ) {
        Tab(
            selected = selectedTab == CategoryTab.BasicInfo,
            onClick = { onTabSelected(CategoryTab.BasicInfo) },
            text = { Text("Basic Info") },
            icon = { Icon(Icons.Default.TextFields, contentDescription = null) }
        )
        Tab(
            selected = selectedTab == CategoryTab.VisualDesign,
            onClick = { onTabSelected(CategoryTab.VisualDesign) },
            text = { Text("Visual Design") },
            icon = { Icon(Icons.Default.Palette, contentDescription = null) }
        )
        Tab(
            selected = selectedTab == CategoryTab.Settings,
            onClick = { onTabSelected(CategoryTab.Settings) },
            text = { Text("Settings") },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) }
        )
    }
}

@Composable
private fun BasicInfoTab(state: CategorySettingsState, onEvent: (CategorySettingsEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MyTextField(
            value = state.categoryName,
            onValueChange = { onEvent(CategorySettingsEvent.OnCategoryNameChange(it.text)) },
            label = "Category Name *",
            placeholder = "Enter category name (e.g., Beverages, Electronics)",
            leadingIcon = Icons.Default.Bookmark
        )
        MyTextField(
            value = state.description,
            onValueChange = { onEvent(CategorySettingsEvent.OnDescriptionChange(it.text)) },
            label = "Description",
            placeholder = "Describe what products belong to this category...",
            leadingIcon = Icons.Default.Description,
            singleLine = false
        )
        MyDropDown(
            label = "Parent Category",
            selectedValue = state.parentCategory,
            options = listOf("None (Top Level Category)", "Food", "Electronics"),
            onValueChange = { onEvent(CategorySettingsEvent.OnParentCategoryChange(it)) },
            leadingIcon = Icons.Default.Category
        )
    }
}

@Composable
private fun VisualDesignTab(state: CategorySettingsState, onEvent: (CategorySettingsEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Category Icon", style = MaterialTheme.typography.titleMedium)
        LazyVerticalGrid(columns = GridCells.Adaptive(40.dp)) {
            items(state.icons) { icon ->
                Icon(
                    imageVector = icon, contentDescription = null, modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .border(
                            width = if (icon == state.selectedIcon) 2.dp else 1.dp,
                            color = if (icon == state.selectedIcon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onEvent(CategorySettingsEvent.OnIconChange(icon)) }
                )
            }
        }
        Text("Category Color", style = MaterialTheme.typography.titleMedium)
        LazyVerticalGrid(columns = GridCells.Adaptive(40.dp)) {
            items(state.colors) { color ->
                Box(modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color)
                    .border(
                        width = if (color == state.selectedColor) 2.dp else 0.dp,
                        color = if (color == state.selectedColor) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onEvent(CategorySettingsEvent.OnColorChange(color)) })
            }
        }

    }

}



@Composable
private fun DialogButtons(onDismiss: () -> Unit, onCreate: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        OutlinedButton(onClick = onDismiss) {
            Text("Cancel")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onCreate) {
            Text("Create Category")
        }
    }
}
