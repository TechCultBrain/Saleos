package org.techcult.scaleos.feature.settings.presentation.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageHeader(
    title: String,
    subtitle: String,
    onExport: (() -> Unit)? = null,
    onImport: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    addText: String = "Add"
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Category, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.titleLarge)
            }
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            onExport?.let {
                OutlinedButton(onClick = it, shape = MaterialTheme.shapes.medium) {
                    Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = "Export")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            onImport?.let {
                OutlinedButton(onClick = it, shape = MaterialTheme.shapes.medium) {
                    Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "Import")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Import")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            onAdd?.let {
                Button(onClick = it, shape = MaterialTheme.shapes.medium) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(addText)
                }
            }
        }
    }
}



