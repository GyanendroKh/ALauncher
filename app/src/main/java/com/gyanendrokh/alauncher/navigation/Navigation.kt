package com.gyanendrokh.alauncher.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.gyanendrokh.alauncher.navigation.screen.SettingsScreen
import com.gyanendrokh.alauncher.ui.component.Pager
import com.gyanendrokh.alauncher.viewmodel.AppsViewModel

@ExperimentalPagerApi
@Composable
fun Navigation() {
    val navController = rememberNavController().apply {
        enableOnBackPressed(true)
    }
    val appsViewModel: AppsViewModel = viewModel()
    val enableBackHandler = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            enableBackHandler.value = !destination.route.equals("main")
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
