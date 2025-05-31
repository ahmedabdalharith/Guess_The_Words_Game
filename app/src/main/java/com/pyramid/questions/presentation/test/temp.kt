package com.pyramid.questions.presentation.test
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.ColorUtils.colorToHSL
import com.pyramid.questions.R


@Composable
fun UrbanPortraitAppIcon(
    image: Int = R.drawable.img1,
    size: Int = 120,
    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Ltr,
) {
    // Base rounded blue background
    Box(
        modifier = Modifier
            .size((size + 16).dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        // Implementation using the ImageBox3D style
        ImageBox3D(
            image = image,
            size = size - 12,
            isRtl = isRtl,
            shadowColor = Color(0x88000000)
        )
    }
}

@Composable
fun ImageBox3D(
    image: Int = R.drawable.img1,
    size: Int = 100,
    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl,
    shadowColor: Color = Color(0x88000000)
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        // Shadow box behind the main icon
        Box(
            modifier = Modifier
                .size(size.dp)
                .offset(
                    x = if (isRtl) (-6).dp else 6.dp,
                    y = 6.dp
                )
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFC0C0C0))
        )

        // Shadow effect
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFAAAAAA)) // Metallic silver color instead of gold
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp)
                )
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){}

        // Main frame
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp),
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF888888),
                            Color(0xFFD8D8D8),
                            Color(0xFF888888)
                        )
                    )
                ), // Metallic silver effect
            contentAlignment = Alignment.Center
        ) {
            // Light reflection in corner
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .clip(
                        GenericShape { shapeSize, _ ->
                            if (isRtl) {
                                moveTo(shapeSize.width, 0f)
                                lineTo(shapeSize.width * 0.5f, 0f)
                                lineTo(shapeSize.width, shapeSize.height * 0.5f)
                                close()
                            } else {
                                moveTo(0f, 0f)
                                lineTo(shapeSize.width * 0.5f, 0f)
                                lineTo(0f, shapeSize.height * 0.5f)
                                close()
                            }
                        }
                    )
                    .background(Color.White.copy(alpha = 0.3f))
            )

            // Image card
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box {
                    // The portrait image
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "Portrait of man in urban setting",
                        modifier = Modifier
                            .fillMaxSize()
                            .size(size.dp)
                            .padding(4.dp)
                            .clip(
                                RoundedCornerShape(10.dp)
                            )
                            .zIndex(1f),
                        contentScale = ContentScale.Crop
                    )

                    // Subtle overlay for somber mood
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x00000000),
                                        Color(0x33000000)
                                    )
                                )
                            )
                            .zIndex(2f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUrbanPortraitAppIcon() {
    Surface(
        modifier = Modifier
            .size(200.dp)
            .background(Color.White),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            UrbanPortraitAppIcon()
        }
    }
}


