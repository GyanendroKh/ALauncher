package com.gyanendrokh.alauncher.ui.component.page

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    Column(
        modifier = modifier
    ) {
        AppsHeader(
            onSettingsClick = {
                Toast.makeText(context, "Opening Settings", Toast.LENGTH_SHORT).show()
            },
            onStoreClick = {
                Toast.makeText(context, "Opening Play Store", Toast.LENGTH_SHORT).show()
            }
        )

        AppItemList(
            apps = apps,
            onItemClick = {
                openApp(context = context, it.packageName)
            }
        )
    }
}
