@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
 fun MyTextDropDown(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector?=null,
    modifier: Modifier=Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier){
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        ExposedDropdownMenuBox(
            modifier = Modifier,
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            MyTextField(
                value = selectedValue.first().uppercase()+selectedValue.substring(1),
                onValueChange = {  },
                trailingIcon = Icons.Default.KeyboardArrowDown,
                leadingIcon = leadingIcon,
                modifier = Modifier.fillMaxWidth(),
                onTrailingIconClick = { expanded = true },
                readOnly = true

                )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Color.White
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.first().uppercase()+option.substring(1)) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        })
                }

            }


        }
    }
}
