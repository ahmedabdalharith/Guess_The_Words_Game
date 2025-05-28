package com.pyramid.questions.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyramid.questions.R
import kotlinx.coroutines.delay

@Composable
fun LivesProgressIndicator(
    currentLives: Int,
    maxLives: Int = 5,
    isRecharging: Boolean = false,
    timeRemainingSeconds: Int = 0,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val progressAnimation by animateFloatAsState(
        targetValue = currentLives.toFloat() / maxLives.toFloat(),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progressAnimation"
    )

    val levelTextAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500),
        label = "levelTextAlpha"
    )

    val pulseAnimation = remember { Animatable(1f) }
    LaunchedEffect(currentLives) {
        pulseAnimation.animateTo(
            targetValue = 1.15f,
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        )
        pulseAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        )
    }

    val formattedTime = remember(timeRemainingSeconds) {
        val minutes = timeRemainingSeconds / 60
        val seconds = timeRemainingSeconds % 60
        context.getString(R.string.time_format, minutes, seconds)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(56.dp)
                .padding(bottom = 8.dp)
                .graphicsLayer(alpha = levelTextAlpha),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = context.getString(R.string.lives_count_format, currentLives, maxLives),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xAA000000), Color(0x88000000))
                        ),
                        RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF4C7CE4), Color(0xFF3A9CFF))
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Progress bar container
        Box(
            modifier = Modifier
                .height(130.dp)
                .padding(vertical = 4.dp)
        ) {
            // Outer container with border
            Box(
                modifier = Modifier
                    .width(34.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .border(
                        width = 2.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF3A9CFF), Color(0xFF4C7CE4))
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .background(Color(0x443A9CFF))
            )

            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FF4444))
                    .align(Alignment.Center)
            )


            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(120.dp * progressAnimation)
                    .clip(RoundedCornerShape(14.dp))
                    .graphicsLayer(scaleX = pulseAnimation.value, scaleY = 1f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(if (currentLives <= 1) 0xFFFF5252 else 0xFFFF8585),
                                Color(if (currentLives <= 1) 0xFFFF3838 else 0xFFFF6B6B),
                                Color(if (currentLives <= 1) 0xFFFF1A1A else 0xFFFF4444)
                            )
                        )
                    )
                    .align(Alignment.BottomCenter)
            )

            // Shine effect
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(120.dp * progressAnimation)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0x00FFFFFF),
                                Color(0x40FFFFFF),
                                Color(0x00FFFFFF)
                            ),
                            startX = 0f,
                            endX = 100f
                        )
                    )
                    .align(Alignment.BottomCenter)
            )

            if (currentLives > 0) {
                for (i in 0 until currentLives) {
                    val yOffset = (120.dp * (1 - ((i + 1).toFloat() / maxLives.toFloat())))

                    val heartPulse = remember { Animatable(1f) }
                    LaunchedEffect(key1 = Unit) {
                        while (true) {
                            delay(i * 200L)
                            heartPulse.animateTo(
                                targetValue = 1.2f,
                                animationSpec = tween(600, easing = FastOutSlowInEasing)
                            )
                            heartPulse.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(600, easing = FastOutSlowInEasing)
                            )
                            delay(800)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .graphicsLayer(
                                scaleX = heartPulse.value,
                                scaleY = heartPulse.value
                            )
                            .align(Alignment.TopCenter)
                            .offset(y = yOffset + 10.dp)
                    ) {
                        Text(
                            text = "♥",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
                .width(48.dp)
        )

        val bounceAnimation = remember { Animatable(0f) }
        LaunchedEffect(currentLives, isRecharging) {
            bounceAnimation.animateTo(
                targetValue = 12f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
            bounceAnimation.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.4f,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        if (currentLives == maxLives) {
            Text(
                text = context.getString(R.string.lives_full),
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .graphicsLayer(
                        translationY = bounceAnimation.value
                    )
            )
        } else if (isRecharging) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .graphicsLayer(
                        translationY = bounceAnimation.value
                    )
            ) {
                Text(
                    text = formattedTime,
                    color = Color(0xFFFF4444),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )

                Text(
                    text = context.getString(R.string.lives_charging),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        } else {
            Text(
                text = context.getString(R.string.lives_incomplete),
                color = Color(0xFFFF4444),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .graphicsLayer(
                        translationY = bounceAnimation.value
                    )
            )
        }
    }
}