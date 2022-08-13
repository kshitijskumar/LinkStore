package com.example.linkstore.common.utils

import android.util.Log
import androidx.navigation.NavController

fun NavController.safeNavigateIfRequired(routeToNavigate: String, navigateToRoute: NavController.() -> Unit) {
    if (this.currentDestination?.route != routeToNavigate) {
        this.navigateToRoute()
    }
}