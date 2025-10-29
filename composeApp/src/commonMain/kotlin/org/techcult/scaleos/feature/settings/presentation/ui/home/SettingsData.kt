package org.techcult.scaleos.feature.settings.presentation.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector
import org.techcult.scaleos.feature.settings.presentation.navigation.SettingsRoutes

data class SettingsCategory(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val items: List<SettingsItemData>
)

data class SettingsItemData(
    val title: String,
    val subtitle: String,
    val route: String,
    val tag: SettingsTag? = null
)

enum class SettingsTag(val text: String) {
    REQUIRED("Required"),
    NEW("New")
}

val settingsCategories = listOf(
    SettingsCategory(
        title = "General Settings",
        subtitle = "Basic store and system configuration",
        icon = Icons.Default.Store,
        items = listOf(
            SettingsItemData(
                title = "Store Information",
                subtitle = "Business details and contact information",
                route = SettingsRoutes.Store.route
            ),
            SettingsItemData(
                title = "Dashboard Settings",
                subtitle = "Customize your dashboard layout and widgets",
                route = ""
            ),
            SettingsItemData(
                title = "Language & Currency",
                subtitle = "Localization and regional settings",
                route = ""
            ),
            SettingsItemData(
                title = "Notifications",
                subtitle = "Alert preferences and notification settings",
                route = ""
            )
        )
    ),
    SettingsCategory(
        title = "User Management",
        subtitle = "Manage users, roles, and permissions",
        icon = Icons.Default.AccountCircle,
        items = listOf(
            SettingsItemData(
                title = "User Management",
                subtitle = "Create and manage user accounts",
                route = ""
            ),
            SettingsItemData(
                title = "Role & Permission Management",
                subtitle = "Configure user roles and permissions",
                route = ""
            )
        )
    ),
    SettingsCategory(
        title = "Staff Management",
        subtitle = "Employee management and administration",
        icon = Icons.Default.People,
        items = listOf(
            SettingsItemData(
                title = "Staff Management",
                subtitle = "Manage employee information and schedules",
                route = ""
            )
        )
    ),
    SettingsCategory(
        title = "Sales & POS",
        subtitle = "Point of sale and transaction settings",
        icon = Icons.Default.PointOfSale,
        items = listOf(
            SettingsItemData(
                title = "Tax Settings",
                subtitle = "Configure tax rates and calculations",
                route = SettingsRoutes.Tax.route,
                tag = SettingsTag.REQUIRED
            ),
            SettingsItemData(
                title = "Product Discounts",
                subtitle = "Set discounts that auto-apply during billing",
                route = SettingsRoutes.Discount.route,
                tag = SettingsTag.NEW
            )
        )
    ),
    SettingsCategory(
        title = "Receipt & Printing",
        subtitle = "Receipt templates and printer configuration",
        icon = Icons.Default.Receipt,
        items = listOf(
            SettingsItemData(
                title = "Receipt template",
                subtitle = "Customize receipt layout and branding",
                route = ""
            ),
            SettingsItemData(
                title = "Printer & Device Settings",
                subtitle = "Configure printers and hardware devices",
                route = ""
            )
        )
    ),
    SettingsCategory(
        title = "Inventory Management",
        subtitle = "Product and stock management settings",
        icon = Icons.Default.Inventory,
        items = listOf(
            SettingsItemData(
                title = "Product Management",
                subtitle = "Manage products, pricing, and specifications",
                route = ""
            ),
            SettingsItemData(
                title = "Suppliers",
                subtitle = "Manage supplier information and relationships",
                route = ""
            ),
            SettingsItemData(
                title = "Categories",
                subtitle = "Organize products into categories and subcategories",
                route = ""
            ),
            SettingsItemData(
                title = "Stock Levels",
                subtitle = "Monitor and manage inventory levels",
                route = ""
            ),
            SettingsItemData(
                title = "Stock Receipts",
                subtitle = "Track and manage stock receipts",
                route = ""
            ),
            SettingsItemData(
                title = "Units of Measure",
                subtitle = "Define and manage measurement units for products",
                route = ""
            ),
            SettingsItemData(
                title = "Batch & MRP Management",
                subtitle = "Manage product batches with different MRPs and expiry dates",
                route = "",
                tag = SettingsTag.NEW
            )
        )
    )
)