@Composable
fun RealisticAppIcon(
    image: Int = R.drawable.img1,
    size: Int = 120,
    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl
) {
    // Main container
    Box(
        modifier = Modifier
            .size(size.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Blue background with rounded rectangle shape
        Box(
            modifier = Modifier
                .size((size).dp)
                .offset(
                    x = if (isRtl) (-3).dp else 3.dp,
                    y = 3.dp
                )
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF2A5298))
                .shadow(8.dp, RoundedCornerShape(18.dp))
        )

        // Main icon container with metallic silver frame
        Box(
            modifier = Modifier
                .size((size - 8).dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFB8B8B8),
                            Color(0xFFE6E6E6),
                            Color(0xFFAAAAAA)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    color = Color.White.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Metallic highlight effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        GenericShape { shapeSize, _ ->
                            if (isRtl) {
                                moveTo(shapeSize.width, 0f)
                                lineTo(shapeSize.width * 0.6f, 0f)
                                lineTo(shapeSize.width, shapeSize.height * 0.6f)
                                close()
                            } else {
                                moveTo(0f, 0f)
                                lineTo(shapeSize.width * 0.6f, 0f)
                                lineTo(0f, shapeSize.height * 0.6f)
                                close()
                            }
                        }
                    )
                    .background(Color.White.copy(alpha = 0.3f))
            )

            // Inner photo container
            Card(
                modifier = Modifier
                    .size((size - 16).dp)
                    .padding(4.dp)
                    .zIndex(1f),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                // Photo of the man with urban background
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "Portrait photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Subtle highlight overlay
        Box(
            modifier = Modifier
                .size((size - 8).dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Transparent)
                .border(
                    width = 0.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.7f),
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRealisticAppIcon() {
    Surface(
        modifier = Modifier
            .size(200.dp)
            .background(Color.White),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            RealisticAppIcon()
        }
    }
}
@Composable
fun EnhancedImageBox3D(
    image: Int = R.drawable.img1,
    size: Int = 100,
    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl,
    frameColor: Color = Color(0xFFD8D8D8),
    shadowDepth: Int = 7
) {
    val hsl = FloatArray(3)
    androidx.core.graphics.ColorUtils.colorToHSL(frameColor.toArgb(), hsl)

    hsl[1] = hsl[1] * 0.7f
    hsl[2] = hsl[2] * 0.4f

    val shadowBaseColor = Color(androidx.core.graphics.ColorUtils.HSLToColor(hsl))
    val shadowColor = shadowBaseColor
        .copy(alpha = 0.6f)
        .compositeOver(frameColor.copy(alpha = 0.15f))

    Box(
        modifier = Modifier
            .size(size.dp)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .offset(
                    x = if (isRtl) (-shadowDepth).dp else shadowDepth.dp,
                    y = shadowDepth.dp
                )
                .clip(RoundedCornerShape(12.dp))
                .background(shadowColor)
                .blur(5.dp)
                .drawBehind {
                    val path = Path().apply {
                        moveTo(0f, size * 0.2f)
                        lineTo(size * 0.3f, size * 0.5f)
                        lineTo(0f, size * 0.8f)
                        close()
                    }

                    drawPath(
                        path = path,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                frameColor.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(size * 0.5f, size * 0.5f)
                        )
                    )
                }
        )

        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            frameColor.copy(brightness = 0.9f),
                            frameColor.copy(brightness = 0.75f),
                            frameColor.copy(brightness = 0.85f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.toFloat(), size.toFloat())
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.9f),
                            frameColor.copy(alpha = 0.7f),
                            Color.White.copy(alpha = 0.8f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.toFloat(), size.toFloat())
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .shadow(shadowDepth.dp, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){}

        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.9f),
                            frameColor.copy(brightness = 1.2f),
                            Color.White.copy(alpha = 0.7f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.toFloat(), size.toFloat())
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            frameColor.copy(brightness = 1.1f),
                            frameColor,
                            frameColor.copy(brightness = 0.9f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.toFloat(), size.toFloat())
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .clip(
                        GenericShape { shapeSize, _ ->
                            if (isRtl) {
                                moveTo(shapeSize.width, 0f)
                                lineTo(shapeSize.width * 0.7f, 0f)
                                quadraticTo(
                                    shapeSize.width * 0.85f,
                                    shapeSize.height * 0.15f,
                                    shapeSize.width,
                                    shapeSize.height * 0.7f
                                )
                                close()
                            } else {
                                moveTo(0f, 0f)
                                lineTo(shapeSize.width * 0.7f, 0f)
                                quadraticTo(
                                    shapeSize.width * 0.15f,
                                    shapeSize.height * 0.15f,
                                    0f,
                                    shapeSize.height * 0.7f
                                )
                                close()
                            }
                        }
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.8f),
                                Color.White.copy(alpha = 0.4f),
                                Color.White.copy(alpha = 0.0f)
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .size(size.dp * 0.65f)
                    .offset(
                        x = if (isRtl) (-size * 0.05f).dp else (size * 0.05f).dp,
                        y = (-size * 0.05f).dp
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.5f),
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
                            ),
                            radius = size * 1.2f
                        )
                    )
                    .zIndex(3f)
            )

            Box(
                modifier = Modifier
                    .size(size.dp * 0.25f)
                    .offset(
                        x = if (isRtl) (size * 0.25f).dp else (-size * 0.25f).dp,
                        y = (size * 0.2f).dp
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.6f),
                                Color.Transparent
                            )
                        )
                    )
                    .zIndex(3f)
            )

            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.padding(6.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "Portrait image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .zIndex(1f),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x00000000),
                                        Color(0x22000000),
                                        Color(0x33000000)
                                    )
                                )
                            )
                            .zIndex(2f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                GenericShape { shapeSize, _ ->
                                    moveTo(0f, 0f)
                                    lineTo(shapeSize.width * 0.4f, 0f)
                                    quadraticTo(
                                        shapeSize.width * 0.2f,
                                        shapeSize.height * 0.2f,
                                        0f,
                                        shapeSize.height * 0.4f
                                    )
                                    close()
                                }
                            )
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.5f),
                                        Color.White.copy(alpha = 0.3f),
                                        Color.White.copy(alpha = 0f)
                                    )
                                )
                            )
                            .zIndex(3f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        GenericShape { shapeSize, _ ->
                            val lineWidth = shapeSize.width * 0.08f
                            moveTo(0f, shapeSize.height * 0.3f)
                            cubicTo(
                                shapeSize.width * 0.3f, shapeSize.height * 0.4f,
                                shapeSize.width * 0.7f, shapeSize.height * 0.7f,
                                shapeSize.width, shapeSize.height * 0.8f
                            )
                            lineTo(shapeSize.width, shapeSize.height * 0.8f + lineWidth)
                            cubicTo(
                                shapeSize.width * 0.7f, shapeSize.height * 0.7f + lineWidth,
                                shapeSize.width * 0.3f, shapeSize.height * 0.4f + lineWidth,
                                0f, shapeSize.height * 0.3f + lineWidth
                            )
                            close()
                        }
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0f),
                                Color.White.copy(alpha = 0.8f),
                                Color.White.copy(alpha = 0f)
                            ),
                            start = Offset(size * 0.3f, size * 0.3f),
                            end = Offset(size * 0.7f, size * 0.7f)
                        )
                    )
                    .zIndex(4f)
            )

            Box(
                modifier = Modifier
                    .size(size.dp * 0.1f)
                    .offset(
                        x = if (isRtl) (-size * 0.3f).dp else (size * 0.3f).dp,
                        y = (size * 0.35f).dp
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.9f),
                                Color.White.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
                    .zIndex(5f)
            )
        }
    }
}

private fun Color.copy(brightness: Float): Color {
    val hsl = FloatArray(3)
    colorToHSL(
        this.toArgb(),
        hsl
    )
    hsl[2] = hsl[2] * brightness
    return Color(ColorUtils.HSLToColor(hsl))
}

@Preview
@Composable
fun DemoUsage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EnhancedImageBox3D(
            image = R.drawable.img1,
            size = 150
        )

        EnhancedImageBox3D(
            image = R.drawable.img1,
            size = 150,
            frameColor = Color(0xFFDAA520),
            shadowDepth = 9
        )
        EnhancedImageBox3D(
            image = R.drawable.img1,
            size = 150,
            frameColor = Color(0xFFB87333),
            shadowDepth = 6
        )

        EnhancedImageBox3D(
            image = R.drawable.img1,
            size = 150,
            frameColor = Color(0xFF1A237E),
            shadowDepth = 8
        )

        EnhancedImageBox3D(
            image = R.drawable.img1,
            size = 150,
            frameColor = Color(0xFF2E7D32),
            shadowDepth = 7
        )
    }
}