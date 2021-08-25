package com.gyanendrokh.alauncher.ui.component.page

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItemList
import com.gyanendrokh.alauncher.ui.component.BottomBar
import com.gyanendrokh.alauncher.ui.component.ClockWidget
import com.gyanendrokh.alauncher.util.getDateTime
import com.gyanendrokh.alauncher.util.openApp

val handler = Handler(Looper.myLooper()!!)

@Composable
fun HomePage(modifier: Modifier = Modifier, apps: List<AppEntity>) {
    val context = LocalContext.current
    val dateTime = remember {
        mutableStateOf(getDateTime())
    }

    fun update() {
        handler.postDelayed({
            dateTime.value = getDateTime()
            update()
        }, 500)
    }

    update()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (topHeader, itemList, bottomBar) = createRefs()

            ClockWidget(
                dateTime = dateTime.value,
                modifier = Modifier.constrainAs(topHeader) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
            )

            AppItemList(
                modifier = Modifier.constrainAs(itemList) {
                    start.linkTo(parent.start)
                    top.linkTo(topHeader.bottom)
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
