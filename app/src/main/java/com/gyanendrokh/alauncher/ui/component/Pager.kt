package com.gyanendrokh.alauncher.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val featuredApps = appsViewModel.featuredApps.value

    HorizontalPager(
        modifier = modifier.padding(
            vertical = 25.dp
        ),
        state = pagerState
    ) { page ->
        if (page == 0) {
            HomePage(apps = featuredApps)
        } else {
            AppsPage(apps = apps)
        }
    }
}
