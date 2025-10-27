package org.techcult.scaleos.feature.auth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import androidx.navigation.toRoute
import org.techcult.scaleos.core.presentation.navigation.AppRoute
import org.techcult.scaleos.feature.auth.presentation.ui.ForgotPasswordScreen
import org.techcult.scaleos.feature.auth.presentation.ui.LoginScreen
import org.techcult.scaleos.feature.auth.presentation.ui.OTPVerificationScreen
import org.techcult.scaleos.feature.auth.presentation.ui.RegisterScreen
import org.techcult.scaleos.feature.auth.presentation.ui.ResetPasswordScreen


fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onAuthSuccess: () -> Unit, // Callback to go to DashboardGraph
) {
    navigation(
        startDestination = AuthRoute.Login.route,
        route = AppRoute.Auth.route,


    ) {

        composable(AuthRoute.Login.route) {
            LoginScreen(
                onLoginSuccess = onAuthSuccess,
                onRegister = { navController.navigate(AuthRoute.Register) },
                onForgotPassword = { navController.navigate(AuthRoute.ForgotPassword) }
            )
        }

        composable<AuthRoute.Register> {
            RegisterScreen(
              /*  onOtpSent = { email ->
                    navController.navigate(AuthRoute.OTPVerification(email))
                },
                onBack = { navController.popBackStack() }*/
            )
        }

        composable(AuthRoute.OTPVerification.route) { backStackEntry ->
            val route = backStackEntry.toRoute<AuthRoute.OTPVerification>()
            OTPVerificationScreen(
              /*  email = route.email,
                onOtpVerified = onAuthSuccess,
                onBack = { navController.popBackStack() }*/
            )
        }

        composable(AuthRoute.ForgotPassword.route) {
            ForgotPasswordScreen(
               /* onResetSent = { email ->
                    navController.navigate(AuthRoute.ResetPassword(email))
                },
                onBack = { navController.popBackStack() }*/
            )
        }

        composable(AuthRoute.ResetPassword.route) { backStackEntry ->
            val route = backStackEntry.toRoute<AuthRoute.ResetPassword>()
            ResetPasswordScreen(
              /*  email = route.email,
                onResetSuccess = {
                    navController.navigate(AuthRoute.Login) {
                        popUpTo(AuthRoute.Login.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }*/
            )
        }
    }
}
