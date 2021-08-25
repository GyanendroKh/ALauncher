package com.gyanendrokh.alauncher.ui.component.page

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItemList
import com.gyanendrokh.alauncher.ui.component.AppsHeader
import com.gyanendrokh.alauncher.util.openApp

@Composable
fun AppsPage(
    modifier: Modifier = Modifier,
    apps: List<AppEntity>
) {
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (header, list) = createRefs()

            AppsHeader(
                modifier = Modifier.constrainAs(header) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
                onSettingsClick = {
                    Toast.makeText(context, "Opening Settings", Toast.LENGTH_SHORT).show()
                },
                onStoreClick = {
                    Toast.makeText(context, "Opening Play Store", Toast.LENGTH_SHORT).show()
                }
            )

            AppItemList(
                modifier = Modifier.constrainAs(list) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(header.bottom)
                },
                apps = apps,
                onItemClick = {
                    openApp(context = context, it.packageName)
                }
            )
        }
    }
}
