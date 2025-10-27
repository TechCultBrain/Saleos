package org.techcult.scaleos.core.navigation

sealed class NavRoutes (val route: String) {
    // Auth
    object Login : NavRoutes("login")
    object Signup : NavRoutes("signup")
    object ForgotPassword : NavRoutes("forgot_password")

    // Main
    object Dashboard : NavRoutes("dashboard")
    object Billing : NavRoutes("billing")
    object Product : NavRoutes("product")
    object Purchase : NavRoutes("purchase")
    object Customer : NavRoutes("customer")
    object Supplier : NavRoutes("supplier")
    object Inventory : NavRoutes("inventory")
    object Reports : NavRoutes("reports")
    object Settings : NavRoutes("settings")

    // Settings Sub-Routes
    object GeneralSettings : NavRoutes("settings/general")
    object TaxSettings : NavRoutes("settings/tax")
    object DiscountSettings : NavRoutes("settings/discount")
    object StoreSettings : NavRoutes("settings/store")
    object UserRoleSettings : NavRoutes("settings/user_roles")
}