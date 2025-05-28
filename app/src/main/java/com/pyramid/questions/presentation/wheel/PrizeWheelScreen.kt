package com.pyramid.questions.presentation.wheel

import android.content.Context
import android.content.res.Configuration
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.presentation.components.RadialRaysBackground
import com.pyramid.questions.presentation.components.TopGameBar
import com.pyramid.questions.presentation.components.YellowButtonWithIcon
import com.pyramid.questions.presentation.test.Button3D
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sin

@Composable
fun WheelPointer(
    modifier: Modifier = Modifier,
    isSpinning: Boolean,
    imageRes: Int,
    currentPrize: WheelPrize = WheelPrize(R.drawable.coin4, "100", stringResource(R.string.coin_100))
) {
    val vibrationTransition = rememberInfiniteTransition(label = "vibration")
    val offsetX = vibrationTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isSpinning) 2f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vibration_x"
    )

    Box(
        modifier = modifier
            .offset(x = with(LocalDensity.current) { offsetX.value.toDp() })
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = stringResource(R.string.wheel_pointer_desc),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier
                .padding(4.dp)
                .padding(top=28.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(currentPrize.imageResId),
                contentDescription = null,
                modifier= Modifier.size(42.dp),
                contentScale = ContentScale.Crop
            )
            if (currentPrize.text != stringResource(R.string.delete_two_letters)){
                Text(
                    text = currentPrize.text,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.arbic_font_bold_2))
                )
            }
        }
    }
}

@Composable
fun CenterSpinButton3D(
    modifier: Modifier = Modifier,
    isSpinning: Boolean,
    currentPrize: WheelPrize? = null
) {
    val pulsateTransition = rememberInfiniteTransition(label = "pulsate")
    val scale = pulsateTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpinning) 1.1f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSpinning) 250 else 800, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsate_scale"
    )

    Box(
        modifier = modifier
            .scale(scale.value)
            .shadow(12.dp, CircleShape)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF5252),
                        Color(0xFFD32F2F)
                    )
                )
            )
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFCCCC),
                        Color(0xFFAA2222)
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inner shadow effect
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    radius = size.minDimension * 0.4f
                ),
                radius = size.minDimension * 0.4f,
                center = center
            )
        }

        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF8F8F),
                            Color(0xFFFF5252)
                        )
                    )
                )
                .border(2.dp, Color(0xFFFFAAAA), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (currentPrize != null && !isSpinning) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = currentPrize.imageResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = currentPrize.text,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color(0xFF7A0000),
                                offset = Offset(1f, 1f),
                                blurRadius = 2f
                            )
                        )
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.spin_button),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color(0xFF7A0000),
                            offset = Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpinWheelScreen(
    onOpenStore: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    onImageClick: (Int) -> Unit = {},
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    var isSpinning by remember { mutableStateOf(false) }
    var isSpined by remember { mutableStateOf(false) }
    var targetRotation by remember { mutableFloatStateOf(0f) }
    val rotation = remember { Animatable(0f) }
    var showPrizeDialog by remember { mutableStateOf(false) }
    var currentPrize by remember { mutableStateOf<WheelPrize?>(null) }
    var currentPointingPrize by remember { mutableStateOf<WheelPrize?>(null) }
    var isConfettiVisible by remember { mutableStateOf(false) }
    val lightAnimator = remember { Animatable(0f) }
    val bulbColors = remember {
        listOf(
            Color(0xFFFFD700), // Gold
            Color(0xFFE0E0E0), // Silver
            Color(0xFFFFB74D), // Light Orange
            Color(0xFFFFD54F)  // Light Yellow
        )
    }

    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            // Fast blinking while spinning
            while(isSpinning) {
                lightAnimator.animateTo(1f, tween(200))
                lightAnimator.animateTo(0f, tween(200))
            }
        } else {
            while(true) {
                lightAnimator.animateTo(1f, tween(800))
                lightAnimator.animateTo(0f, tween(800))
            }
        }
    }

    val context = LocalContext.current
