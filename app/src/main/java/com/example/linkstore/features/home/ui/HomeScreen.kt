package com.example.linkstore.features.home.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.linkstore.features.home.HomeIntent
import com.example.linkstore.features.home.HomeSideEffect

@Composable
fun HomeScreen(
    homeVm: HomeViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit) {
        homeVm.processIntent(HomeIntent.InitializationIntent)
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            homeVm.sideEffect
                .collect {
                    when(it) {
                        is HomeSideEffect.NavigateToLinksListScreen -> {

                        }
                    }
                }
        }
    }

    val state = homeVm.state.collectAsState()

    Log.d("HomeScreenStuff", "state: ${state.value}")

}