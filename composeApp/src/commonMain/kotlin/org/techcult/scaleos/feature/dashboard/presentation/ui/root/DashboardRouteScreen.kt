package org.techcult.scaleos.feature.dashboard.presentation.ui.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import org.techcult.scaleos.core.domain.model.PosType
import org.techcult.scaleos.core.utils.DeviceConfiguration
import org.techcult.scaleos.feature.auth.domain.model.UserRole
import org.techcult.scaleos.feature.dashboard.presentation.navigation.DashboardGraph
import org.techcult.scaleos.feature.dashboard.presentation.ui.components.DashboardBottomBar
import org.techcult.scaleos.feature.dashboard.presentation.ui.components.DashboardNavigationRail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardRouteScreen(
    mainNavController: NavHostController,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val dashboardNavController = rememberNavController()
    val userRole = remember { UserRole.ADMIN }
    val posType = remember { PosType.RETAIL }
    val currentDestination =
        dashboardNavController.currentBackStackEntryAsState().value?.destination
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    Scaffold(
        bottomBar = {
            if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT || deviceConfiguration== DeviceConfiguration.TABLET_PORTRAIT || deviceConfiguration== DeviceConfiguration.MOBILE_LANDSCAPE) {
                DashboardBottomBar(navController = dashboardNavController, userRole, posType)
            }
        }
    ) { paddingValues ->
        Row(modifier = Modifier.fillMaxSize()) {

            // Desktop - Navigation Rail
            if (deviceConfiguration == DeviceConfiguration.DESKTOP || deviceConfiguration == DeviceConfiguration.TABLET_LANDSCAPE) {
                DashboardNavigationRail(
                    navController = dashboardNavController,
                    userRole,
                    posType,
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
                )
            }


            // Content area
            Box(modifier = Modifier.weight(1f)) {
                DashboardGraph(
                    dashboardNavController,
                    mainNavController = mainNavController,
                    paddingValues
                )

            }
        }
    }

}