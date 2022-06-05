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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.ui.component.AppItem
import com.gyanendrokh.alauncher.ui.component.BottomBar
import com.gyanendrokh.alauncher.ui.component.ClockWidget
import com.gyanendrokh.alauncher.util.getDateTime
import com.gyanendrokh.alauncher.util.openApp
import com.gyanendrokh.alauncher.util.openAppSettings
import kotlin.concurrent.fixedRateTimer

const val offset = (50 + 20).toFloat()

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    apps: List<AppEntity>,
    onAppDrawerClick: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val dateTime = remember { mutableStateOf(getDateTime()) }

    DisposableEffect(lifecycleOwner) {
        val fixedRateTimer = fixedRateTimer("timer", initialDelay = 0, period = 500) {
            dateTime.value = getDateTime()
        }

        onDispose {
            fixedRateTimer.cancel()
        }
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
            GestureHandler(xOffset = -offset.dp, disable = apps.isEmpty()) {
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
                                    openAppSettings(
                                        context = context,
                                        packageName = app.packageName
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        BottomBar(
            modifier = Modifier.fillMaxWidth(),
            onAppDrawerClick = onAppDrawerClick
        )
    }
}

@Composable
fun GestureHandler(
    modifier: Modifier = Modifier,
    xOffset: Dp = 0.dp,
    disable: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current
    val handler = remember { Handler(Looper.getMainLooper()) }
    var paths by remember { mutableStateOf<List<Path>>(ArrayList()) }
    var path by remember { mutableStateOf(Path()) }
    var boardSize by remember { mutableStateOf(IntSize(0, 0)) }
    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }

    val cleanUpRunnable = remember {
        Runnable {
            paths = ArrayList()
            path = Path()
        }
    }

    val startCleanUp = {
        handler.removeCallbacks(cleanUpRunnable)
        handler.postDelayed(cleanUpRunnable, 1200)
    }

    DisposableEffect(lifecycle) {
        onDispose {
            handler.removeCallbacks(cleanUpRunnable)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .offset(x = xOffset)
            .onGloballyPositioned {
                boardSize = it.size
            }
            .pointerInput(disable) {
                if (!disable) {
                    detectDragGestures(
                        onDragStart = {
                            handler.removeCallbacks(cleanUpRunnable)

                            path = Path().apply {
                                reset()
                                moveTo(it.x, it.y)
                            }
                            x = it.x
                            y = it.y
                        },
                        onDragEnd = {
                            paths = ArrayList<Path>().apply {
                                addAll(paths)
                                add(Path().apply {
                                    addPath(path)
                                    lineTo(x, y)
                                })
                            }

                            path = Path()
                            x = 0f
                            y = 0f

                            startCleanUp()
                        }
                    ) { _, it ->
                        path = Path().apply {
                            addPath(path)
                            lineTo(x, y)
                        }

                        x += it.x
                        y += it.y
                    }
                }
            }
    ) {
        content()

        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val style = Stroke(
                width = 21f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )

            clipRect(
                top = 0f,
                left = 0f - xOffset.toPx(),
                right = boardSize.width.toFloat() - xOffset.toPx(),
                bottom = boardSize.height.toFloat()
            ) {
                paths.forEach {
                    drawPath(
                        path = it,
                        style = style,
                        color = Color.White,
                    )
                }

                drawPath(
                    path = path,
                    style = style,
                    color = Color.White,
                )
            }
        }
    }
}
