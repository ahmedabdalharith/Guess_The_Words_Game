package com.pyramid.questions.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun RadialRaysBackground(
    baseColor: Color = Color(0xFF2952B2),
    raysColor: Color = Color(0xFF1D3C89),
    numberOfRays: Int = 16,
    rotationSpeedDegPerSec: Float = 10f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "raysRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (360f / rotationSpeedDegPerSec * 1000).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "raysRotationAngle"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "raysPulse"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(baseColor)

            val centerX = size.width / 2
            val centerY = size.height / 2
            val maxRadius = size.width * 1.5f * pulseScale

            val angleStep = 360f / numberOfRays
            for (i in 0 until numberOfRays) {
                val angle = i * angleStep + rotationAngle
                val rayWidthAngle = 30f

                val startAngle = angle - rayWidthAngle / 2
                val endAngle = angle + rayWidthAngle / 2

                val x1 = centerX + maxRadius * cos(Math.toRadians(startAngle.toDouble())).toFloat()
                val y1 = centerY + maxRadius * sin(Math.toRadians(startAngle.toDouble())).toFloat()

                val x2 = centerX + maxRadius * cos(Math.toRadians(endAngle.toDouble())).toFloat()
                val y2 = centerY + maxRadius * sin(Math.toRadians(endAngle.toDouble())).toFloat()

                val path = Path().apply {
                    moveTo(centerX, centerY)
                    lineTo(x1, y1)
                    lineTo(x2, y2)
                    close()
                }

                drawPath(
                    path = path,
                    color = raysColor.copy(alpha = 0.8f),
                    style = Stroke(width = 2.5f)
                )

                drawPath(
                    path = path,
                    brush = Brush.radialGradient(
                        colors = listOf(
                            raysColor.copy(alpha = 0.7f),
                            raysColor.copy(alpha = 0.4f),
                            baseColor.copy(alpha = 0.2f)
                        ),
                        center = Offset(centerX, centerY),
                        radius = maxRadius
                    )
                )

                val innerRadius = size.width * 0.2f

                val innerX1 = centerX + innerRadius * cos(Math.toRadians(startAngle.toDouble())).toFloat()
                val innerY1 = centerY + innerRadius * sin(Math.toRadians(startAngle.toDouble())).toFloat()

                val innerX2 = centerX + innerRadius * cos(Math.toRadians(endAngle.toDouble())).toFloat()
                val innerY2 = centerY + innerRadius * sin(Math.toRadians(endAngle.toDouble())).toFloat()

                val innerGlowPath = Path().apply {
                    moveTo(centerX, centerY)
                    lineTo(innerX1, innerY1)
                    lineTo(innerX2, innerY2)
                    close()
                }

                drawPath(
                    path = innerGlowPath,
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            raysColor.copy(alpha = 0.1f)
                        ),
                        center = Offset(centerX, centerY),
                        radius = size.width * 0.2f
                    )
                )
            }

            // Draw center glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                ),
                radius = size.width * 0.12f,
                center = Offset(centerX, centerY)
            )
        }
    }
}