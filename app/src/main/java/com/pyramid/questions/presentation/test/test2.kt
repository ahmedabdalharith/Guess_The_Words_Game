package com.pyramid.questions.presentation.test

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.pyramid.questions.AppColors
import com.pyramid.questions.R
//@Composable
//fun ImageBox3D(
//    image: Int = R.drawable.img1,
//    size: Int = 64,
//    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl,
//    shadowColor: Color
//) {
//    Box(
//        modifier = Modifier
//            .size(size.dp)
//            .padding(2.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Box(
//            modifier = Modifier
//                .size((size).dp)
//                .offset(
//                    x = if (isRtl) (-6).dp else 6.dp,
//                    y = 6.dp
//                )
//                .clip(RoundedCornerShape(12.dp))
//                .background(Color(0xFFC0C0C0))
//        )
//        Box(
//            modifier = Modifier
//                .size((size).dp)
//                .clip(RoundedCornerShape(12.dp))
//                .background(Color(0xFFFFD700)) // Fixed gold color
//                .border(
//                    width = 1.dp,
//                    color = Color.White.copy(alpha = 0.7f),
//                    shape = RoundedCornerShape(12.dp)
//                )
//                .shadow(8.dp, RoundedCornerShape(12.dp)),
//            contentAlignment = Alignment.Center
//        ){
//
//        }
//        Box(
//            modifier = Modifier
//                .size((size).dp)
//                .clip(RoundedCornerShape(12.dp))
//                .border(
//                    width = 2.dp,
//                    color = Color.White.copy(alpha = 0.7f),
//                    shape = RoundedCornerShape(12.dp),
//                )
//                .background(Color(0xFFFFD700)) // Fixed gold color
//        ) {
//            Box(
//                modifier = Modifier
//                    .size((size).dp)
//                    .clip(
//                        GenericShape { shapeSize, _ ->
//                            if (isRtl) {
//                                moveTo(shapeSize.width, 0f)
//                                lineTo(shapeSize.width * 0.5f, 0f)
//                                lineTo(shapeSize.width, shapeSize.height * 0.5f)
//                                close()
//                            } else {
//                                moveTo(0f, 0f)
//                                lineTo(shapeSize.width * 0.5f, 0f)
//                                lineTo(0f, shapeSize.height * 0.5f)
//                                close()
//                            }
//                        }
//                    )
//                    .background(Color.White.copy(alpha = 0.3f))
//            ){
//
//            }
//
//            Card(
//                elevation = CardDefaults.cardElevation((-4).dp)
//            ) {
//                Image(
//                    painter = painterResource(id = image),
//                    contentDescription = "هدية",
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .size((size).dp)
//                        .padding(4.dp)
//                        .clip(
//                            RoundedCornerShape(10.dp)
//                        )
//                        .zIndex(1f),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//    }
//}
@Preview
@Composable
fun GiftBox3DPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        ImageBox3D(shadowColor = Color(0xFF385170))
    }
}


