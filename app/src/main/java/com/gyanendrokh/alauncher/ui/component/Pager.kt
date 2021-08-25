package com.gyanendrokh.alauncher.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.gyanendrokh.alauncher.ui.component.page.AppsPage
import com.gyanendrokh.alauncher.ui.component.page.HomePage

@ExperimentalPagerApi
@Composable
fun Pager(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(
        pageCount = 2,
        initialOffscreenLimit = 2
    )

    HorizontalPager(
        modifier = modifier,
        state = pagerState
    ) { page ->
        if (page == 0) {
            HomePage()
        } else {
            AppsPage()
        }
    }
}
