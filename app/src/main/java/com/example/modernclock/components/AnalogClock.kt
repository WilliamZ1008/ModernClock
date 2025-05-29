package com.example.modernclock.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalogClock(
    modifier: Modifier = Modifier,
    time: LocalTime = LocalTime.now(),
    primaryColor: Color = Color.White,
    secondaryColor: Color = Color.White.copy(alpha = 0.7f)
) {
    Canvas(modifier = modifier.size(200.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.width.coerceAtMost(size.height) / 2f

        // 绘制磨砂表盘背景
        drawCircle(
            color = Color.White.copy(alpha = 0.15f),
            radius = radius,
            center = center
        )

        // 绘制表盘边框
        drawCircle(
            color = primaryColor.copy(alpha = 0.3f),
            radius = radius,
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
        )

        // 绘制刻度
        for (i in 0..11) {
            val angle = i * 30f * (PI / 180f).toFloat()
            val startRadius = radius * 0.9f
            val endRadius = radius
            val start = Offset(
                x = center.x + cos(angle) * startRadius,
                y = center.y + sin(angle) * startRadius
            )
            val end = Offset(
                x = center.x + cos(angle) * endRadius,
                y = center.y + sin(angle) * endRadius
            )
            drawLine(
                color = primaryColor.copy(alpha = 0.5f),
                start = start,
                end = end,
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
        }

        // 绘制时针
        rotate(time.hour * 30f + time.minute * 0.5f) {
            drawLine(
                color = primaryColor,
                start = center,
                end = Offset(center.x, center.y - radius * 0.5f),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }

        // 绘制分针
        rotate(time.minute * 6f) {
            drawLine(
                color = secondaryColor,
                start = center,
                end = Offset(center.x, center.y - radius * 0.7f),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        // 绘制秒针
        rotate(time.second * 6f) {
            drawLine(
                color = Color.Red.copy(alpha = 0.8f),
                start = center,
                end = Offset(center.x, center.y - radius * 0.8f),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
        }

        // 绘制中心点
        drawCircle(
            color = Color.Red.copy(alpha = 0.8f),
            radius = 4f,
            center = center
        )
    }
} 