//    val wheelSpinSound = MediaPlayer.create(context, R.raw.notification)
//    val victorySound = MediaPlayer.create(context, R.raw.bell_notification)

    val prizes = getPrizesList()

    LaunchedEffect(rotation.value) {
        if (isSpinning) {
            val normalizedRotation = (rotation.value % 360 + 360) % 360
            val segment = floor(normalizedRotation / (360f / prizes.size)).toInt()
            val prizeIndex = (prizes.size - segment - 1) % prizes.size
            currentPointingPrize = prizes[prizeIndex]
        }
    }

    val preferencesManager = remember { GamePreferencesManager(navController.context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.en_font
    }))
    val playerStats =preferencesManager.getPlayerStats()
    CompositionLocalProvider(
        LocalContext provides LocalContext.current.createConfigurationContext(
            Configuration().apply { setLocale(currentLocale) }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E3C8A),
                            Color(0xFF3871E0),
                            Color(0xFF0C1B45)
                        )
                    )
                )
        ) {
            RadialRaysBackground()
            TopGameBar(
                playerStats = playerStats,
                onOpenStore = onOpenStore,
                onOpenProfile = onOpenProfile,
                showDriver = true,
                onBackClicked = {
                    navController.popBackStack()
                },
                fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
            )

            // Wheel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                // Wheel Background with animated lights
                EnhancedWheelBackground(
                    modifier = Modifier
                        .size(340.dp)
                        .align(Alignment.Center),
                    isSpinning = isSpinning,
                    bulbColors = bulbColors
                )

                // Spinning Wheel
                WheelContent(
                    modifier = Modifier
                        .size(300.dp)
                        .rotate(rotation.value),
                    segments = prizes.size,
                    prizes = prizes,
                    isSpinning = isSpinning
                )

                // Center Button with result
                CenterSpinButton3D(
                    modifier = Modifier
                        .size(90.dp)
                        .align(Alignment.Center)
                        .scale(if (isSpinning) 0.9f else 1f),
                    isSpinning = isSpinning,
                    currentPrize = currentPrize
                )

                WheelPointer(
                    modifier = Modifier
                        .width(150.dp)
                        .height(200.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (-120).dp),
                    isSpinning = isSpinning,
                    imageRes = R.drawable.indictor,
                    currentPrize = currentPointingPrize ?: prizes[0]
                )
            }

            // Bottom Spin Button

            if (!isSpined){
                Button3D(
                    modifier = Modifier
                        .padding(bottom = 100.dp)
                        .align(Alignment.BottomCenter)
                        .width(200.dp)
                        .height(50.dp),
                    onClick = {
                        isSpined =true
                        if (!isSpinning) {
                            isSpinning = true
                            showPrizeDialog = false
                            currentPrize = null
                            scope.launch {
                                try {
                                    //  wheelSpinSound.start()
                                } catch (e: Exception) {
                                    // Handle potential media player errors
                                }
                            }

                            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                            if (vibrator?.hasVibrator() == true) {
                                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                            }

                            val spinCount = (5..8).random()
                            val extraAngle = (0..359).random().toFloat()
                            targetRotation = rotation.value + (spinCount * 360f) + extraAngle
                            scope.launch {
                                rotation.animateTo(
                                    targetValue = targetRotation,
                                    animationSpec = tween(
                                        durationMillis = 7000,
                                        easing = CubicBezierEasing(0.35f, 0.0f, 0.25f, 1.0f)
                                    )
                                )

                                val normalizedRotation = (rotation.value % 360 + 360) % 360
                                val segment = floor(normalizedRotation / (360f / prizes.size)).toInt()
                                val prizeIndex = (prizes.size - segment - 1) % prizes.size

                                currentPrize = prizes[prizeIndex ]
                                currentPointingPrize = currentPrize
                                isSpinning = false

                                // Play victory sound and vibrate
                                try {
                                    // victorySound.start()
                                } catch (e: Exception) {
                                    // Handle potential media player errors
                                }

                                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                                if (vibrator?.hasVibrator() == true) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                                }

                                delay(500) // Small delay before showing prize
                                showPrizeDialog = true
                                isConfettiVisible = true

                                delay(3000) // Hide confetti after 3 seconds
                                isConfettiVisible = false
                            }
                        }
                    },
                    text = stringResource(R.string.spin_button),
                    backgroundColor=Color(0xFF2ED15F),
                    shadowColor= Color(0xFF13833A),
                )
            }else if (isSpinning){
                Spacer(
                    Modifier.height(22.dp)
                )
            }else{
                YellowButtonWithIcon(
                    modifier = Modifier
                        .padding(bottom = 100.dp)
                        .align(Alignment.BottomCenter),
                    width = 200.dp,
                    height = 50.dp,
                    onClick = { /* your action */ },
                    content = {
                        Row (
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier

                        ){
                            Image(
                                painterResource(R.drawable.video_ic),
                                contentDescription = stringResource(R.string.video_icon_desc),
                            )
                            Text(
                                text = stringResource(R.string.watch_video),
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                                color = Color(0xFF5F4B00)
                            )
                        }
                    }

                )
            }
            if (isConfettiVisible) {
                ConfettiAnimation(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun EnhancedWheelBackground(
    modifier: Modifier = Modifier,
    isSpinning: Boolean = false,
    isRandomPattern: Boolean = false,
    bulbColors: List<Color> = listOf(
        Color(0xFFFFD700),
        Color(0xFF1E88E5),
        Color(0xFFE53935),
        Color(0xFF43A047),
        Color(0xFFFF9800),
        Color(0xFF9C27B0),
        Color(0xFF00BCD4),
        Color(0xFFFF4081)
    ),
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wheelTransition")
    val lightAnimatorValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isSpinning) 500 else 2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "lightAnimator"
    )

    val pulseAnimatorValue by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAnimator"
    )

    val randomPatternAnimator by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "randomPatternAnimator"
    )

    Box(modifier = modifier.aspectRatio(1f)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension / 2

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    radius = size.minDimension * 0.6f
                ),
                radius = radius + 20f,
            )

            val outerRingGradient = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFE0E0E0),
                    Color(0xFFBBBBBB),
                    Color(0xFF9E9E9E),
                    Color(0xFFBBBBBB)
                )
            )
            drawCircle(
                brush = outerRingGradient,
                radius = radius,
                style = Stroke(width = 60f)
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                radius = radius - 25f,
                style = Stroke(width = 8f)
            )

            drawCircle(
                color = Color.Black.copy(alpha = 0.15f),
                radius = radius + 25f,
                style = Stroke(width = 10f)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF4A148C).copy(alpha = 0.3f * pulseAnimatorValue),
                        Color.Transparent
                    )
                ),
                radius = radius * 0.7f * pulseAnimatorValue,
                center = Offset(centerX, centerY)
            )

            val bulbCount = 16

            val randomValues = List(bulbCount) { index ->
                val seed = (index * 31).toFloat() / bulbCount
                val sinValue = sin(seed * 10).toFloat()
                sinValue * 0.5f + 0.5f
            }

            for (i in 0 until bulbCount) {
                val angle = (i * (2 * Math.PI / bulbCount))
                val x = centerX + (radius * cos(angle)).toFloat()
                val y = centerY + (radius * sin(angle)).toFloat()

                val bulbIndex = (i + floor((lightAnimatorValue * bulbColors.size).toDouble()).toDouble()) % bulbColors.size
                val baseColor = bulbColors[bulbIndex.toInt()]

                val brightness = if (isRandomPattern) {
                    val randomValue = randomValues[i]
                    val animPhase = (randomPatternAnimator + randomValue) % 1.0f
                    if (animPhase < 0.5f) {
                        lerp(0.3f, 1f, animPhase * 2)
                    } else {
                        lerp(1f, 0.3f, (animPhase - 0.5f) * 2)
                    }
                } else {
                    val brightnessPhase = (i / 2) % 4
                    val normalizedAnimValue = (lightAnimatorValue * 4f) % 4f

                    when {
                        brightnessPhase == floor(normalizedAnimValue.toDouble()).toInt() -> {
                            lerp(0.3f, 1f, (normalizedAnimValue % 1f))
                        }
                        (brightnessPhase + 1) % 4 == floor(normalizedAnimValue.toDouble()).toInt() -> {
                            lerp(1f, 0.3f, (normalizedAnimValue % 1f))
                        }
                        else -> 0.3f
                    }
                }

                val animatedColor = baseColor.copy(alpha = brightness)

                val bulbRadiusMultiplier = 2.5f

                drawCircle(
                    color = Color.LightGray.copy(alpha = 0.7f),
                    radius = 12f * bulbRadiusMultiplier,
                    center = Offset(x, y),
                    style = Stroke(width = 2f * bulbRadiusMultiplier)
                )
                drawCircle(
                    color = Color.DarkGray.copy(alpha = 0.5f),
                    radius = 11f * bulbRadiusMultiplier,
                    center = Offset(x, y)
                )
                drawCircle(
                    color = animatedColor.copy(alpha = 0.4f),
                    radius = 14f * bulbRadiusMultiplier,
                    center = Offset(x, y)
                )
                if (brightness > 0.8f) {
                    drawCircle(
                        color = Color.White.copy(alpha = (brightness - 0.8f) * 5f),
                        radius = 11f * bulbRadiusMultiplier,
                        center = Offset(x, y)
                    )

                    val starSize = 14f * bulbRadiusMultiplier
                    val starPoints = 4
                    for (j in 0 until starPoints) {
                        val starAngle = j * (2 * Math.PI / starPoints) + (lightAnimatorValue * Math.PI / 2)
                        val starX = x + (starSize * cos(starAngle)).toFloat()
                        val starY = y + (starSize * sin(starAngle)).toFloat()
                        drawLine(
                            color = Color.White.copy(alpha = (brightness - 0.8f) * 3f),
                            start = Offset(x, y),
                            end = Offset(starX, starY),
                            strokeWidth = 1.5f * bulbRadiusMultiplier
                        )
                    }
                }
                drawCircle(
                    color = animatedColor,
                    radius = 9f * bulbRadiusMultiplier,
                    center = Offset(x, y)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.7f),
                    radius = 3f * bulbRadiusMultiplier,
                    center = Offset(x - 2f * bulbRadiusMultiplier, y - 2f * bulbRadiusMultiplier)
                )

                drawArc(
                    color = Color.White.copy(alpha = 0.5f),
                    startAngle = 45f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(x - 6f * bulbRadiusMultiplier, y - 6f * bulbRadiusMultiplier),
                    size = Size(12f * bulbRadiusMultiplier, 12f * bulbRadiusMultiplier),
                    style = Stroke(width = 2f * bulbRadiusMultiplier)
                )
            }
        }
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}

