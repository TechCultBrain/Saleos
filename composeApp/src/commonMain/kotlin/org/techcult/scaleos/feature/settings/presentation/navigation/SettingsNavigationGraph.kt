package org.techcult.scaleos.feature.settings.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.techcult.scaleos.core.presentation.navigation.AppRoute
import org.techcult.scaleos.feature.settings.presentation.ui.Purchase.PurchaseSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.general.AboutSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.general.AppearanceSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.general.DashboardSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.general.LocalizationSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.general.NotificationSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.general.ReceiptSettingScreen
import org.techcult.scaleos.feature.settings.presentation.ui.general.StoreSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.home.SettingsHomeScreen
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.BatchSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.CategorySettingScreen
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.DepartmentSettingScreen
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.InventorySettingScreen
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.ProductSettingScreen
import org.techcult.scaleos.feature.settings.presentation.ui.inventory.UnitSettingScreen
import org.techcult.scaleos.feature.settings.presentation.ui.pricing.DiscountSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.pricing.TaxSettingScreen
import org.techcult.scaleos.feature.settings.presentation.ui.supplier.SupplierSettingScreen
import org.techcult.scaleos.feature.settings.presentation.ui.system.DbManagementScreen
import org.techcult.scaleos.feature.settings.presentation.ui.system.SyncSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.user.RolePermissionSettingsScreen
import org.techcult.scaleos.feature.settings.presentation.ui.user.StaffManagementScreen
import org.techcult.scaleos.feature.settings.presentation.ui.user.UserManagementScreen


@Composable
fun SettingsNavGraph(navController: NavHostController,paddingValues: PaddingValues) {
    NavHost(
        startDestination = SettingsRoutes.Home.route,
        navController = navController,
        modifier = androidx.compose.ui.Modifier.padding(top = paddingValues.calculateTopPadding())
    ) {

        // --- Home ---
        composable(SettingsRoutes.Home.route) {
            SettingsHomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        // --- General ---
        composable(SettingsRoutes.Store.route) { StoreSettingsScreen() }
        composable(SettingsRoutes.Dashboard.route) { DashboardSettingsScreen() }
        composable(SettingsRoutes.Notifications.route) { NotificationSettingsScreen() }
        composable(SettingsRoutes.Receipt.route) { ReceiptSettingScreen() }
        composable(SettingsRoutes.Database.route) { DbManagementScreen() }
        composable(SettingsRoutes.Sync.route) { SyncSettingsScreen() }

        // --- Users & Access ---
        composable(SettingsRoutes.UserManagement.route) { UserManagementScreen() }
        composable(SettingsRoutes.StaffManagement.route) { StaffManagementScreen() }
        composable(SettingsRoutes.RolePermission.route) { RolePermissionSettingsScreen() }

        // --- Pricing ---
        composable(SettingsRoutes.Tax.route) { TaxSettingScreen() }
        composable(SettingsRoutes.Discount.route) { DiscountSettingsScreen() }

        // --- Products & Inventory ---
        composable(SettingsRoutes.Product.route) { ProductSettingScreen() }
        composable(SettingsRoutes.Category.route) { CategorySettingScreen(
            onBack = {
                navController.navigateUp()
            }
        ) }
        composable(SettingsRoutes.Department.route) { DepartmentSettingScreen() }
        composable(SettingsRoutes.Supplier.route) { SupplierSettingScreen() }
        composable(SettingsRoutes.Inventory.route) { InventorySettingScreen() }
        composable(SettingsRoutes.Unit.route) { UnitSettingScreen() }
        composable(SettingsRoutes.Batch.route) { BatchSettingsScreen() }
        composable(SettingsRoutes.PurchaseSettings.route) { PurchaseSettingsScreen() }


        // --- System ---
        composable(SettingsRoutes.Appearance.route) { AppearanceSettingsScreen() }
        composable(SettingsRoutes.Localization.route) { LocalizationSettingsScreen() }

        // --- About ---
        composable(SettingsRoutes.About.route) { AboutSettingsScreen() }
    }
}
