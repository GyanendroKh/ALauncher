package com.gyanendrokh.alauncher.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.gyanendrokh.alauncher.navigation.screen.SettingsScreen
import com.gyanendrokh.alauncher.ui.component.Pager
import com.gyanendrokh.alauncher.viewmodel.AppsViewModel
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun Navigation() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val appsViewModel: AppsViewModel = viewModel()
    val enableBackHandler = remember {
        mutableStateOf(true)
    }

    DisposableEffect(lifecycleOwner) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            enableBackHandler.value = !destination.route.equals("main")
        }

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                scope.launch {
                    appsViewModel.updateApps()
                }
            }
        }

        navController.addOnDestinationChangedListener(listener)
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler(enabled = enableBackHandler.value) {
        navController.navigateUp()
    }

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            Pager(navController = navController, appsViewModel = appsViewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController, appsViewModel = appsViewModel)
        }
    }
}
