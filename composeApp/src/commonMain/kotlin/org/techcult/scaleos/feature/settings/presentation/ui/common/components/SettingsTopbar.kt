package org.techcult.scaleos.feature.settings.presentation.ui.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppbar(
    modifier: Modifier = Modifier,
    title:String="",
    navigationIcon: @Composable () -> Unit={},
    actions: @Composable () -> Unit={},
    scrollBehavior: TopAppBarScrollBehavior?=null,
    colors: TopAppBarColors=TopAppBarDefaults.topAppBarColors(),
    isBackNavigation: Boolean = true,
    onBack: () -> Unit = {}
)
{
    TopAppBar(
        title = {
            Text(text = title,style = MaterialTheme.typography.titleMedium)
        },
        navigationIcon = {
            if (isBackNavigation){
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = null
                    )
                }
            }else {
                navigationIcon()
            }
        },
        actions = {
            actions()
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors()
    )
}