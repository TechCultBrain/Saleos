package org.techcult.scaleos.feature.settings.presentation.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CompactPageHeader(
    title: String,
    subtitle: String? = null,
    onExport: (() -> Unit)? = null,
    onImport: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    addText: String = "Add",
    onBack: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = "Category",
            modifier = Modifier.clickable() {
                onBack()
            })
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        // Text(text = subtitle ?: "", style = MaterialTheme.typography.bodySmall)


        onAdd?.let {
            Row(modifier = Modifier.padding(8.dp).clip(MaterialTheme.shapes.small).clickable {
                onAdd()
            }, verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    addText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


