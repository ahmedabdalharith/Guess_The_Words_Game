//import android.content.Context
//import android.os.Build
//import android.os.VibrationEffect
//import android.os.Vibrator
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.CubicBezierEasing
//import androidx.compose.animation.core.EaseInOutQuad
//import androidx.compose.animation.core.LinearEasing
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.spring
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.core.updateTransition
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.scaleIn
//import androidx.compose.animation.scaleOut
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.Shadow
//import androidx.compose.ui.graphics.drawscope.Fill
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.drawscope.translate
//import androidx.compose.ui.graphics.drawscope.withTransform
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDirection
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.pyramid.questions.R//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlin.math.PI
//import kotlin.math.cos
//import kotlin.math.floor
//import kotlin.math.sin
//
//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun SpinWheelScreen(
//    onBackPressed: () -> Unit = {},
//    currentCoins: Int = 425,
//    currentStars: Int = 8,
//    onAddCoins: () -> Unit = {}
//) {
//    val scope = rememberCoroutineScope()
//    var isSpinning by remember { mutableStateOf(false) }
//    var targetRotation by remember { mutableFloatStateOf(0f) }
//    val rotation = remember { Animatable(0f) }
//
//    // State for celebration animation
//    var showPrizeDialog by remember { mutableStateOf(false) }
//    var currentPrize by remember { mutableStateOf("") }
//    var isConfettiVisible by remember { mutableStateOf(false) }
//
//    // State for lights animation - optimized with fewer animatables
//    val lightAnimator = rememberInfiniteTransition(label = "lights").animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(if (isSpinning) 400 else 1200),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "light_animation"
//    )
//
//    // List of prizes with emoji icons
//    val prizes = listOf("10 🪙", "50 🪙", "100 🪙", "❤️", "200 🪙", "500 🪙", "25 🪙", "🌟", "75 🪙", "150 🪙")
//
//    // Colors for the bulbs - memory efficient
//    val bulbColors = remember {
//        listOf(
//            Color(0xFFFFD700), // Gold
//            Color(0xFFE0E0E0), // Silver
//            Color(0xFFFFB74D), // Light Orange
//            Color(0xFFFFD54F)  // Light Yellow
//        )
//    }
//
//    // Sound and vibration effects
//    val context = LocalContext.current
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF1E3C8A),
//                        Color(0xFF3871E0),
//                        Color(0xFF0C1B45)
//                    )
//                )
//            )
//    ) {
//        // Game Header
//        GameHeader(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 16.dp)
//                .align(Alignment.TopCenter),
//            currentCoins = currentCoins,
//            currentStars = currentStars,
//            onBackPressed = onBackPressed,
//            onAddCoins = onAddCoins
//        )
//
//        // Wheel
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.Center),
//            contentAlignment = Alignment.Center
//        ) {
//            // Wheel Background with animated lights
//            WheelBackground(
//                modifier = Modifier
//                    .size(340.dp)
//                    .align(Alignment.Center),
//                isSpinning = isSpinning,
//                lightAnimatorValue = lightAnimator.value,
//                bulbColors = bulbColors
//            )
//
//            // Spinning Wheel
//            WheelContent(
//                modifier = Modifier
//                    .size(300.dp)
//                    .rotate(rotation.value),
//                segments = prizes.size,
//                prizes = prizes,
//                isSpinning = isSpinning
//            )
//
//            // Center Button
//            CenterSpinButton(
//                modifier = Modifier
//                    .size(90.dp)
//                    .align(Alignment.Center)
//                    .scale(if (isSpinning) 0.9f else 1f),
//                isSpinning = isSpinning
//            )
//
//            // Pointer
//            WheelPointer(
//                modifier = Modifier
//                    .width(40.dp)
//                    .height(60.dp)
//                    .align(Alignment.TopCenter)
//                    .rotate(180f)
//                    .offset(y = 26.dp),
//                isSpinning = isSpinning
//            )
//        }
//
//        // Bottom Spin Button
//        SpinButton(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 40.dp)
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 40.dp),
//            isSpinning = isSpinning,
//            onClick = {
//                if (!isSpinning) {
//                    isSpinning = true
//                    showPrizeDialog = false
//
//                    // Vibration feedback - optimized to check API level
//                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
//                    if (vibrator?.hasVibrator() == true) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
//                        } else {
//                            @Suppress("DEPRECATION")
//                            vibrator.vibrate(100)
//                        }
//                    }
//
//                    // Generate random spin between 5-8 full rotations + random ending position
//                    val spinCount = (5..8).random()
//                    val extraAngle = (0..359).random().toFloat()
//                    targetRotation = rotation.value + (spinCount * 360f) + extraAngle
//
//                    scope.launch {
//                        rotation.animateTo(
//                            targetValue = targetRotation,
//                            animationSpec = tween(
//                                durationMillis = 5000, // Reduced from 7000 for better UX
//                                easing = CubicBezierEasing(0.35f, 0.0f, 0.25f, 1.0f)
//                            )
//                        )
//
//                        // Determine the prize
//                        val normalizedRotation = (rotation.value % 360 + 360) % 360
//                        val segment = floor(normalizedRotation / (360f / prizes.size)).toInt()
//                        val prizeIndex = (prizes.size - segment - 1) % prizes.size
//
//                        // Show victory animation
//                        currentPrize = prizes[prizeIndex]
//                        isSpinning = false
//
//                        // Longer vibration for win
//                        if (vibrator?.hasVibrator() == true) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
//                            } else {
//                                @Suppress("DEPRECATION")
//                                vibrator.vibrate(300)
//                            }
//                        }
//
//                        delay(300) // Reduced delay for more responsive experience
//                        showPrizeDialog = true
//                        isConfettiVisible = true
//
//                        delay(2500) // Reduced from 3000 for better UX
//                        isConfettiVisible = false
//                    }
//                }
//            }
//        )
//
//        // Prize Dialog
//        AnimatedVisibility(
//            visible = showPrizeDialog,
//            enter = scaleIn(initialScale = 0.8f) + fadeIn(),
//            exit = scaleOut() + fadeOut()
//        ) {
//            PrizeDialog(
//                prize = currentPrize,
//                onDismiss = { showPrizeDialog = false }
//            )
//        }
//
//        // Confetti Animation - only rendered when visible for performance
//        if (isConfettiVisible) {
//            ConfettiAnimation(
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//    }
//}
//
//@Composable
//fun GameHeader(
//    modifier: Modifier = Modifier,
//    currentCoins: Int = 425,
//    currentStars: Int = 8,
//    onBackPressed: () -> Unit = {},
//    onAddCoins: () -> Unit = {}
//) {
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Back Button with improved styling
//        IconButton(
//            onClick = onBackPressed,
//            modifier = Modifier
//                .size(48.dp)
//                .shadow(4.dp, CircleShape)
//                .clip(CircleShape)
//                .background(Color(0xFF7FD1F1)),
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "رجوع",
//                tint = Color.Black,
//                modifier = Modifier.size(24.dp)
//            )
//        }
//
//        // Star Counter
//        Row(
//            modifier = Modifier
//                .clip(RoundedCornerShape(25.dp))
//                .background(Color(0xFF0E234D).copy(alpha = 0.8f))
//                .border(1.dp, Color(0xFF3871E0), RoundedCornerShape(25.dp))
//                .padding(horizontal = 12.dp, vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = Icons.Default.Star,
//                contentDescription = "النجوم",
//                tint = Color(0xFFFFD700),
//                modifier = Modifier.size(28.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = currentStars.toString(),
//                color = Color.White,
//                fontWeight = FontWeight.Bold,
//                fontSize = 22.sp
//            )
//        }
//
//        // Coins with Add Button
//        Row(
//            modifier = Modifier
//                .clip(RoundedCornerShape(25.dp))
//                .background(Color(0xFF0E234D).copy(alpha = 0.8f))
//                .border(1.dp, Color(0xFF3871E0), RoundedCornerShape(25.dp))
//                .padding(horizontal = 12.dp, vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(28.dp)
//                    .clip(CircleShape)
//                    .background(
//                        brush = Brush.radialGradient(
//                            colors = listOf(Color(0xFFFFE57F), Color(0xFFFFD700))
//                        )
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    painterResource(R.drawable.coin_ic),
//                    contentDescription = "عملات",
//                    tint = Color(0xFFAA8800),
//                    modifier = Modifier.size(18.dp)
//                )
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = currentCoins.toString(),
//                color = Color.White,
//                fontWeight = FontWeight.Bold,
//                fontSize = 22.sp
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            IconButton(
//                onClick = onAddCoins,
//                modifier = Modifier
//                    .size(28.dp)
//                    .shadow(2.dp, CircleShape)
//                    .clip(CircleShape)
//                    .background(Color(0xFF22CC44))
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "إضافة عملات",
//                    tint = Color.White,
//                    modifier = Modifier.size(16.dp)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun WheelBackground(
//    modifier: Modifier = Modifier,
//    isSpinning: Boolean,
//    lightAnimatorValue: Float,
//    bulbColors: List<Color>
//) {
//    Box(modifier = modifier) {
//        // Outer circle with animated lights - optimized drawing
//        Canvas(modifier = Modifier.matchParentSize()) {
//            // Draw outer shadow
//            drawCircle(
//                brush = Brush.radialGradient(
//                    colors = listOf(
//                        Color.Black.copy(alpha = 0.3f),
//                        Color.Transparent
//                    ),
//                    radius = size.minDimension * 0.6f
//                ),
//                radius = size.minDimension / 2 + 20f,
//            )
//
//            // Draw main circle (outline)
//            drawCircle(
//                brush = Brush.radialGradient(
//                    colors = listOf(
//                        Color(0xFFE0E0E0),
//                        Color(0xFFBBBBBB)
//                    )
//                ),
//                radius = size.minDimension / 2,
//                style = Stroke(width = 15f)
//            )
//
//            // Draw animated bulbs around the circle - optimized with fewer calculations
//            val centerX = size.width / 2
//            val centerY = size.height / 2
//            val radius = size.minDimension / 2
//            val bulbCount = 24
//
//            // Pre-calculate the phase for flickering to avoid duplicate calculations
//            val phase = floor(lightAnimatorValue * 3).toInt()
//
//            for (i in 0 until bulbCount) {
//                val angle = (i * (2 * PI / bulbCount))
//                val x = centerX + (radius * cos(angle)).toFloat()
//                val y = centerY + (radius * sin(angle)).toFloat()
//
//                // More efficient color selection
//                val bulbIndex = (i + phase) % bulbColors.size
//                val baseColor = bulbColors[bulbIndex]
//
//                // Simplified brightness calculation
//                val brightness = if ((i % 2 == 0) == (lightAnimatorValue > 0.5f)) 1.0f else 0.3f
//                val animatedColor = baseColor.copy(alpha = brightness)
//
//                // Draw bulb glow
//                drawCircle(
//                    color = animatedColor.copy(alpha = 0.4f),
//                    radius = 12f,
//                    center = Offset(x, y)
//                )
//
//                // Draw bulb
//                drawCircle(
//                    color = animatedColor,
//                    radius = 8f,
//                    center = Offset(x, y)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun WheelContent(
//    modifier: Modifier = Modifier,
//    segments: Int,
//    prizes: List<String>,
//    isSpinning: Boolean
//) {
//    // Optimized with memoized colors
//    val colors = remember {
//        listOf(
//            Color(0xFFFF5252), // Red
//            Color(0xFF536DFE), // Indigo
//            Color(0xFFFFEB3B), // Yellow
//            Color(0xFFFF9800), // Orange
//            Color(0xFF9C27B0), // Purple
//            Color(0xFF2196F3), // Blue
//            Color(0xFF4CAF50), // Green
//            Color(0xFF03A9F4), // Light Blue
//            Color(0xFFFF4081), // Pink
//            Color(0xFFAA00FF)  // Deep Purple
//        )
//    }
//
//    // Shimmer animation for sections when spinning - simplified for better performance
//    val shimmerTransition = rememberInfiniteTransition(label = "shimmer")
//    val shimmerTranslate = shimmerTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1000f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1200, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ),
//        label = "shimmer_translate"
//    )
//
//    val shimmerBrush = remember(shimmerTranslate.value) {
//        if (isSpinning) {
//            Brush.linearGradient(
//                colors = listOf(
//                    Color.White.copy(alpha = 0.2f),
//                    Color.White.copy(alpha = 0.4f),
//                    Color.White.copy(alpha = 0.2f)
//                ),
//                start = Offset(shimmerTranslate.value - 1000f, shimmerTranslate.value - 1000f),
//                end = Offset(shimmerTranslate.value, shimmerTranslate.value)
//            )
//        } else null
//    }
//
//    Canvas(modifier = modifier) {
//        val anglePerSegment = 360f / segments
//        val radius = size.minDimension / 2
//        val center = Offset(size.width / 2, size.height / 2)
//
//        // Draw wheel sections - optimized with fewer calculations in the loop
//        for (i in 0 until segments) {
//            val startAngle = i * anglePerSegment
//            val color = colors[i % colors.size]
//
//            // Draw segment
//            drawArc(
//                color = color,
//                startAngle = startAngle,
//                sweepAngle = anglePerSegment,
//                useCenter = true
//            )
//
//            // Add divider lines between segments
//            val angle = Math.toRadians(startAngle.toDouble())
//            val xEnd = center.x + (radius * cos(angle)).toFloat()
//            val yEnd = center.y + (radius * sin(angle)).toFloat()
//
//            drawLine(
//                color = Color.White,
//                start = center,
//                end = Offset(xEnd, yEnd),
//                strokeWidth = 3f
//            )
//
//            // Add shimmer effect when spinning
//            if (isSpinning && shimmerBrush != null) {
//                drawArc(
//                    brush = shimmerBrush,
//                    startAngle = startAngle,
//                    sweepAngle = anglePerSegment,
//                    useCenter = true
//                )
//            }
//        }
//
//        // Draw inner circle
//        drawCircle(
//            color = Color(0xFF0E234D),
//            radius = radius * 0.15f,
//            center = center
//        )
//    }
//
//    // Content of segments (numbers and icons)
//    Box(modifier = modifier) {
//        val anglePerSegment = 360f / segments
//
//        // Pre-calculate text style for better performance
//        val prizeTextStyle = TextStyle(
//            color = Color.White,
//            fontWeight = FontWeight.Bold,
//            fontSize = 18.sp,
//            textAlign = TextAlign.Center,
//            shadow = Shadow(
//                color = Color.Black.copy(alpha = 0.5f),
//                offset = Offset(1f, 1f),
//                blurRadius = 2f
//            )
//        )
//
//        for (i in 0 until segments) {
//            val angle = i * anglePerSegment + (anglePerSegment / 2)
//            val radians = Math.toRadians(angle.toDouble())
//            val radius = 120.dp
//
//            val offsetX = (radius.value * cos(radians)).dp
//            val offsetY = (radius.value * sin(radians)).dp
//
//            Box(
//                modifier = Modifier
//                    .offset(
//                        x = offsetX,
//                        y = offsetY
//                    )
//                    .align(Alignment.Center)
//                    .rotate(-angle + 90) // Adjust rotation for readability
//            ) {
//                // Prize text/icon
//                Text(
//                    text = prizes[i],
//                    style = prizeTextStyle
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun SpinButton(
//    modifier: Modifier = Modifier,
//    isSpinning: Boolean,
//    onClick: () -> Unit
//) {
//    // Animation for button when spinning - simplified
//    val buttonTransition = updateTransition(targetState = isSpinning, label = "buttonTransition")
//    val scale by buttonTransition.animateFloat(
//        transitionSpec = {
//            if (targetState) {
//                spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
//            } else {
//                spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMedium)
//            }
//        },
//        label = "buttonScale"
//    ) { spinning ->
//        if (spinning) 0.95f else 1f
//    }
//
//    // Pulsating animation when not spinning - more efficient with single animation
//    val pulseScale = if (!isSpinning) {
//        val pulseTransition = rememberInfiniteTransition(label = "pulse")
//        pulseTransition.animateFloat(
//            initialValue = 1f,
//            targetValue = 1.05f,
//            animationSpec = infiniteRepeatable(
//                animation = tween(1000, easing = EaseInOutQuad),
//                repeatMode = RepeatMode.Reverse
//            ),
//            label = "pulseScale"
//        ).value
//    } else scale
//
//    Button(
//        onClick = onClick,
//        modifier = modifier
//            .height(60.dp)
//            .scale(pulseScale)
//            .shadow(8.dp, RoundedCornerShape(18.dp)),
//        enabled = !isSpinning,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(0xFF22CC44),
//            disabledContainerColor = Color(0xFF119933)
//        ),
//        shape = RoundedCornerShape(18.dp)
//    ) {
//        Text(
//            text = if (isSpinning) "جاري الدوران..." else "يلف الآن!",
//            color = Color.White,
//            fontWeight = FontWeight.Bold,
//            fontSize = 24.sp,
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//@Composable
//fun PrizeDialog(prize: String, onDismiss: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black.copy(alpha = 0.5f))
//            .clickable { onDismiss() },
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            modifier = Modifier
//                .padding(24.dp)
//                .width(280.dp)
//                .clickable { /* Prevent click through */ },
//            shape = RoundedCornerShape(24.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = Color(0xFF0E234D)
//            ),
//            elevation = CardDefaults.cardElevation(
//                defaultElevation = 8.dp
//            )
//        ) {
//            Column(
//                modifier = Modifier.padding(24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = "مبروك!",
//                    style = TextStyle(
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 28.sp,
//                        textAlign = TextAlign.Center
//                    ),
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                Text(
//                    text = "لقد ربحت",
//                    style = TextStyle(
//                        color = Color.White,
//                        fontSize = 20.sp,
//                        textAlign = TextAlign.Center
//                    ),
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//
//                Text(
//                    text = prize,
//                    style = TextStyle(
//                        color = Color(0xFFFFD700),
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 36.sp,
//                        textAlign = TextAlign.Center,
//                        shadow = Shadow(
//                            color = Color(0xFFFFA000),
//                            offset = Offset(2f, 2f),
//                            blurRadius = 4f
//                        )
//                    ),
//                    modifier = Modifier.padding(vertical = 16.dp)
//                )
//
//                Button(
//                    onClick = { onDismiss() },
//                    modifier = Modifier
//                        .padding(top = 16.dp)
//                        .fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF22CC44)
//                    ),
//                    shape = RoundedCornerShape(16.dp)
//                ) {
//                    Text(
//                        text = "رائع!",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ConfettiAnimation(modifier: Modifier) {
//    // Optimized confetti with fewer particles
//    val particles = remember {
//        List(60) { // Reduced from 100 to 60 particles for better performance
//            ConfettiParticle(
//                x = (0..1000).random().toFloat(),
//                y = (-500..0).random().toFloat(),
//                velocity = (3..10).random().toFloat(),
//                angle = (-30..30).random().toFloat(),
//                rotation = (0..360).random().toFloat(),
//                rotationSpeed = (-5..5).random().toFloat(),
//                size = (10..25).random().toFloat(),
//                color = listOf(
//                    Color(0xFFFF5252), // Red
//                    Color(0xFF2196F3), // Blue
//                    Color(0xFFFFEB3B), // Yellow
//                    Color(0xFF4CAF50), // Green
//                    Color(0xFFFF9800), // Orange
//                    Color(0xFFE040FB)  // Pink
//                ).random()
//            )
//        }
//    }
//
//    // Animation for falling confetti - simplified
//    val animationProgress = rememberInfiniteTransition(label = "confetti").animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(3000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ),
//        label = "confetti_progress"
//    )
//
//    Canvas(modifier = modifier) {
//        particles.forEach { particle ->
//            val progress = animationProgress.value
//
//            // More efficient position calculation
//            val currentY = particle.y + particle.velocity * progress * size.height
//            val currentX = particle.x + sin(progress * 10 + particle.angle) * 20
//            val currentRotation = particle.rotation + particle.rotationSpeed * progress * 360
//
//            // Draw confetti particle with transform for better performance
//            withTransform({
//                translate(currentX, currentY)
//                rotate(currentRotation)
//            }) {
//                drawRect(
//                    color = particle.color,
//                    topLeft = Offset(-particle.size/2, -particle.size/2),
//                    size = Size(particle.size, particle.size/2)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CenterSpinButton(
//    modifier: Modifier = Modifier,
//    isSpinning: Boolean
//) {
//    // Animation for pulsating effect - simplified
//    val pulsate = rememberInfiniteTransition(label = "button_pulse").animateFloat(
//        initialValue = 1f,
//        targetValue = if (isSpinning) 1.1f else 1.05f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(700, easing = EaseInOutQuad),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "pulse_value"
//    )
//
//    Box(
//        modifier = modifier
//            .scale(pulsate.value)
//            .shadow(8.dp, CircleShape)
//            .clip(CircleShape)
//            .background(
//                brush = Brush.radialGradient(
//                    colors = listOf(
//                        Color(0xFFFFEE58),
//                        Color(0xFFFFD600)
//                    )
//                )
//            )
//            .border(
//                width = 4.dp,
//                brush = Brush.radialGradient(
//                    colors = listOf(
//                        Color(0xFFFFFFFF),
//                        Color(0xFFE0E0E0)
//                    )
//                ),
//                shape = CircleShape
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = if (isSpinning) "..." else "يلف",
//            color = Color(0xFF3E2723),
//            fontWeight = FontWeight.ExtraBold,
//            fontSize = 22.sp,
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//@Composable
//fun WheelPointer(
//    modifier: Modifier = Modifier,
//    isSpinning: Boolean
//) {
//    // Animation for pointer vibration when spinning
//    val vibration = if (isSpinning) {
//        rememberInfiniteTransition(label = "pointer_vibration").animateFloat(
//            initialValue = -2f,
//            targetValue = 2f,
//            animationSpec = infiniteRepeatable(
//                animation = tween(100, easing = LinearEasing),
//                repeatMode = RepeatMode.Reverse
//            ),
//            label = "vibration_value"
//        )
//    } else {
//        remember { mutableFloatStateOf(0f) }
//    }
//
//    Canvas(
//        modifier = modifier.offset(x = if (isSpinning) vibration.value.dp else 0.dp)
//    ) {
//        // Draw the pointer triangle
//        val path = Path().apply {
//            moveTo(size.width / 2, 0f)
//            lineTo(0f, size.height)
//            lineTo(size.width, size.height)
//            close()
//        }
//
//        // Shadow
//        translate(left = 2f, top = 2f) {
//            drawPath(
//                path = path,
//                color = Color.Black.copy(alpha = 0.3f),
//                style = Fill
//            )
//        }
//
//        // Main pointer
//        drawPath(
//            path = path,
//            brush = Brush.linearGradient(
//                colors = listOf(
//                    Color(0xFFE53935),
//                    Color(0xFFC62828)
//                )
//            ),
//            style = Fill
//        )
//
//        // Outline
//        drawPath(
//            path = path,
//            color = Color.White,
//            style = Stroke(width = 2f)
//        )
//    }
//}
//
//// Helper class for confetti
//private data class ConfettiParticle(
//    val x: Float,
//    var y: Float,
//    val velocity: Float,
//    val angle: Float,
//    val rotation: Float,
//    val rotationSpeed: Float,
//    val size: Float,
//    val color: Color
//)
//
//@Preview(showBackground = true)
//@Composable
//fun SpinWheelScreenPreview() {
//    SpinWheelScreen()
//}