@Composable
fun ConfettiAnimation(modifier: Modifier) {
    val particles = remember {
        List(100) {
            ConfettiParticle(
                x = (0..1000).random().toFloat(),
                y = (-500..0).random().toFloat(),
                velocity = (3..10).random().toFloat(),
                angle = (-30..30).random().toFloat(),
                rotation = (0..360).random().toFloat(),
                rotationSpeed = (-5..5).random().toFloat(),
                size = (10..25).random().toFloat(),
                color = listOf(
                    Color(0xFFFF5252), // Red
                    Color(0xFF2196F3), // Blue
                    Color(0xFFFFEB3B), // Yellow
                    Color(0xFF4CAF50), // Green
                    Color(0xFFFF9800), // Orange
                    Color(0xFFE040FB)  // Pink
                ).random()
            )
        }
    }

    // Animation for falling confetti
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(3000, easing = LinearEasing)
        )
    }

    Canvas(modifier = modifier) {
        particles.forEach { particle ->
            val progress = animationProgress.value

            // Update particle position
            val currentY = particle.y + particle.velocity * progress * size.height
            val currentX = particle.x + sin(progress * 10 + particle.angle) * 20

            // Calculate rotation
            val currentRotation = particle.rotation + particle.rotationSpeed * progress * 360

            // Draw confetti particle
            withTransform({
                translate(currentX, currentY)
                rotate(currentRotation)
            }) {
                // Draw a rectangle for the confetti
                drawRect(
                    color = particle.color,
                    topLeft = Offset(-particle.size/2, -particle.size/2),
                    size = Size(particle.size, particle.size/2)
                )
            }
        }
    }
}

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val velocity: Float,
    val angle: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val size: Float,
    val color: Color
)

