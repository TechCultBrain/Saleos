package org.techcult.scaleos.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.techcult.scaleos.feature.auth.presentation.navigation.authNavGraph
import org.techcult.scaleos.feature.dashboard.presentation.ui.root.DashboardRouteScreen
import org.techcult.scaleos.feature.settings.presentation.navigation.settingsNavGraph
import org.techcult.scaleos.feature.splash.presentation.SplashScreen

@Composable
fun MainNavHost(navController: NavHostController,onLoading:(Boolean)-> Unit) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Splash.route
    ) {


        // Main POS features
        composable(AppRoute.Splash.route) {
            SplashScreen(
                onLoggedIn = {
                    onLoading(true)
                    navController.navigate(AppRoute.Dashboard.route) {
                        popUpTo(AppRoute.Splash.route) { inclusive = true }
                    }
                },
                onNotLoggedIn = {
                    onLoading(true)
                    navController.navigate(AppRoute.Auth.route) {
                        popUpTo(AppRoute.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        // Auth
        authNavGraph(navController, onAuthSuccess = {
            navController.navigate(AppRoute.Dashboard.route)
        })
        composable(AppRoute.Dashboard.route) {
            DashboardRouteScreen(navController)

        }
        //salesNavGraph(navController)
        //reportsNavGraph(navController)

        // ⚙️ Settings
        settingsNavGraph(navController)
    }
}