package org.techcult.scaleos.feature.auth.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    Scaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)) {

        Column {
            Text("LoginScreen", modifier = Modifier.clickable {
                onLoginSuccess()
            })

            Text("Register", modifier = Modifier.clickable {
                onRegister()
            })
            Text("Forgot Password", modifier = Modifier.clickable {
                onForgotPassword()
            })

        }
    }

}