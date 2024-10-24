package com.gyanendrokh.alauncher.ui.page

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

const val offset = (50 + 20).toFloat()

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    apps: List<AppEntity>,
    onAppDrawerClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val dateTime = remember { mutableStateOf(getDateTime()) }

    DisposableEffect(Unit) {
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
    val coroutineScope = rememberCoroutineScope()
    val handler = remember { Handler(Looper.getMainLooper()) }
    var paths by remember { mutableStateOf<List<Path>>(ArrayList()) }
    var path by remember { mutableStateOf(Path()) }
    var boardSize by remember { mutableStateOf(IntSize(0, 0)) }
    var x by remember { mutableFloatStateOf(0f) }
    var y by remember { mutableFloatStateOf(0f) }

    val cleanUpRunnable = remember {
        Runnable {
            coroutineScope.launch(Dispatchers.Default) {
                createBitmap(
                    paths.map { it.asAndroidPath() },
                    width = boardSize.width,
                    height = boardSize.height,
                    brushSize = 21f,
                    scaledWidth = 28,
                    scaledHeight = 28
                )

                paths = ArrayList()
                path = Path()
            }
        }
    }

    val startCleanUp = {
        handler.removeCallbacks(cleanUpRunnable)
        handler.postDelayed(cleanUpRunnable, 1200)
    }

    DisposableEffect(Unit) {
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
                            val dx = it.x + xOffset.toPx()

                            path = Path().apply {
                                reset()
                                moveTo(dx, it.y)
                            }
                            x = dx
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
                .offset(x = -xOffset),
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
