package com.gyanendrokh.alauncher.ui.component.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItemList
import com.gyanendrokh.alauncher.ui.component.BottomBar
import com.gyanendrokh.alauncher.util.openApp

@Composable
fun HomePage(modifier: Modifier = Modifier, apps: List<AppEntity>) {
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (itemList, bottomBar) = createRefs()

            AppItemList(
                modifier = Modifier.constrainAs(itemList) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomBar.top)
                },
                apps = apps,
                onItemClick = {
                    openApp(context = context, it.packageName)
                }
            )

            BottomBar(
                modifier = Modifier.constrainAs(bottomBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}
