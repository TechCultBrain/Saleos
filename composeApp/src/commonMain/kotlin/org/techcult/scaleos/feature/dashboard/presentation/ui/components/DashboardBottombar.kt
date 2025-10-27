package org.techcult.scaleos.feature.dashboard.presentation.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.techcult.scaleos.core.domain.model.PosType
import org.techcult.scaleos.feature.auth.domain.model.UserRole
import org.techcult.scaleos.feature.dashboard.presentation.navigation.DashboardNavItems
import org.techcult.scaleos.feature.dashboard.presentation.ui.root.filterDashboardItems

@Composable
fun DashboardBottomBar(
    navController: NavHostController,
    userRole: UserRole,
    posType: PosType
) {
    val filteredItems = remember(userRole, posType) {
        filterDashboardItems(DashboardNavItems, userRole, posType)
    }

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        filteredItems.forEach { item ->
            val selected = currentRoute == item.route::class.simpleName
            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}
