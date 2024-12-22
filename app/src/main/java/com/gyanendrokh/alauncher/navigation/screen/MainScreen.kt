package com.gyanendrokh.alauncher.navigation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gyanendrokh.alauncher.navigation.Screen
import com.gyanendrokh.alauncher.ui.page.AppsPage
import com.gyanendrokh.alauncher.ui.page.HomePage
import com.gyanendrokh.alauncher.viewmodel.AppsViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavController, appsViewModel: AppsViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )

    val bottomApps by appsViewModel.apps.map {
        it.filter {
            listOf(
                "com.google.android.dialer",
                "com.android.chrome",
                "com.whatsapp",
                "com.google.android.youtube"
            ).indexOf(it.packageName) != -1
        }
    }.collectAsStateWithLifecycle(emptyList())
    val featuredApps = appsViewModel.featuredApps.value
    val apps = appsViewModel.filteredApps.collectAsState().value

    BackHandler(enabled = pagerState.currentPage != 0) {
        scope.launch {
            pagerState.scrollToPage(0, 0f)
        }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                scope.launch {
                    pagerState.scrollToPage(0, 0f)
                    appsViewModel.updateApps()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    HorizontalPager(
        modifier = Modifier,
        state = pagerState
    ) { page ->
        if (page == 0) {
            HomePage(
                apps = featuredApps,
                bottomApps = bottomApps,
                onAppDrawerClick = {
                    scope.launch {
                        pagerState.scrollToPage(1, 0f)
                    }
                }
            )
        } else {
            AppsPage(
                apps = apps,
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
    }
}
