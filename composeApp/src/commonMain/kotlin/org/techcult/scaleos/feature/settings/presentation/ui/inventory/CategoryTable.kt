package org.techcult.scaleos.feature.settings.presentation.ui.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Checkbox
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

data class CategoryItem(
    val id: Int,
    val name: String,
    val description: String,
    val parent: String,
    val products: Int,
    val status: String,
    val updated: String,
    val isTopLevel: Boolean = false
)

@Composable
fun CategoryTable(categories: List<CategoryItem>) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface).padding(16.dp)
    ) {
        CategoryTableHeader()
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { category ->
                CategoryTableRow(category)
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun CategoryTableHeader() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = false, onCheckedChange = {})
        Text("Category", modifier = Modifier.weight(3f), style = MaterialTheme.typography.titleMedium)
        Text("Parent", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text("Products", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text("Status", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text("Updated", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text("Actions", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)

    }
}

@Composable
fun CategoryTableRow(category: CategoryItem) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = false, onCheckedChange = {})
        Row(modifier = Modifier.weight(3f), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Folder,
                contentDescription = "Folder",
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer).padding(8.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(category.name, fontWeight = FontWeight.Medium)
                Text(category.description, style = MaterialTheme.typography.bodySmall)
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            ParentChip(category.parent, category.isTopLevel)
        }
        Text(category.products.toString(), modifier = Modifier.weight(1f))
        Box(modifier = Modifier.weight(1f)) {
            StatusChip(category.status)
        }
        Text(category.updated, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val color = if (status.equals("Active", true)) Color(0xFFE3F3E8) else Color(0xFFFDE8E8)
    val textColor = if (status.equals("Active", true)) Color(0xFF4CAF50) else Color(0xFFD32F2F)
    Text(
        text = status,
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
        text = parent,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        style = MaterialTheme.typography.bodySmall
    )
}