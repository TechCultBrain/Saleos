package org.techcult.scaleos.core.presentation.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    prefix: String? = null,
    suffix: String? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    isError: Boolean = false,
    supportingText: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Password
    ),
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None

) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused = interactionSource.collectIsFocusedAsState()

    Column(modifier = modifier.padding()) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            readOnly = readOnly,
            modifier = Modifier.padding(

            ).height(45.dp), // Padding for the BasicTextField content

            value = value,
            onValueChange = { it ->
                onValueChange(it)
            },
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            maxLines = maxLines,

            decorationBox = { innerTextField ->
                Surface(
                    modifier = Modifier.height(48.dp).fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(
                        width = if (focused.value) {
                            2.dp
                        } else {
                           1.dp
                        },
                        color = if (focused.value) {
                            MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(0.2f)
                        }
                    ),
                    color = Color.White

                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxSize()

                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (leadingIcon != null) {
                                Icon(
                                    imageVector = leadingIcon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Surface(
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                                color = Color.Transparent
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                                    contentAlignment = Alignment.CenterStart
                                ) {

                                    innerTextField()

                                    if (value.isEmpty() && placeholder != null) {

                                        Text(
                                            text = placeholder,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f)
                                        )


                                    }

                                }

                            }
                            // innerTextField()
                            if (trailingIcon != null) {
                                IconButton(onClick = onTrailingIconClick) {
                                    Icon(
                                        imageVector = trailingIcon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            }

                        }
                    }
                }
            },
            visualTransformation = visualTransformation,
            textStyle = TextStyle.Default.copy(
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

        )
        Spacer(modifier = Modifier.height(4.dp))
        if (isError) {
            Text(
                text = supportingText.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error
            )
        } else if (supportingText != null) {
            Text(
                text = supportingText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f)
            )
        }

    }

}

@Preview()
@Composable
fun MyTextPreview() {
    Surface {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            MyTextField(value = "Hello", onValueChange = {})
        }
    }
}