package org.techcult.scaleos.feature.splash.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onLoggedIn: () -> Unit,
    onNotLoggedIn: () -> Unit,
) {
    var isChecking by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1500) // simulate API or datastore check
        val loggedIn = checkIfUserLoggedIn()
        if (loggedIn) onLoggedIn() else onNotLoggedIn()
        isChecking = false
    }

    if (isChecking) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

private fun checkIfUserLoggedIn(): Boolean {
    // replace with real authRepository check
    return false
}
