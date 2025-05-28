package com.pyramid.questions.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.navigation.Route
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.sin
import kotlin.random.Random
import com.pyramid.questions.R

// Enhanced 3D Progress Bar with depth and lighting effects
@Composable
fun SplashProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 12.dp,
    animationDuration: Int = 1000
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(animationDuration, easing = EaseOutCubic),
        label = "progress"
    )

    // Colors matching the button style
    val backgroundColors = listOf(
        Color(0xFFE6E6E6),
        Color(0xFFCCCCCC),
        Color(0xFFB3B3B3)
    )

    val progressColors = listOf(
        Color(0xFFFFF4C4),    // Top highlight
        Color(0xFFFFE066),    // Main gradient start
        Color(0xFFFFCC00),    // Main gradient end
        Color(0xFFCC9900)     // Bottom shadow
    )

    val borderHighlight = Color(0xFFFFF4C4)
    val borderShadow = Color(0xFF996600)
    val dropShadowColor = Color(0x30000000)

    Box(
        modifier = modifier
            .height(height)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(height / 2),
                spotColor = dropShadowColor,
                ambientColor = dropShadowColor
            )
    ) {
        // Background track
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(height / 2))
                .background(
                    brush = Brush.verticalGradient(
                        colors = backgroundColors
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFF999999),
                    shape = RoundedCornerShape(height / 2)
                )
        )

        // Progress fill
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .clip(RoundedCornerShape(height / 2))
                .background(
                    brush = Brush.verticalGradient(
                        colors = progressColors
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            borderHighlight.copy(alpha = 0.8f),
                            borderShadow.copy(alpha = 0.6f)
                        )
                    ),
                    shape = RoundedCornerShape(height / 2)
                )
        ) {
            // Inner highlight for 3D effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(height / 2))
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f),
                                Color.Transparent
                            ),
                            center = Offset(0.3f, 0.2f),
                            radius = height.value * 2
                        )
                    )
            )
        }
    }
}

@Composable
fun CircularYellowProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 6.dp,
    animationDuration: Int = 1000
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(animationDuration, easing = EaseOutCubic),
        label = "circular_progress"
    )

    val progressColors = listOf(
        Color(0xFFFFE066),
        Color(0xFFFFCC00),
        Color(0xFFCC9900)
    )

    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 4.dp,
                shape = CircleShape,
                spotColor = Color(0x30000000)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(size)
                .padding(strokeWidth / 2)
        ) {
            val center = Offset(this.size.width / 2, this.size.height / 2)
            val radius = (this.size.width - strokeWidth.toPx()) / 2

            // Background circle
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE6E6E6),
                        Color(0xFFCCCCCC)
                    ),
                    center = center,
                    radius = radius
                ),
                center = center,
                radius = radius,
                style = Stroke(strokeWidth.toPx())
            )

            // Progress arc
            if (animatedProgress > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = progressColors,
                        center = center
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}

// مثال على الاستخدام
@Composable
fun ProgressExample() {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            progress = (progress + 0.01f) % 1.1f
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Linear Progress")
        SplashProgressBar(
            progress = progress,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Thicker Progress")
        SplashProgressBar(
            progress = progress,
            height = 20.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Circular Progress")
        CircularYellowProgress(
            progress = progress,
            size = 64.dp,
            strokeWidth = 8.dp
        )

        Text("Progress: ${(progress * 100).toInt()}%")
    }
}

// Enhanced 3D Box Component with better lighting
@Composable
fun Enhanced3DBox(
    content: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    containerGradient: Brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF4FC3F7),
            Color(0xFF29B6F6),
            Color(0xFF1976D2)
        )
    ),
    shadowColor: Color = Color.Black.copy(alpha = 0.4f),
    borderColor: Color = Color.White.copy(alpha = 0.3f),
    elevation: Float = 12f
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
    ) {
        // Main container with enhanced gradient
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4FC3F7).copy(alpha = 0.9f),
                            Color(0xFF29B6F6).copy(alpha = 0.7f),
                            Color(0xFF1976D2).copy(alpha = 0.8f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        // Inner highlight
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(2.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
        )

        // Content container
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(3.dp)
                .background(
                    brush = containerGradient,
                    shape = RoundedCornerShape(13.dp)
                ),
            content = content
        )

        // Gloss effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        center = Offset(0.3f, 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )
    }
}

// Enhanced Particle System
@Composable
fun EnhancedParticleBackground(
    particleCount: Int = 30,
    modifier: Modifier = Modifier
) {
    val particles = remember {
        (0 until particleCount).map {
            ParticleData(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 6f + 2f,
                speed = Random.nextFloat() * 0.3f + 0.1f,
                alpha = Random.nextFloat() * 0.6f + 0.2f,
                color = listOf(
                    Color(0xFFFFD700),
                    Color(0xFFFF8C00),
                    Color(0xFF4FC3F7),
                    Color.White
                ).random()
            )
        }.toMutableList()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particle_animation")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_progress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val currentY = (particle.y + animationProgress * particle.speed) % 1f
            val currentX = particle.x + sin(currentY * 8f) * 0.03f
            val twinkle = sin(animationProgress * 10f + particle.x * 20f) * 0.3f + 0.7f

            // Draw particle with glow effect
            drawCircle(
                color = particle.color.copy(alpha = particle.alpha * (1f - currentY) * twinkle * 0.3f),
                radius = particle.size * 3f,
                center = Offset(
                    x = currentX * size.width,
                    y = currentY * size.height
                )
            )

            drawCircle(
                color = particle.color.copy(alpha = particle.alpha * (1f - currentY) * twinkle),
                radius = particle.size,
                center = Offset(
                    x = currentX * size.width,
                    y = currentY * size.height
                )
            )
        }
    }
}

