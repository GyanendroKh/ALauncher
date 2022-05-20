package com.gyanendrokh.alauncher.navigation

import androidx.compose.runtime.Composable
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

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            Pager(navController = navController, appsViewModel = appsViewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController, appsViewModel = appsViewModel)
        }
    }
}
