package org.techcult.scaleos.feature.settings.presentation.ui.inventory

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.painterResource
import org.techcult.scaleos.core.presentation.components.MyDropDown
import org.techcult.scaleos.core.presentation.components.MyTextField
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsAction
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsState
import scaleos.composeapp.generated.resources.Res
import scaleos.composeapp.generated.resources.allDrawableResources


@Composable
fun CreateCategoryDialog(
    state: CategorySettingsState,
    onEvent: (CategorySettingsAction) -> Unit,
) {

    Dialog(onDismissRequest = { onEvent(CategorySettingsAction.OnDismissDialog) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                DialogHeader(state.isEditMode, onDismissRequest = {onEvent(CategorySettingsAction.OnDismissDialog) })
                Spacer(modifier = Modifier.height(24.dp))
                BasicInfoFields(state, onEvent)
                Spacer(modifier = Modifier.height(24.dp))
                VisualDesignFields(state, onEvent)
                Spacer(modifier = Modifier.height(24.dp))
                AvailabilityField(state.isAvailable, onChange = {onEvent(CategorySettingsAction.OnAvailabilityChange(it))})
                Spacer(modifier = Modifier.height(24.dp))
                DialogButtons(
                    onDismiss = { onEvent(CategorySettingsAction.OnDismissDialog) },
                    onCreate = { onEvent(CategorySettingsAction.CreateCategory) },
                    isEditMode = state.isEditMode
                )
            }
        }
    }
}

@Composable
 fun DialogHeader(isEditMode: Boolean, onDismissRequest: () -> Unit,title: String = "Category") {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(
                if (isEditMode) "Edit $title" else "Create New $title",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                if (isEditMode) "Edit $title details" else "Add a new $title",
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(onClick = onDismissRequest) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }
    }
}

@Composable
private fun BasicInfoFields(
    state: CategorySettingsState,
    onEvent: (CategorySettingsAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MyTextField(
            value = state.categoryName,
            onValueChange = { onEvent(CategorySettingsAction.OnCategoryNameChange(it)) },
            label = "Category Name *",
            placeholder = "Enter category name",
            leadingIcon = Icons.Default.Bookmark
        )
        MyTextField(
            value = state.description,
            onValueChange = { onEvent(CategorySettingsAction.OnDescriptionChange(it)) },
            label = "Description",
            placeholder = "Describe product category...",
            leadingIcon = Icons.Default.Description,
            singleLine = false
        )
        MyDropDown(
            label = "Parent Category",
            selectedValue = state.parentCategory,
            options = state.parentCategories,
            onValueChange = { id,name->
                onEvent(CategorySettingsAction.OnParentCategoryChange(id,name)) },

            leadingIcon = Icons.Default.Category
        )
    }
}

@Composable
private fun VisualDesignFields(
    state: CategorySettingsState,
    onEvent: (CategorySettingsAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Category Icon", style = MaterialTheme.typography.titleMedium)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(42.dp),
            modifier = Modifier.height(80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement =Arrangement.spacedBy(8.dp)
        ) {
            items(state.icons) { icon ->
                Box(modifier = Modifier
                    .size(42.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(0.4f))
                    .border(
                        width = if (state.selectedIcon == icon.name) 2.dp else 0.dp,
                        color = if (state.selectedIcon == icon.name) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable() {
                        onEvent(CategorySettingsAction.OnIconChange(icon.name))
                    }) {
                    Image(
                        painter = painterResource(Res.allDrawableResources[icon.name]!!),
                        null,
                        modifier = Modifier.size(36.dp),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                            Color.Black
                        )

                    )
                }
            }
        }
        Text("Category Color", style = MaterialTheme.typography.titleMedium)
        LazyVerticalGrid(columns = GridCells.Adaptive(40.dp), modifier = Modifier.height(75.dp)) {
            items(state.colors) { color ->
                Box(modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(color))
                    .border(
                        width = if (color == state.selectedColor) 2.dp else 0.dp,
                        color = if (color == state.selectedColor) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onEvent(CategorySettingsAction.OnColorChange(color)) })
            }
        }

    }

}

@Composable
fun AvailabilityField(state: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Available", style = MaterialTheme.typography.titleMedium)
            Text("Make this category available", style = MaterialTheme.typography.bodySmall)
        }
        Switch(
            checked = state,
            onCheckedChange = { onChange(it) })
    }
}


@Composable
 fun DialogButtons(onDismiss: () -> Unit, onCreate: () -> Unit, isEditMode: Boolean,title: String = "Category") {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        OutlinedButton(onClick = onDismiss) {
            Text("Cancel")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onCreate) {
            Text(if (isEditMode) "Update $title" else "Create $title")
        }
    }
}