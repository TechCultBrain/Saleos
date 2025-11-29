package org.techcult.scaleos.feature.settings.presentation.ui.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.BrandingWatermark
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WidePageHeader(
    title: String,
    subtitle: String,
    onExport: (() -> Unit)? = null,
    onImport: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    addText: String = "Add"
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.BrandingWatermark,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            onExport?.let {
                OutlinedButton(
                    onClick = it,
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(
                        0.7.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = "Export")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            onImport?.let {
                OutlinedButton(onClick = it, shape = MaterialTheme.shapes.medium, border = BorderStroke(
                    0.7.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )) {
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



