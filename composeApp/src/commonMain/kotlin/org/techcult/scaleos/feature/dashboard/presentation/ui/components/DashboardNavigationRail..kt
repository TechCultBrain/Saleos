package org.techcult.scaleos.feature.dashboard.presentation.ui.components

import org.techcult.scaleos.core.domain.model.PosType
import org.techcult.scaleos.feature.auth.domain.model.UserRole
import org.techcult.scaleos.feature.dashboard.presentation.navigation.DashboardNavItems
import org.techcult.scaleos.feature.dashboard.presentation.ui.root.filterDashboardItems


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun DashboardNavigationRail(
    navController: NavHostController,
    userRole: UserRole,
    posType: PosType
) {
    val filteredItems = remember(userRole, posType) {
        filterDashboardItems(DashboardNavItems, userRole, posType)
    }

    NavigationRail(
        modifier = Modifier

    ) {
        filteredItems.forEach { item ->
            val selected =
                navController.currentDestination?.route == item.route

            NavigationRailItem(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId)
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                label = { Text(item.title) }
            )
        }
    }
}
