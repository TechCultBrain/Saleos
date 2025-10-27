package org.techcult.scaleos.feature.auth.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoute(val route: String) {

    @Serializable
    data object Login : AuthRoute("login")
    @Serializable
    data object Register : AuthRoute("register")
    @Serializable
    data object OTPVerification : AuthRoute("otp_verification")
    @Serializable
    data object ForgotPassword : AuthRoute("forgot_password")
    @Serializable
    data object ResetPassword : AuthRoute("reset_password")
}