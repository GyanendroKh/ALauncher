package com.gyanendrokh.alauncher.ui.component.page

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItem
import com.gyanendrokh.alauncher.ui.component.AppsHeader
import com.gyanendrokh.alauncher.util.openApp

@Composable
fun AppsPage(
    modifier: Modifier = Modifier,
    apps: List<AppEntity>,
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(top = 20.dp)
    ) {
        AppsHeader(
            onSettingsClick = onSettingsClick,
            onStoreClick = {
                Toast.makeText(context, "Opening Play Store", Toast.LENGTH_SHORT).show()
            }
        )

        LazyColumn {
            items(apps) { app ->
                AppItem(
                    app = app,
                    onClick = {
                        openApp(context = context, it.packageName)
                    }
                )
            }
        }
    }
}
