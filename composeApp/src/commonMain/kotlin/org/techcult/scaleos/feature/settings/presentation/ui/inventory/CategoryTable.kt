package org.techcult.scaleos.feature.settings.presentation.ui.inventory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.painterResource
import org.techcult.scaleos.feature.product.domain.model.Category
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsAction
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsState
import scaleos.composeapp.generated.resources.Res
import scaleos.composeapp.generated.resources.allDrawableResources


@Composable
fun CategoryTable(
    state: CategorySettingsState,
    action: (CategorySettingsAction) -> Unit,
    categories: List<Category>
) {
    Column(
        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        CategoryTableHeader()
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
        if (state.isLoading) {

            Box(
                modifier = Modifier.fillMaxWidth().fillMaxSize(0.5f).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }


        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (categories.isEmpty()) {
                    item()
                    {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No categories found")
                        }

                    }
                }

                itemsIndexed(categories) { index, category ->
                    CategoryTableRow(
                        category,
                        onEdit = {
                            action(CategorySettingsAction.OnEditCategoryClick(it))
                        },
                        onDelete = { action(CategorySettingsAction.OnDeleteCategoryClick(category)) })
                    if (index < categories.size - 1) {
                        HorizontalDivider(thickness = 0.5.dp)

                    }

                }
            }
            }
        }
    }


@Composable
fun CategoryTableHeader() {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
       // Checkbox(checked = false, onCheckedChange = {})
        Spacer(modifier = Modifier.width(16.dp))
        Text("Category", modifier = Modifier.weight(3f), style = MaterialTheme.typography.titleMedium)
        Text("Parent", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text("Products", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text("Status", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text("Updated", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text("Actions", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)

    }
}

@Composable
fun CategoryTableRow(
    category: Category,
    onEdit: (Category) -> Unit,
    onDelete: (Category) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Checkbox(checked = false, onCheckedChange = {})
        // bytes = Res.readBytes("files/example.png")
        Spacer(modifier = Modifier.width(16.dp))

        Row(modifier = Modifier.weight(3f), verticalAlignment = Alignment.CenterVertically) {
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
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    category.categoryName.first().uppercase() + category.categoryName.substring(1),
                    fontWeight = FontWeight.Medium
                )
                category.description?.let {
                    Text(
                        it.first().uppercase() + it.substring(1),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            ParentChip(
                if (category.parentName.isNullOrEmpty()) "No Parent" else (category.parentName),
                category.parentId == null
            )
        }
        Text(category.productCount.toString(), modifier = Modifier.weight(1f))
        Box(modifier = Modifier.weight(1f)) {
            StatusChip(category.isAvailable)
        }
        val customFormat = LocalDateTime.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); day(); chars(", "); year()
        }
        Text(
            category.updatedAt!!.format(customFormat),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = { onEdit(category) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { onDelete(category) }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}

@Composable
fun StatusChip(status: Boolean) {
    val color = if (status) Color(0xFFE3F3E8) else Color(0xFFFDE8E8)
    val textColor = if (status) Color(0xFF4CAF50) else Color(0xFFD32F2F)
    Text(
        text = if (status) "Available" else "Unavailable",
        color = textColor,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun ParentChip(parent: String, isTopLevel: Boolean) {
    val color =
        if (isTopLevel) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.secondaryContainer.copy(
            alpha = 0.5f
        )
    Text(
        text = parent.first().uppercase() + parent.substring(1),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        style = MaterialTheme.typography.bodySmall
    )
}