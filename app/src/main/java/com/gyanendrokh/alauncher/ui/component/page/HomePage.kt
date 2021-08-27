package com.gyanendrokh.alauncher.ui.component.page

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
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
    val paths by remember { mutableStateOf(ArrayList<Path>()) }
    var path by remember { mutableStateOf(Path()) }
    var boardSize by remember { mutableStateOf(IntSize(0, 0)) }
    var goneOutside by remember { mutableStateOf(false) }
    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
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
                    .onGloballyPositioned {
                        boardSize = it.size
                    }
                    .pointerInput(Unit) {
                        val offsetToPx = (-offset).dp.toPx()
                        detectDragGestures(
                            onDragStart = {
                                path = Path().apply {
                                    reset()
                                    moveTo(it.x + offsetToPx, it.y)
                                }
                                x = it.x + offsetToPx
                                y = it.y
                            },
                            onDragEnd = {
                                path.apply {
                                    lineTo(x, y)
                                    paths.add(this)
                                }
                            }
                        ) { _, it ->
                            val isInside = x >= 0f && x <= boardSize.width.toFloat() &&
                                y >= 0f && y <= boardSize.height.toFloat()

                            if (isInside) {
                                if (goneOutside) {
                                    path.moveTo(x, y)
                                    goneOutside = false
                                }

                                path.apply {
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
            ) {
                // Need this line to recompose the Canvas
                println("Path $x $y")

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
