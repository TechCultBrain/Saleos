package org.techcult.scaleos.feature.settings.presentation.navigation

import kotlinx.serialization.Serializable
@Serializable
sealed class SettingsRoutes(val route: String) {
    @Serializable
    object Home : SettingsRoutes("settings/home")

    // General
    object Store : SettingsRoutes("settings/store")
    object Dashboard : SettingsRoutes("settings/dashboard")
    object Notifications : SettingsRoutes("settings/notifications")
    object Receipt : SettingsRoutes("settings/receipt")
    object Database : SettingsRoutes("settings/database")
    object Sync : SettingsRoutes("settings/sync")
    object Printer : SettingsRoutes("settings/printer")


    // Users & Access
    object UserManagement : SettingsRoutes("settings/user_management")
    object StaffManagement : SettingsRoutes("settings/staff_management")
    object RolePermission : SettingsRoutes("settings/role_permission")

    // Pricing
    object Tax : SettingsRoutes("settings/tax")
    object Discount : SettingsRoutes("settings/discount")

    // Products & Inventory
    object Product : SettingsRoutes("settings/product")
    object Category : SettingsRoutes("settings/category")
    object Department : SettingsRoutes("settings/department")
    object Supplier : SettingsRoutes("settings/supplier")
    object Inventory : SettingsRoutes("settings/inventory")
    object Unit : SettingsRoutes("settings/unit")
    object Batch : SettingsRoutes("settings/batch")

    object Brand: SettingsRoutes("settings/brand")

    // System
    object Appearance : SettingsRoutes("settings/appearance")
    object Localization : SettingsRoutes("settings/localization")

    // About
    object About : SettingsRoutes("settings/about")
    object PurchaseSettings : SettingsRoutes("settings/purchase_settings")
}
