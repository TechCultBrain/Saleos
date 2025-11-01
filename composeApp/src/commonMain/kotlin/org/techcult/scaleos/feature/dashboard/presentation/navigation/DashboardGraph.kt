package org.techcult.scaleos.feature.dashboard.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.techcult.scaleos.feature.dashboard.DashboardRoute
import org.techcult.scaleos.feature.dashboard.presentation.ui.CustomersScreen
import org.techcult.scaleos.feature.dashboard.presentation.ui.DashboardHomeScreen
import org.techcult.scaleos.feature.dashboard.presentation.ui.ProductsScreen
import org.techcult.scaleos.feature.dashboard.presentation.ui.PurchaseScreen
import org.techcult.scaleos.feature.dashboard.presentation.ui.ReportsScreen
import org.techcult.scaleos.feature.dashboard.presentation.ui.SalesScreen
import org.techcult.scaleos.feature.settings.presentation.navigation.SettingsNavGraph
import org.techcult.scaleos.feature.settings.presentation.ui.SettingsRootScreen

@Composable
fun DashboardGraph(dashboardNavController: NavHostController, mainNavController: NavHostController, paddingValues: PaddingValues)
{
    NavHost(dashboardNavController, startDestination = DashboardRoute.Home)
    {
        composable<DashboardRoute.Home> {
            DashboardHomeScreen(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))
        }

        composable<DashboardRoute.Sales> {
            SalesScreen()
        }

        composable<DashboardRoute.Purchase> {
            PurchaseScreen()
        }

        composable<DashboardRoute.Products> {
            ProductsScreen()
        }

        composable<DashboardRoute.Customers> {
            CustomersScreen()
        }

        composable<DashboardRoute.Reports> {
            ReportsScreen()
        }
        composable<DashboardRoute.Settings> {
            SettingsRootScreen()
        }




    }
}