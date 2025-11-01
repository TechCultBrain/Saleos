package org.techcult.scaleos.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
 fun MyDropDown(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Box {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(leadingIcon, contentDescription = null) },
                trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null) })

            Box(modifier = Modifier.matchParentSize().clickable { expanded = true })

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        onValueChange(it)
                        expanded = false
                    })
                }
            }
        }
    }
}