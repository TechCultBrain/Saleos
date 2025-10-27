package org.techcult.scaleos.feature.settings.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsHomeScreen(onNavigate: (String) -> Unit) {

    Text(text = "Settings Home Screen", modifier = Modifier.clickable {
        onNavigate("settings_graph")
    })

}

