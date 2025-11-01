package org.techcult.scaleos

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.techcult.scaleos.core.di.initKoin
import org.techcult.scaleos.core.presentation.ScaleOsApp

fun main() = application {
    initKoin {  }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Scaleos",
    ) {
        ScaleOsApp()
    }
}