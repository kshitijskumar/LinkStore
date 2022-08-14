package com.example.linkstore.common.utils

import androidx.navigation.NavController

fun NavController.safeNavigateIfRequired(
    routeToNavigate: String,
    shouldCheckSubStringToo: Boolean = false,
    navigateToRoute: NavController.() -> Unit
) {
    val currentRoute = currentDestination?.route
    if (currentRoute != routeToNavigate || (shouldCheckSubStringToo && !currentRoute.contains(routeToNavigate))) {
        this.navigateToRoute()
    }
}