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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
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
                                x = 0f
                                y = 0f

                                handler.removeCallbacks(cleanUpRunnable)
                                handler.postDelayed(cleanUpRunnable, 1200)
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

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val style = Stroke(
                    width = 18f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Bevel
                )

                clipRect(
                    top = 0f,
                    left = 0f,
                    right = boardSize.width.toFloat(),
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

        BottomBar(
            modifier = Modifier.fillMaxWidth(),
            onAppDrawerClick = onAppDrawerClick
        )
    }
}
