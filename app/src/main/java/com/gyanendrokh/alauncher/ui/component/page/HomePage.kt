package com.gyanendrokh.alauncher.ui.component.page

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItem
import com.gyanendrokh.alauncher.ui.component.BottomBar
import com.gyanendrokh.alauncher.ui.component.ClockWidget
import com.gyanendrokh.alauncher.util.getDateTime
import com.gyanendrokh.alauncher.util.openApp

val handler = Handler(Looper.myLooper()!!)

const val offset = 50 + 20

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    apps: List<AppEntity>,
    onAppDrawerClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val dateTime = remember {
        mutableStateOf(getDateTime())
    }

    LaunchedEffect(Unit) {
        fun update() {
            handler.postDelayed({
                dateTime.value = getDateTime()
                update()
            }, 500)
        }

        update()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ClockWidget(
            dateTime = dateTime.value,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Using offset to align gesture for drawing to start from left of icons
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (-offset).dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { }
                        ) { _, _ -> }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = offset.dp),
                    verticalArrangement = if (apps.isNotEmpty()) Arrangement.SpaceAround else Arrangement.Center
                ) {
                    if (apps.isEmpty()) {
                        CircularProgressIndicator()
                    } else {
                        apps.forEach { app ->
                            AppItem(
                                modifier = Modifier,
                                app = app,
                                onClick = {
                                    openApp(context = context, packageName = app.packageName)
                                }
                            )
                        }
                    }
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) { }
        }

        BottomBar(
            modifier = Modifier.fillMaxWidth(),
            onAppDrawerClick = onAppDrawerClick
        )
    }
}
