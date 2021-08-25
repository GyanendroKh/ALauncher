package com.gyanendrokh.alauncher.ui.component.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItemList
import com.gyanendrokh.alauncher.util.openApp

@Composable
fun HomePage(modifier: Modifier = Modifier, apps: List<AppEntity>) {
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AppItemList(apps = apps, onItemClick = {
            openApp(context = context, it.packageName)
        })
    }
}
