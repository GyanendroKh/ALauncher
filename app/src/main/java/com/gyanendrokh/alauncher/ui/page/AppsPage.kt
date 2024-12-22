package com.gyanendrokh.alauncher.ui.page

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.gyanendrokh.alauncher.R
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItem
import com.gyanendrokh.alauncher.util.openApp
import com.gyanendrokh.alauncher.util.openAppSettings
import kotlinx.coroutines.launch

@Composable
fun AppsPage(
    modifier: Modifier = Modifier,
    apps: List<AppEntity>,
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                scope.launch {
                    scrollState.scrollToItem(0, 0)
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = modifier
            .padding(top = 20.dp)
    ) {
        Header(
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
                LazyColumn(
                    state = scrollState
                ) {
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

                    item {
                        Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                    }
                }
            }
        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    onStoreClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Apps",
                fontSize = 35.sp,
                color = Color.White
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.End)
        ) {
            IconButton(
                onClick = onStoreClick
            ) {
                Icon(
                    modifier = Modifier
                        .size(height = 28.dp, width = 28.dp),
                    painter = painterResource(id = R.drawable.ic_icons8_google_play),
                    contentDescription = "Play Store",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = onSettingsClick
            ) {
                Icon(
                    modifier = Modifier
                        .size(height = 28.dp, width = 28.dp),
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.7.dp,
            color = Color.White
        )
    }
}
