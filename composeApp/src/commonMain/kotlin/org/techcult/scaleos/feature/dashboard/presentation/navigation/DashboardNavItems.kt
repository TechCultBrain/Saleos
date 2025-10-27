package org.techcult.scaleos.feature.dashboard.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import org.techcult.scaleos.feature.dashboard.DashboardRoute

val DashboardNavItems = listOf(
    DashboardNavItem(
        route = DashboardRoute.Home,
        title = "Home",
        icon = Icons.Outlined.Home
    ),
    DashboardNavItem(
        route = DashboardRoute.Sales,
        title = "Sales",
        icon = Icons.Outlined.PointOfSale
    ),
    DashboardNavItem(
        route = DashboardRoute.Purchase,
        title = "Purchase",
        icon = Icons.Outlined.ShoppingCart
    ),
    DashboardNavItem(
        route = DashboardRoute.Products,
        title = "Products",
        icon = Icons.Outlined.Inventory
    ),
    DashboardNavItem(
        route = DashboardRoute.Customers,
        title = "Customers",
        icon = Icons.Outlined.People
    ),
    DashboardNavItem(
        route = DashboardRoute.Reports,
        title = "Reports",
        icon = Icons.Outlined.BarChart
    ),
    DashboardNavItem(
        route = DashboardRoute.Settings,
        title = "Settings",
        icon = Icons.Outlined.Settings
    )
)
