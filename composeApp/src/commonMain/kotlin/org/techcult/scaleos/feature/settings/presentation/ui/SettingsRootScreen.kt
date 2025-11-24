package org.techcult.scaleos.feature.settings.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import org.techcult.scaleos.feature.settings.presentation.navigation.SettingsNavGraph

@Suppress("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootScreen() {

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Settings")
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))


        }) {paddingValues ->
            SettingsNavGraph(navController,paddingValues)

        }}

