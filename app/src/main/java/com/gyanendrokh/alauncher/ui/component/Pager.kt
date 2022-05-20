package com.gyanendrokh.alauncher.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.gyanendrokh.alauncher.navigation.Screen
import com.gyanendrokh.alauncher.ui.component.page.AppsPage
import com.gyanendrokh.alauncher.ui.component.page.HomePage
import com.gyanendrokh.alauncher.viewmodel.AppsViewModel
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun Pager(
    modifier: Modifier = Modifier,
    navController: NavController,
    appsViewModel: AppsViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = 2,
        initialOffscreenLimit = 2
    )

    val featuredApps = appsViewModel.featuredApps.value
    val apps = appsViewModel.apps.value
    val hiddenApps = appsViewModel.hiddenApps.value

    println("Pager")
    println(featuredApps.toString())
    println(apps.toString())
    println(hiddenApps.toString())

    BackHandler(enabled = pagerState.currentPage != 0) {
        scope.launch {
            pagerState.scrollToPage(0, 0f)
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState
    ) { page ->
        if (page == 0) {
            HomePage(
                apps = featuredApps,
                onAppDrawerClick = {
                    scope.launch {
                        pagerState.scrollToPage(1, 0f)
                    }
                }
            )
        } else {
            AppsPage(
                apps = apps.filter {
                    !hiddenApps.contains(it.packageName)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
    }
}