data class WheelPrize(
    val imageResId: Int,
    val text: String,
    val value: String,
    val isPremium: Boolean = false
)

@Composable
fun getPrizesList(): List<WheelPrize> {
    return listOf(
        WheelPrize(R.drawable.coin1, "1", stringResource(R.string.coin_1)),
        WheelPrize(R.drawable.help_idea, stringResource(R.string.hint), ""),
        WheelPrize(R.drawable.coin2, "15", stringResource(R.string.coin_15)),
        WheelPrize(R.drawable.heart, "1", stringResource(R.string.heart)),
        WheelPrize(R.drawable.delete_letters, stringResource(R.string.delete_two_letters), ""),
        WheelPrize(R.drawable.coin3, "20", stringResource(R.string.coin_20)),
        WheelPrize(R.drawable.coin4, "25", stringResource(R.string.coin_25)),
        WheelPrize(R.drawable.coin5, "50", stringResource(R.string.coin_50)),
    )
}
@Composable
fun WheelContent(
    modifier: Modifier = Modifier,
    segments: Int,
    prizes: List<WheelPrize>,
    isSpinning: Boolean,
    wheelRotation: Float = 0f
) {
    val colors = listOf(
        Color(0xFFFF5252),
        Color(0xFF536DFE),
        Color(0xFFFFEB3B),
        Color(0xFFFF9800),
        Color(0xFF9C27B0),
        Color(0xFF2196F3),
        Color(0xFF4CAF50),
        Color(0xFF03A9F4),
        Color(0xFFFFCA28),
        Color(0xFFFF4081)
    )
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.6f),
        Color.White.copy(alpha = 0.2f)
    )

    val shimmerTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerTranslate = shimmerTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isSpinning) 600 else 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale = pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Animate the pick indicator glow
    val pickGlowTransition = rememberInfiniteTransition(label = "pick_glow")
    val pickGlowAlpha = pickGlowTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pick_glow_alpha"
    )

    Canvas(modifier = modifier) {
        val anglePerSegment = 360f / segments
        val radius = size.minDimension / 2
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Draw the wheel segments
        for (i in 0 until segments) {
            val startAngle = i * anglePerSegment
            prizes[i].isPremium
            val baseColor = colors[i % colors.size]
            val gradientColors = listOf(
                baseColor.copy(alpha = 0.95f),
                baseColor,
                baseColor.copy(alpha = 0.85f)
            )
            drawArc(
                brush = Brush.radialGradient(
                    colors = gradientColors,
                    center = Offset(centerX, centerY),
                    radius = radius * 1.5f
                ),
                startAngle = startAngle,
                sweepAngle = anglePerSegment,
                useCenter = true,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, size.height)
            )

            // Draw division lines between segments
            val angle = Math.toRadians((startAngle).toDouble())
            val xEnd = centerX + (radius * cos(angle)).toFloat()
            val yEnd = centerY + (radius * sin(angle)).toFloat()

            // Enhanced division lines with 3D effect
            // Shadow line
            drawLine(
                color = Color.Black.copy(alpha = 0.4f),
                start = Offset(centerX + 2f, centerY + 2f),
                end = Offset(xEnd + 2f, yEnd + 2f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )

            // Main division line with gradient
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.9f),
                        Color.White.copy(alpha = 0.5f)
                    ),
                    start = Offset(centerX, centerY),
                    end = Offset(xEnd, yEnd)
                ),
                start = Offset(centerX, centerY),
                end = Offset(xEnd, yEnd),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )

            // Add "pick" indicators at segment borders
            val pickSize = radius * 0.05f
            val pickDistanceFromEdge = radius * 1.05f
            val pickX = centerX + (pickDistanceFromEdge * cos(angle)).toFloat()
            val pickY = centerY + (pickDistanceFromEdge * sin(angle)).toFloat()

            // Draw 3D pick with shadow
            drawCircle(
                color = Color.Black.copy(alpha = 0.5f),
                radius = pickSize + 1.5f,
                center = Offset(pickX + 1.5f, pickY + 1.5f)
            )

            // Draw metallic pick base
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFAAAAAA),
                        Color(0xFF888888),
                        Color(0xFF666666)
                    ),
                    center = Offset(pickX, pickY),
                    radius = pickSize * 1.2f
                ),
                radius = pickSize,
                center = Offset(pickX, pickY)
            )

            // Draw pick highlight
            drawCircle(
                color = Color.White.copy(alpha = pickGlowAlpha.value),
                radius = pickSize * 0.6f,
                center = Offset(pickX - pickSize * 0.3f, pickY - pickSize * 0.3f)
            )

            if (isSpinning) {
                drawArc(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = Offset(shimmerTranslate.value - 1000f, shimmerTranslate.value - 1000f),
                        end = Offset(shimmerTranslate.value, shimmerTranslate.value)
                    ),
                    startAngle = startAngle,
                    sweepAngle = anglePerSegment,
                    useCenter = true,
                    alpha = 0.3f
                )
            }

            val patternAngle = startAngle + 5
            val patternSweep = anglePerSegment - 10

            drawArc(
                color = if (i % 2 == 0) Color.White.copy(alpha = 0.07f) else Color.Black.copy(alpha = 0.07f),
                startAngle = patternAngle,
                sweepAngle = patternSweep,
                useCenter = true,
                topLeft = Offset(centerX - radius * 0.8f, centerY - radius * 0.8f),
                size = Size(radius * 1.6f, radius * 1.6f)
            )
        }

        val innerRadius = radius * 0.12f

        drawCircle(
            color = Color.Black.copy(alpha = 0.4f),
            radius = innerRadius + 4f,
            center = Offset(centerX + 2.5f, centerY + 2.5f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF303F9F),
                    Color(0xFF1A237E),
                    Color(0xFF0D193D)
                ),
                center = Offset(centerX, centerY),
                radius = innerRadius * 1.5f
            ),
            radius = innerRadius + 3f,
            center = Offset(centerX, centerY)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF3949AB),
                    Color(0xFF1A237E),
                    Color(0xFF0D193D)
                ),
                center = Offset(centerX, centerY),
                radius = innerRadius * 2f
            ),
            radius = innerRadius,
            center = Offset(centerX, centerY)
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.4f),
            radius = innerRadius * 0.6f,
            center = Offset(centerX - innerRadius * 0.3f, centerY - innerRadius * 0.3f)
        )
    }

    Box(modifier = modifier) {
        for (i in 0 until segments) {
            val anglePerSegment = 360f / segments
            val midAngle = i * anglePerSegment + (anglePerSegment / 2)
            val radians = Math.toRadians(midAngle.toDouble())
            val distanceFromCenter = 0.85f
            val radius = min(120.dp.value, 120.dp.value) * distanceFromCenter
            val xPos = cos(radians).toFloat() * radius
            val yPos = sin(radians).toFloat() * radius

            Box(
                modifier = Modifier
                    .offset(
                        x = xPos.dp,
                        y = yPos.dp
                    )
                    .align(Alignment.Center)
                    .graphicsLayer {
                        rotationZ = midAngle + 90
                        if (prizes[i].isPremium && !isSpinning) {
                            scaleX = pulseScale.value
                            scaleY = pulseScale.value
                        }
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .width(65.dp)
                        .padding(horizontal = 2.dp)
                ) {
                    Image(
                        painter = painterResource(id = prizes[i].imageResId),
                        contentDescription = stringResource(R.string.prize_desc, prizes[i].text),
                        modifier = Modifier
                            .size(65.dp)
                            .padding(bottom = 2.dp)
                            .graphicsLayer {
                                if (isSpinning) {
                                    rotationZ = shimmerTranslate.value * 0.02f
                                }
                            }
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        // Display Arabic text based on prize type
                        val displayText = when (prizes[i].text) {
                            "1 coin" -> stringResource(R.string.coin_1)
                            "15 coins" -> stringResource(R.string.coin_15)
                            "20 coins" -> stringResource(R.string.coin_20)
                            "25 coins" -> stringResource(R.string.coin_25)
                            "50 coins" -> stringResource(R.string.coin_50)
                            "100 coins" -> stringResource(R.string.coin_100)
                            "Watch Video" -> stringResource(R.string.watch_video)
                            "Delete Two Letters" -> stringResource(R.string.delete_two_letters)
                            "Hint" -> stringResource(R.string.hint)
                            "Heart" -> stringResource(R.string.heart)
                            else -> prizes[i].text // fallback to original text
                        }

                        Text(
                            text = displayText,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = if (displayText.length > 6) 9.sp else 16.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = Color.White,
                                drawStyle = Stroke(
                                    width = 2f,
                                    join = StrokeJoin.Round
                                )
                            )
                        )

                        Text(
                            text = displayText,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = if (displayText.length > 6) 9.sp else 16.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SpinWheelScreenPreview() {
    MaterialTheme {
        SpinWheelScreen(
            onOpenStore = {},
            onOpenProfile = {},
            onImageClick = {},
            navController = rememberNavController()
        )
    }
}