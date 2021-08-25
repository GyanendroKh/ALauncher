package com.gyanendrokh.alauncher.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.gyanendrokh.alauncher.ui.component.page.AppsPage
import com.gyanendrokh.alauncher.ui.component.page.HomePage
import com.gyanendrokh.alauncher.viewmodel.AppsViewModel

@ExperimentalPagerApi
@Composable
fun Pager(modifier: Modifier = Modifier, appsViewModel: AppsViewModel = viewModel()) {
    val pagerState = rememberPagerState(
        pageCount = 2,
        initialOffscreenLimit = 2
    )
    val apps = appsViewModel.apps.value

    HorizontalPager(
        modifier = modifier,
        state = pagerState
    ) { page ->
        if (page == 0) {
            HomePage()
        } else {
            AppsPage(apps = apps)
        }
    }
}
