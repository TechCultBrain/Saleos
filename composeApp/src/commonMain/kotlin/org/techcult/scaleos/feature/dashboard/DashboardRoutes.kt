package org.techcult.scaleos.feature.dashboard


import kotlinx.serialization.Serializable

@Serializable
sealed interface DashboardRoute {
    @Serializable
    object Home : DashboardRoute

    @Serializable
    object Sales : DashboardRoute

    @Serializable
    object Purchase : DashboardRoute

    @Serializable
    object Products : DashboardRoute

    @Serializable
    object Customers : DashboardRoute

    @Serializable
    object Inventory : DashboardRoute

    @Serializable
    object Reports : DashboardRoute

    @Serializable
    object Settings : DashboardRoute
}