@Composable
fun ComposableBox3D(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
    containerColor: Color = Color(0xFFD8D8D8),
    containerGradient: Brush? = null, // إضافة دعم التدرج اللوني
    borderColor: Color = Color.White.copy(alpha = 0.7f),
    shadowBoxColor: Color = Color(0xFFC0C0C0),
    shadowEffectColor: Color = Color(0xFFAAAAAA),
    highlightColor: Color = Color.White.copy(alpha = 0.3f),
    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl,
) {
    Box(
        modifier = modifier
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        // Shadow box behind the main component
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(
                    x = if (isRtl) (-6).dp else 6.dp,
                    y = 6.dp
                )
                .clip(RoundedCornerShape(12.dp))
                .background(shadowBoxColor)
        )

        // Shadow effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .background(shadowEffectColor)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){}

        // Main frame
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp),
                )
                .background(
                    brush = containerGradient ?: Brush.linearGradient(
                        colors = listOf(
                            containerColor.darken(0.3f),
                            containerColor,
                            containerColor.darken(0.3f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Light reflection in corner
            Box(
                modifier = Modifier
                    .matchParentSize()
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
                    .background(highlightColor)
            )

            // Content container with padding
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

// Extension function للحصول على لون داكن
fun Color.darken(factor: Float): Color {
    return Color(
        red = (red * (1f - factor)).coerceIn(0f, 1f),
        green = (green * (1f - factor)).coerceIn(0f, 1f),
        blue = (blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}



@Composable
fun CircleBox3D(
    image: Int = R.drawable.category_movies,
    size: Int = 64,
    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        // Back face (shadow)
        Box(
            modifier = Modifier
                .size((size).dp)
                .offset(
                    x = if (isRtl) (-2).dp else 2.dp,
                    y = 2.dp
                )
                .clip(CircleShape)
                .background(Color(0xFFC0C0C0))
        )

        // Main box (with fixed gold color)
        Box(
            modifier = Modifier
                .size((size).dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD700)) // Fixed gold color
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.7f),
                    shape = CircleShape
                )
                .shadow(8.dp, CircleShape),
            contentAlignment = Alignment.Center
        ){

        }

        // Static highlight effect
        Box(
            modifier = Modifier
                .size((size).dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.7f),
                    shape = CircleShape,
                )
                .background(Color(0xFFFFD700)) // Fixed gold color
        ) {
            // Highlight effect - simple radial highlight
            Box(
                modifier = Modifier
                    .size((size).dp)
                    .offset(
                        x = if (isRtl) (2).dp else -(2).dp,
                        y = -(2).dp
                    )
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f))
            )

            Card(
                elevation = CardDefaults.cardElevation((-4).dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size((size).dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "هدية",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .background(Color(0xFFFFD700))
                        .fillMaxSize()
                        .padding(4.dp)
                        .zIndex(1f)
                )
            }
        }
    }
}
@Composable
fun GameProgressIndicator3D(
    progress: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF0A4D8F),
    progressColor: Color = Color(0xFF4CAF50),
    badgeColor: Color = Color(0xFF7FBDFF),
    textColor: Color = Color.White,
    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl
) {
    // Parse the progress value (e.g., "8/10", "4/6", etc.)
    val progressParts = progress.split("/")
    val currentProgress = progressParts.getOrNull(0)?.trim()?.toFloatOrNull() ?: 0f
    val maxProgress = progressParts.getOrNull(1)?.trim()?.toFloatOrNull() ?: 1f

    // Calculate progress percentage
    val progressPercentage = if (maxProgress > 0) currentProgress / maxProgress else 0f

    Box(
        modifier = modifier
            .height(36.dp)
            .fillMaxWidth()
    ) {
        // Shadow (back face)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(26.dp)
                .offset(
                    x = if (isRtl) (-6).dp else 6.dp,
                    y = 6.dp
                )
                .align(if (isRtl) Alignment.TopEnd else Alignment.TopStart)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF385170))
        )

        // Main progress bar container
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(26.dp)
                .align(if (isRtl) Alignment.TopEnd else Alignment.TopStart)
                .clip(RoundedCornerShape(20.dp))
                .background(backgroundColor)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            // Progress fill with 3D effect
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressPercentage)
                    .height(26.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(progressColor)
            ) {
                // Highlight effect for progress bar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            GenericShape { shapeSize, _ ->
                                if (isRtl) {
                                    // RTL highlight
                                    moveTo(shapeSize.width, 0f)
                                    lineTo(shapeSize.width * 0.7f, 0f)
                                    lineTo(shapeSize.width, shapeSize.height * 0.7f)
                                    close()
                                } else {
                                    // LTR highlight
                                    moveTo(0f, 0f)
                                    lineTo(shapeSize.width * 0.3f, 0f)
                                    lineTo(0f, shapeSize.height * 0.7f)
                                    close()
                                }
                            }
                        )
                        .background(Color.White.copy(alpha = 0.3f))
                )
            }

            // Border highlight
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(20.dp)
                    )
            )

            // Content row (text and indicator)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Progress text with shadow for 3D effect
                Box {
                    // Text shadow
                    Text(
                        text = progress,
                        color = Color.Black.copy(alpha = 0.3f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.offset(x = 1.dp, y = 1.dp)
                    )

                    // Main text
                    Text(
                        text = progress,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // 3D Gift badge
        Box(
            modifier = Modifier
                .size(46.dp)
                .padding(bottom = 8.dp)
                .padding(end = 16.dp)
                .align(Alignment.CenterEnd)
                .offset(x = if (isRtl) (-16).dp else 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Badge shadow
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .offset(x = 3.dp, y = 3.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF385170))
            )

            // Main badge
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(badgeColor)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.7f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Highlight effect for badge
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            GenericShape { shapeSize, _ ->
                                // Top-left corner highlight
                                moveTo(0f, 0f)
                                lineTo(shapeSize.width * 0.5f, 0f)
                                lineTo(0f, shapeSize.height * 0.5f)
                                close()
                            }
                        )
                        .background(Color.White.copy(alpha = 0.3f))
                )

                // Gift icon with shadow for 3D effect
                Box {
                    // Icon shadow
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.gift_ic),
                        contentDescription = "هدية",
                        modifier = Modifier
                            .size(24.dp)
                            .offset(x = 1.dp, y = 1.dp)
                            .alpha(0.3f)
                    )

                    // Main icon
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.gift_ic),
                        contentDescription = "هدية",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun   GameProgressIndicator3DPre(){
        GameProgressIndicator3D("8/10")
}
@Composable
fun Text3D(
    text: String,
    fontSize: Int = 18,
    fontFamily: FontFamily? = null,
    textColor: Color = Color(0xFF663300),
    backgroundColor: Color = Color(0xFFFFD700),
    shadowColor: Color = Color(0xFFC0C0C0),
    cornerRadius: Int = 8,
    shadowOffsetX: Int = 4,
    shadowOffsetY: Int = 4,
    lightAnglePercentage: Float = 0.4f,
    borderWidth: Int = 1,
    borderColor: Color = Color.White.copy(alpha = 0.7f),
    paddingHorizontal: Int = 16,
    paddingVertical: Int = 8,
    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val boxModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        // الظل الخلفي
        Box(
            modifier = Modifier
                .offset(
                    x = if (isRtl) (-shadowOffsetX).dp else shadowOffsetX.dp,
                    y = shadowOffsetY.dp
                )
                .background(
                    color = shadowColor,
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .padding(horizontal = paddingHorizontal.dp, vertical = paddingVertical.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontFamily = fontFamily,
                color = Color.Transparent
            )
        }

        Box(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .border(
                    width = borderWidth.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .padding(horizontal = paddingHorizontal.dp, vertical = paddingVertical.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontFamily = fontFamily,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AnimatedText3D(
    text: String,
    fontSize: Int = 18,
    fontFamily: FontFamily? = null,
    textColor: Color = Color(0xFF663300),
    backgroundColor: Color = Color(0xFFFFD700),
    shadowColor: Color = Color(0xFFC0C0C0),
    isRtl: Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    glowColor: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "text_3d_animation")
    val shadowOffset by infiniteTransition.animateFloat(
        initialValue = 3f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shadow_animation"
    )

    var isClicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isClicked) 0.95f else 1f,
        animationSpec = tween(150),
        label = "click_scale"
    )

    val boxModifier = if (onClick != null) {
        modifier
            .scale(scale)
            .clickable {
                isClicked = true
                onClick()
                // إعادة الحجم بعد النقر
//                LaunchedEffect(isClicked) {
//                    delay(150)
//                    isClicked = false
//                }
            }
    } else {
        modifier
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        // الظل الخلفي المتحرك
        Box(
            modifier = Modifier
                .offset(
                    x = if (isRtl) (-shadowOffset).dp else shadowOffset.dp,
                    y = shadowOffset.dp
                )
                .background(
                    color = shadowColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontFamily = fontFamily,
                color = Color.Transparent
            )
        }

        // الخلفية الأساسية
        Box(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontFamily = fontFamily,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun Text3DExample() {
    val arabicFont = FontFamily.Default

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        AnimatedText3D(
            text = "نص متحرك ثلاثي الأبعاد",
            fontSize = 20,
            fontFamily = arabicFont,
            backgroundColor = Color(0xFFFF9800),
            glowColor = AppColors.SecondaryColor.copy(alpha = 0.6f)
        )
        Text3D(
            text = "نص مخصص بالكامل",
            fontSize = 18,
            fontFamily = arabicFont,
            textColor = Color.White,
            backgroundColor = Color(0xFF4CAF50),
            shadowColor = Color(0xFF1B5E20),
            cornerRadius = 16,
            shadowOffsetX = 5,
            shadowOffsetY = 5,
            lightAnglePercentage = 0.3f,
            borderWidth = 2,
            borderColor = Color.White.copy(alpha = 0.9f),
            paddingHorizontal = 24,
            paddingVertical = 12
        )
    }
}

@Composable
fun Button3D(
    modifier: Modifier = Modifier,
    icon: Int = 0,
    backgroundColor: Color = Color(0xFF2ED15F),
    shadowColor: Color = Color(0xFF13833A),
    width: Dp = 200.dp,
    height: Dp = 50.dp,
    cornerRadius: Int = 12,
    onClick: () -> Unit,
    text: String = "",
    textColor: Color = Color.White,
    textSize: Int = 18,
) {
    var isPressed by remember { mutableStateOf(false) }
    val offsetY by animateFloatAsState(
        targetValue = if (isPressed) 0f else 6f,
        animationSpec = tween(100),
        label = "press_offset"
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .padding(bottom = 6.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(
                    color = shadowColor,
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = (-offsetY).dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                            onClick()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =  Modifier.width(
                    width
                ).height(
                    height
                ).align(
                    Alignment.Center
                ).padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            ) {
                if (icon!=0){
                    Icon(
                        painterResource(icon),
                        contentDescription = "$icon",

                        )

                }else{
                    Text(
                        text =  text  ,
                        color = textColor,
                        fontSize = textSize.sp,
                        fontFamily = FontFamily.Default,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
@Preview
@Composable
fun Button3DPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Button3D(
            icon = R.drawable.play_ic,
            onClick = { /* اضغط هنا */ },
        )
    }
}