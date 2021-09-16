package com.gyanendrokh.alauncher.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.gyanendrokh.alauncher.MainActivity
import com.gyanendrokh.alauncher.ui.component.page.AppsPage
import com.gyanendrokh.alauncher.ui.component.page.HomePage
import com.gyanendrokh.alauncher.viewmodel.AppsViewModel
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun Pager(
    modifier: Modifier = Modifier,
    appsViewModel: AppsViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = 2,
        initialOffscreenLimit = 2
    )

    MainActivity.setOnBackPressCallback {
        if (pagerState.currentPage != 0) {
            scope.launch {
                pagerState.scrollToPage(0, 0f)
            }
            true
        } else {
            false
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState
    ) { page ->
        if (page == 0) {
            HomePage(
                apps = appsViewModel.apps.value,
                onAppDrawerClick = {
                    scope.launch {
                        pagerState.scrollToPage(1, 0f)
                    }
                }
            )
        } else {
            AppsPage(apps = appsViewModel.apps.value.filter {
                !appsViewModel.hiddenApps.value.contains(it.packageName)
            })
        }
    }
}