data class ParticleData(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val alpha: Float,
    val color: Color = Color.White
)

// Enhanced Floating Letter with game-style effects
@Composable
fun EnhancedFloatingLetter(
    letter: String,
    modifier: Modifier = Modifier,
    fontSize: Float = 22f,
    color: Color = Color.White,
    fontFamily: FontFamily = FontFamily.Default
) {
    val infiniteTransition = rememberInfiniteTransition(label = "letter_animation")
    val letterGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "letter_glow"
    )

    Enhanced3DBox(
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Glow effect
                Text(
                    text = letter,
                    fontSize = (fontSize + 2f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700).copy(alpha = letterGlow * 0.3f),
                    fontFamily = fontFamily
                )

                // Main text
                Text(
                    text = letter,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    fontFamily = fontFamily
                )
            }
        },
        modifier = modifier,
        containerGradient = Brush.radialGradient(
            colors = listOf(
                Color(0xFF3B6CB1).copy(alpha = 0.6f),
                Color(0xFF5D8CCB).copy(alpha = 0.4f),
                Color(0xFF1976D2).copy(alpha = 0.3f)
            )
        ),
        shadowColor = Color(0xFF1565C0).copy(alpha = 0.4f),
        borderColor = Color.White.copy(alpha = 0.2f),
        elevation = 8f
    )
}

// Enhanced Background Floating Elements
@Composable
fun EnhancedFloatingElements(
    floatingOffset: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        val gameLetters = listOf("G", "A", "M", "E", "W", "O", "R", "D", "S", "F", "U", "N")

        gameLetters.forEachIndexed { index, letter ->
            val xPosition = (20 + (index * 35)) % 380
            val yPosition = 60 + (index % 5) * 150
            val scaleVariation = 0.6f + (index % 4) * 0.15f
            val floatDirection = if (index % 2 == 0) 1f else -1f
            val rotationSpeed = if (index % 3 == 0) 1f else -1f

            EnhancedFloatingLetter(
                letter = letter,
                modifier = Modifier
                    .offset(
                        x = xPosition.dp,
                        y = (yPosition + floatingOffset * floatDirection).dp
                    )
                    .scale(scaleVariation)
                    .rotate(floatingOffset * rotationSpeed * 0.5f)
                    .size(40.dp),
                fontSize = 18f,
                fontFamily = FontFamily(Font(R.font.fon2))
            )
        }
    }
}

// Main Enhanced Splash Screen
@Composable
fun SplashScreen(
    navController: NavHostController,
    onNavigateToHome: () -> Unit = {},
    onSplashFinished: () -> Unit,
    navigateToMain: () -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }

    val arabicFont = FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.fon2
    }))

    // Animation states
    var startAnimation by remember { mutableStateOf(false) }
    var showProgressBar by remember { mutableStateOf(false) }
    var progressValue by remember { mutableStateOf(0f) }

    // Enhanced logo animations
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )

    val logoRotation by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -720f,
        animationSpec = tween(1500, easing = FastOutSlowInEasing),
        label = "logo_rotation"
    )

    // Enhanced text animations
    val titleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1200, delayMillis = 800),
        label = "title_alpha"
    )

    val titleScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "title_scale"
    )

    val subtitleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000, delayMillis = 1200),
        label = "subtitle_alpha"
    )

    // Enhanced floating animation
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating_offset"
    )

    // Enhanced pulsing effect for title
    val titlePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "title_pulse"
    )

    // Launch animations and navigation
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        showProgressBar = true

        // Enhanced loading simulation
        val loadingSteps = listOf(8, 15, 28, 42, 55, 68, 75, 82, 90, 95, 100)
        for (step in loadingSteps) {
            progressValue = step.toFloat()
            delay(if (step < 70) 250 else 400)
        }

        delay(1000)
        onNavigateToHome()
        navController.navigate(Route.HOME) {
            popUpTo(Route.SPLASH) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0D47A1),
                        Color(0xFF1565C0),
                        Color(0xFF1976D2),
                        Color(0xFF1E88E5),
                        Color(0xFF0D47A1)
                    ),
                    center = Offset(0.5f, 0.3f)
                )
            )
    ) {
        // Enhanced animated background particles
        EnhancedParticleBackground(particleCount = 35)

        // Enhanced floating letters
        EnhancedFloatingElements(
            floatingOffset = floatingOffset,
            modifier = Modifier.fillMaxSize()
        )

        // Main content with enhanced styling
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Enhanced title section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Game icon or logo placeholder
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(logoScale * titlePulse)
                        .rotate(logoRotation)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFD700),
                                    Color(0xFFFF8C00)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎮",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = arabicFont,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = titleAlpha
                            scaleX = titleScale
                            scaleY = titleScale
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Challenge Your Mind & Have Fun!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = arabicFont,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { alpha = subtitleAlpha }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Enhanced loading section
            AnimatedVisibility(
                visible = showProgressBar,
                enter = fadeIn(
                    animationSpec = tween(800)
                ) + slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(800)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.loading),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = arabicFont,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    SplashProgressBar(
                        progress = progressValue / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        height = 16.dp,
                        animationDuration = 500
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${progressValue.toInt()}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = arabicFont,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Enhanced version info
            Text(
                text = stringResource(R.string.app_version_info),
                fontSize = 12.sp,
                fontFamily = arabicFont,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        navController = rememberNavController(),
        navigateToMain = {},
        onNavigateToHome = {},
        onSplashFinished = {}
    )
}