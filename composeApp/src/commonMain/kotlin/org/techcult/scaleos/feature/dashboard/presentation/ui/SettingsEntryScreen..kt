package org.techcult.scaleos.feature.dashboard.presentation.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.techcult.scaleos.feature.settings.presentation.navigation.SettingsRoutes

@Composable
fun SettingsEntryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Button(modifier = modifier, onClick = {
        navController.navigate(SettingsRoutes.Home.route)
    }){
        Text(text = "Settings")
    }
}