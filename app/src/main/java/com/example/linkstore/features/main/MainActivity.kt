package com.example.linkstore.features.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.linkstore.features.alllinksofgroup.ui.AllLinksOfGroupScreen
import com.example.linkstore.features.home.ui.HomeScreen
import com.example.linkstore.features.main.MainActivityNavigationSideEffect.NavigateToHomeScreenSideEffect
import com.example.linkstore.features.main.MainActivityNavigationSideEffect.NavigateToSaveLinkScreenSideEffect
import com.example.linkstore.features.savelink.data.models.appmodel.SaveScreenInitialData
import com.example.linkstore.features.savelink.data.toSaveScreenExistingData
import com.example.linkstore.features.savelink.data.toSaveScreenFreshData
import com.example.linkstore.features.savelink.ui.SaveLinkScreen
import com.example.linkstore.ui.theme.LinkStoreTheme
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityVm: MainActivityNavigationViewModel by viewModels()

    private val intentChannel = Channel<MainActivityNavigationIntent>(capacity = BUFFERED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LinkStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val scope = rememberCoroutineScope()

                    LaunchedEffect(key1 = navController) {
                        handleSideEffects(navController = navController)
                    }

                    NavHost(navController = navController, startDestination = "homeScreen") {
                        composable(route = "homeScreen") {
                            HomeScreen(
                                navigateToGroupLinks = {
                                    scope.launch {
                                        mainActivityVm.processIntent(
                                            MainActivityNavigationIntent.NavigateToAllLinksOfGroupIntent(it)
                                        )
                                    }
                                }
                            )
                        }
                        composable(
                            route = "saveLinkScreen/{isFreshData}/{processedLink}",
                            arguments = listOf(
                                navArgument("isFreshData") { type = NavType.BoolType },
                                navArgument("processedLink") { type = NavType.StringType }
                            )
                        ) {
                            val initialDataString = it.arguments?.getString("processedLink") ?: return@composable
                            val isFreshData = it.arguments?.getBoolean("isFreshData") ?: true
                            val initialData = if (isFreshData) {
                                Gson().fromJson(initialDataString, SaveScreenInitialData.FreshData::class.java)
                            } else {
                                Gson().fromJson(initialDataString, SaveScreenInitialData.ExistingData::class.java)
                            }
                            SaveLinkScreen(
                                initialData = initialData,
                                navigateToHomeScreen = {
                                    navController.navigateUp()
                                },
                                closeApp = {
                                    finishAndRemoveTask()
                                }
                            )
                        }
                        composable(
                            route = "allLinksOfGroup/{groupName}",
                            arguments = listOf(navArgument("groupName") { type = NavType.StringType })
                        ) {
                            val groupName = it.arguments?.getString("groupName") ?: return@composable
                            AllLinksOfGroupScreen(
                                groupName = groupName,
                                navigateBack = {
                                    navController.navigateUp()
                                },
                                navigateToEditFlow = {
                                    scope.launch {
                                        mainActivityVm.processIntent(
                                            MainActivityNavigationIntent.EditExistingLinkIntent(
                                                linkAppModel = it
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        handleIntentToStartFlow(intent)
    }

    private fun handleSideEffects(navController: NavController) {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                mainActivityVm
                    .sideEffect
                    .collect {
                        when(it) {
                            is NavigateToHomeScreenSideEffect -> {
                                navController.navigate("homeScreen") {
                                    launchSingleTop = true
                                }
                            }
                            is NavigateToSaveLinkScreenSideEffect -> {
                                val json = Uri.encode(Gson().toJson(it.processedLinkData.toSaveScreenFreshData()))
                                navController.navigate("saveLinkScreen/true/$json") {
                                    launchSingleTop = true
                                }
                            }
                            is MainActivityNavigationSideEffect.NavigateToAllLinksOfGroupSideEffect -> {
                                navController.navigate("allLinksOfGroup/${it.groupName}") {
                                    launchSingleTop = true
                                }
                            }
                            is MainActivityNavigationSideEffect.NavigateToEditLinkFLow -> {
                                val json = Uri.encode(Gson().toJson(it.linkAppModel.toSaveScreenExistingData()))
                                navController.navigate("saveLinkScreen/false/$json") {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                intentChannel
                    .receiveAsFlow()
                    .collect {
                        mainActivityVm.processIntent(it)
                    }
            }
        }
    }

    private fun handleIntentToStartFlow(intent: Intent?) {
        if(intent?.action == Intent.ACTION_SEND) {
            if (intent.type == "text/plain") {
                intentChannel.trySend(
                    MainActivityNavigationIntent.TryNavigateToSaveLinkOrHomeScreenIntent(
                        originalLink = intent.getStringExtra(Intent.EXTRA_TEXT)
                    )
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntentToStartFlow(intent)
    }
}
