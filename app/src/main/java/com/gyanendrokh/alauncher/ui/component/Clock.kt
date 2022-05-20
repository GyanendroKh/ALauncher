package com.gyanendrokh.alauncher.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gyanendrokh.alauncher.model.DateTimeEntity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun ClockWidget(dateTime: DateTimeEntity, modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .width(IntrinsicSize.Max)
        ) {
            Text(
                text = dateTime.timeStr,
                fontSize = 38.sp,
                color = Color.White
            )

            Text(
                text = dateTime.dateStr,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Canvas(modifier = Modifier.size(width = 45.dp, height = 45.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val color = Color.White
            val center = Offset(x = canvasWidth / 2, y = canvasHeight / 2)
            val radius = min((canvasHeight / 2), (canvasWidth / 2))
            val style = Stroke(5f)

            val piX2 = PI * 2
            val piX2b4 = piX2 / 4f

            val minuteHandLength = radius - 3.dp.toPx()
            val minuteAngle = (piX2 * (dateTime.min / 60.0f)) - piX2b4
            val minuteHand = Offset(
                x = (center.x + cos(minuteAngle) * minuteHandLength).toFloat(),
                y = (center.y + sin(minuteAngle) * minuteHandLength).toFloat()
            )

            val hourHandLength = radius - 8.dp.toPx()
            val hourAngle =
                (piX2 * ((dateTime.hour * 5 + (dateTime.min / 60.0f) * 5) / 60.0f)) - piX2b4
            val hourHand = Offset(
                x = (center.x + cos(hourAngle) * hourHandLength).toFloat(),
                y = (center.y + sin(hourAngle) * hourHandLength).toFloat()
            )

            drawCircle(
                color = color,
                center = center,
                radius = radius,
                style = style
            )
            drawLine(
                start = center,
                end = minuteHand,
                color = color,
                strokeWidth = style.width
            )
            drawLine(
                start = center,
                end = hourHand,
                color = color,
                strokeWidth = style.width
            )
        }
    }
}
