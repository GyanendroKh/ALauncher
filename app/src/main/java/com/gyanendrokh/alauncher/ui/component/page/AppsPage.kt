package com.gyanendrokh.alauncher.ui.component.page

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gyanendrokh.alauncher.SettingsActivity
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
            .padding(top = 20.dp)
    ) {
        AppsHeader(
            onSettingsClick = {
                context.startActivity(Intent(context, SettingsActivity::class.java))
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
