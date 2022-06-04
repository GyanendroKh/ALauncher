package com.gyanendrokh.alauncher.ui.page

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItem
import com.gyanendrokh.alauncher.ui.component.BottomBar
import com.gyanendrokh.alauncher.ui.component.ClockWidget
import com.gyanendrokh.alauncher.util.createBitmap
import com.gyanendrokh.alauncher.util.getDateTime
import com.gyanendrokh.alauncher.util.openApp
import com.gyanendrokh.alauncher.util.openAppSettings

val handler = Handler(Looper.myLooper()!!)

const val offset = 50 + 20

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    apps: List<AppEntity>,
    onAppDrawerClick: () -> Unit = {}
) {
    var paths by remember { mutableStateOf<List<Path>>(ArrayList()) }
    var path by remember { mutableStateOf(Path()) }
    var boardSize by remember { mutableStateOf(IntSize(0, 0)) }
    var goneOutside by remember { mutableStateOf(false) }
    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
    val context = LocalContext.current
    val density = LocalDensity.current
    val dateTime = remember {
        mutableStateOf(getDateTime())
    }

    val cleanUpRunnable = remember {
        Runnable {
            with(density) {
                createBitmap(
                    paths.map { it.asAndroidPath() },
                    boardSize.width,
                    boardSize.height,
                    10.dp.toPx(),
                    45.dp.toPx().toInt(),
                    45.dp.toPx().toInt()
                )
            }
            paths = ArrayList()
            path = Path()
        }
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
                    .onGloballyPositioned {
                        boardSize = it.size
                    }
                    .pointerInput(Unit) {
                        val offsetToPx = (-offset).dp.toPx()
                        detectDragGestures(
                            onDragStart = {
                                handler.removeCallbacks(cleanUpRunnable)

                                path = Path().apply {
                                    reset()
                                    moveTo(it.x + offsetToPx, it.y)
                                }
                                x = it.x + offsetToPx
                                y = it.y
                            },
                            onDragEnd = {
                                path = Path().apply {
                                    addPath(path)
                                    lineTo(x, y)
                                }
                                paths = ArrayList<Path>().apply {
                                    addAll(paths)
                                    add(path)
                                }

                                handler.postDelayed(cleanUpRunnable, 1200)
                            }
                        ) { _, it ->
                            val isInside = x >= 0f && x <= boardSize.width.toFloat() &&
                                y >= 0f && y <= boardSize.height.toFloat()

                            if (isInside) {
                                if (goneOutside) {
                                    path.moveTo(x, y)
                                    goneOutside = false
                                }

                                path = Path().apply {
                                    addPath(path)
                                    lineTo(
                                        x,
                                        y,
                                        // (it.x + x) / 2,
                                        // (it.y + y) / 2
                                    )
                                }
                            } else {
                                goneOutside = true
                            }

                            x += it.x
                            y += it.y
                        }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = offset.dp),
                    verticalArrangement = if (apps.isNotEmpty()) Arrangement.SpaceAround else Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (apps.isEmpty()) {
                        CircularProgressIndicator()
                    } else {
                        apps.forEach { app ->
                            AppItem(
                                modifier = Modifier,
                                app = app,
                                onPress = {
                                    openApp(context = context, packageName = app.packageName)
                                },
                                onLongPress = {
                                    openAppSettings(context = context, packageName = app.packageName)
                                }
                            )
                        }
                    }
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                paths.forEach {
                    drawPath(
                        path = it,
                        style = Stroke(
                            width = 10f
                        ),
                        color = Color.White,
                    )
                }

                drawPath(
                    path = path,
                    style = Stroke(
                        width = 10f
                    ),
                    color = Color.White,
                )
            }
        }

        BottomBar(
            modifier = Modifier.fillMaxWidth(),
            onAppDrawerClick = onAppDrawerClick
        )
    }
}
