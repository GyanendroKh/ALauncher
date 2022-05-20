package com.gyanendrokh.alauncher.navigation

sealed class Screen(val route: String) {
    object Main: Screen("main")
    object Settings: Screen("settings")
}
