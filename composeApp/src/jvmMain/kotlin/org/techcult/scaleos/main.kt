package org.techcult.scaleos

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.techcult.scaleos.core.di.initScaleOsKoin
import org.techcult.scaleos.core.presentation.ScaleOsApp

fun main() = application {
    initScaleOsKoin {

    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Scaleos",

    ) {
        ScaleOsApp()
    }
}