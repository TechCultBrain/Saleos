package org.techcult.scaleos.feature.settings.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.techcult.scaleos.feature.settings.presentation.navigation.SettingsNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootScreen() {

    val navController = rememberNavController()

        Scaffold(topBar = {
            TopAppBar(title = { Text("Settings") })
        }) {paddingValues ->
            SettingsNavGraph(navController,paddingValues)

        }}

