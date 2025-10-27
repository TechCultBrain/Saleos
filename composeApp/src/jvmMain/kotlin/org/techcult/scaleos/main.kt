package org.techcult.scaleos

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.techcult.scaleos.core.presentation.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Scaleos",
    ) {
        App()
    }
}