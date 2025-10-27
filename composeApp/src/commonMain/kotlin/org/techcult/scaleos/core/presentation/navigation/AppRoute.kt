package org.techcult.scaleos.core.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute(val route: String) {

    @Serializable
    object Splash : AppRoute("splash")
    @Serializable
    object Auth : AppRoute("auth")
    @Serializable
    object Dashboard : AppRoute("dashboard")
    @Serializable
    object Home : AppRoute("home")
}