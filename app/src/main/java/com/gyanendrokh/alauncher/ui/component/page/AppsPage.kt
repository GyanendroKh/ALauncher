package com.gyanendrokh.alauncher.ui.component.page

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItem
import com.gyanendrokh.alauncher.ui.component.AppsHeader
import com.gyanendrokh.alauncher.util.openApp
import com.gyanendrokh.alauncher.util.openAppSettings

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

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterHorizontally)
        ) {
            if (apps.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn {
                    items(apps) { app ->
                        AppItem(
                            app = app,
                            onPress = {
                                openApp(context = context, it.packageName)
                            },
                            onLongPress = {
                                openAppSettings(context = context, it.packageName)
                            }
                        )
                    }
                }
            }
        }
    }
}
