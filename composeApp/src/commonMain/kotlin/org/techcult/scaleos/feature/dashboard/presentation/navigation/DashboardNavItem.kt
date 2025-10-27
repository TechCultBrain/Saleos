package org.techcult.scaleos.feature.dashboard.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import org.techcult.scaleos.feature.dashboard.DashboardRoute
data class DashboardNavItem(
    val route: Any,
    val title: String,
    val icon: ImageVector
)