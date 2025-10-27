package org.techcult.scaleos.feature.dashboard.presentation.ui.root


import org.techcult.scaleos.core.domain.model.PosType
import org.techcult.scaleos.feature.auth.domain.model.UserRole
import org.techcult.scaleos.feature.dashboard.DashboardRoute
import org.techcult.scaleos.feature.dashboard.presentation.navigation.DashboardNavItem

fun filterDashboardItems(
    items: List<DashboardNavItem>,
    userRole: UserRole,
    posType: PosType
): List<DashboardNavItem> {

    // 1️⃣ Start with allowed routes per POS type
    val businessFiltered = when (posType) {
        PosType.RETAIL -> items.filterNot { it.route is DashboardRoute.Purchase }
        PosType.RESTAURANT -> items.filterNot { it.route is DashboardRoute.Purchase || it.route is DashboardRoute.Products }
        PosType.TEXTILE -> items.filterNot { it.route is DashboardRoute.Purchase }
    }

    // 2️⃣ Apply role-based filtering on top
    return when (userRole) {
        UserRole.ADMIN -> businessFiltered
        UserRole.MANAGER -> businessFiltered.filterNot { it.route is DashboardRoute.Settings }
        UserRole.CASHIER -> businessFiltered.filter {
            it.route is DashboardRoute.Sales || it.route is DashboardRoute.Home
        }
        UserRole.VIEWER -> businessFiltered.filter { it.route is DashboardRoute.Reports }
    }
}
