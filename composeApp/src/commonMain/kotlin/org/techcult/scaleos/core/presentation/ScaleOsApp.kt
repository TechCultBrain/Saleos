package org.techcult.scaleos.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.techcult.scaleos.core.presentation.navigation.MainNavHost
import org.techcult.scaleos.core.presentation.theme.AppTheme

@Composable
@Preview
fun ScaleOsApp(onLoading: (Boolean) -> Unit={}) {
    val navController = rememberNavController()
    AppTheme {
        MainNavHost(navController = navController, onLoading =onLoading )
    }
}

