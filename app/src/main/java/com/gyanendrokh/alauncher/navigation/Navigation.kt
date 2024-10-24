package com.gyanendrokh.alauncher.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gyanendrokh.alauncher.navigation.screen.MainScreen
import com.gyanendrokh.alauncher.navigation.screen.SettingsScreen
import com.gyanendrokh.alauncher.viewmodel.AppsViewModel
import kotlinx.coroutines.launch

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

    NavHost(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.15f))
            .safeContentPadding(),
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController, appsViewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController, appsViewModel)
        }
    }
}
