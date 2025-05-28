package com.pyramid.questions.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyramid.questions.R@Composable
fun GreenAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size : Dp = 54.dp,
    icon :Int = R.drawable.add_icon,
    innerShadow : List<Color> = listOf(
        Color(0xFF006400),
        Color(0xFFAEF4AE)
    ),
) {
    val lightGreen = Color(0xFF7CDF7C)
    val darkerGreen = Color(0xFF008000)
    val shadowColor = Color(0xFF073B80)
    val innerShadowTop = Color(0xFF006400)
    Color(0xFFAEF4AE)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = shadowColor
            )
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            lightGreen,
                            Color(0xFF5BCF5B)
                        ),
                        radius = 120f
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = innerShadow
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = darkerGreen
            ),
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .padding(size/8),
                colorFilter = ColorFilter.tint(innerShadowTop)

            )
        }
    }
}

@Preview
@Composable
fun PreviewEmbossedGreenAddButton() {
    GreenAddButton(
        onClick = { /* Do something */ },
    )
}
@Composable
fun RedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 54.dp,
    icon: Int = R.drawable.close_ic,
    innerShadow: List<Color> = listOf(
        Color(0xFF640000),
        Color(0xFFF4AEAE)
    ),
) {
    val lightRed = Color(0xFFDF7C7C)
    val darkerRed = Color(0xFF800000)
    val shadowColor = Color(0xFF3B0707)
    val innerShadowTop = Color(0xFF640000)
    Color(0xFFF4AEAE)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = shadowColor
            )
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            lightRed,
                            Color(0xFFCF5B5B)
                        ),
                        radius = 120f
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = innerShadow
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = darkerRed
            ),
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .padding(size/8),
                colorFilter = ColorFilter.tint(innerShadowTop)
            )
        }
    }
}

@Composable
fun RedCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size : Dp = 54.dp,
    icon :Int = R.drawable.close_ic,
) {
    val lightRed = Color(0xFFFF0000)
    val darkerRed = Color(0xFFB00000)
    val shadowColor = Color(0xFF073B80)
    val innerShadowTop = Color(0xFF790E0E)
    val innerShadowBottom = Color(0xFFFFA07A)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = shadowColor
            )
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            lightRed,
                            Color(0xFFFF5C5C)
                        ),
                        radius = 120f
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(innerShadowTop, innerShadowBottom)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = darkerRed
            ),
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .padding(size/8),
                colorFilter = ColorFilter.tint(innerShadowTop)

            )
        }
    }
}
@Composable
fun YellowButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    innerShadow: List<Color> = listOf(
        Color(0xFFFFEFB1),
        Color(0xFF8B6B00)
    ),
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    fontFamily: FontFamily = FontFamily(Font(R.font.arbic_font_bold_2))
) {
    val darkerYellow = Color(0xFFFFDD55)
    Color(0xFFFFC800)
    val shadowColor = Color(0xFF805E07)
    Color(0xFF8B6B00)
    Color(0xFFFFEFB1)
    val textColor = Color(0xFF5F4B00)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = shadowColor
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            darkerYellow,
                            Color(0xFFFFCC33)
                        ),
                        radius = 120f
                    )
                )
                .border(
                    width = 4.dp,
                    brush = Brush.verticalGradient(
                        colors = innerShadow
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable(onClick = onClick)
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp) // Padding for the text
            )

        }
    }
}
@Preview
@Composable
fun PreviewYellowButton() {
    YellowButton(
        onClick = { /* Do something */ },
        text = "Click Me",
        modifier = Modifier.fillMaxWidth(),
        fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)))
}

@Preview
@Composable
fun PreviewRedCloseButton() {
    RedCloseButton(
        onClick = { /* Do something */ },
    )
}
@Composable
fun YellowButtonWithIcon(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    innerShadow: List<Color> = listOf(
        Color(0xFFFFEFB1),
        Color(0xFF8B6B00)
    ),
    width: Dp = 48.dp,
    height :Dp =48.dp

) {
    val darkerYellow = Color(0xFFFFDD55)
    Color(0xFFFFC800)
    val shadowColor = Color(0xFF805E07)
    Color(0xFF8B6B00)
    Color(0xFFFFEFB1)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = shadowColor
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            darkerYellow,
                            Color(0xFFFFCC33)  // Maintained the gradient effect
                        ),
                        radius = 120f
                    )
                )
                .border(
                    width = 4.dp,
                    brush = Brush.verticalGradient(
                        colors = innerShadow
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable(onClick = onClick)
                .padding(8.dp)  // Padding for the icon
        ) {
            content()
        }
    }
}

// Example usage:
@Preview
@Composable
fun YellowButtonWithIconExample() {
    YellowButtonWithIcon(
        onClick = { /* your action */ },
        content = {
            Icon(
                painterResource(R.drawable.music_iic),  // Replace with your desired icon
                contentDescription = "Add",
                tint = Color(0xFF5F4B00),  // Dark yellow/brown for icon
                modifier = Modifier.size(24.dp)
            )
        }
    )
}

@Composable
fun GreenTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    innerShadow: List<Color> = listOf(
        Color(0xFF006400),
        Color(0xFFAEF4AE)
    ),
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    val darkerGreen = Color(0xFF008000)
    Color(0xFF7CDF7C)
    val shadowColor = Color(0xFF073B80)
    Color(0xFFAEF4AE)
    Color(0xFF006400)
    Color(0xFF5F4B00)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = shadowColor
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            darkerGreen,
                            Color(0xFF00F500)
                        ),
                        radius = 120f
                    )
                )
                .border(
                    width = 4.dp,
                    brush = Brush.verticalGradient(
                        colors = innerShadow
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable(onClick = onClick)
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = fontWeight,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.padding(top = 2.dp, bottom = 2.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewGreenTextButton() {
    GreenTextButton(
        onClick = { /* Do something */ },
        text = "Click Me",
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
fun ProgressIndicator(
    progress: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF0A4D8F),
    progressColor: Color = Color(0xFF00FF0D),
    textColor: Color = Color.White,
) {
    val progressParts = progress.split("/")
    val currentProgress = progressParts.getOrNull(0)?.trim()?.toFloatOrNull() ?: 0f
    val maxProgress = progressParts.getOrNull(1)?.trim()?.toFloatOrNull() ?: 1f
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
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF385170))
        )

        // Main progress bar container
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(26.dp)
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
    }
}
@Preview
@Composable
fun PreviewGameProgressIndicator3D() {
    ProgressIndicator(
        progress = "0/3",
        modifier = Modifier.padding(16.dp)
    )
}
@Composable
fun BlueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 54.dp,
    icon: Int = R.drawable.arrow_back_ic,
    innerShadow: List<Color> = listOf(
        Color(0xFF00008B), // Dark blue
        Color(0xFFADD8E6)  // Light blue
    ),
) {
    val lightBlue = Color(0xFF7CB9E8)
    val darkerBlue = Color(0xFF0047AB)
    val shadowColor = Color(0xFF073B80)
    val innerShadowTop = Color(0xFF00008B)
    Color(0xFFADD8E6)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = shadowColor
            )
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            lightBlue,
                            Color(0xFF5B9ECF)
                        ),
                        radius = 120f
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = innerShadow
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = darkerBlue
            ),
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .padding(size/8),
                colorFilter = ColorFilter.tint(innerShadowTop)
            )
        }
    }
}
@Preview
@Composable
fun PreviewBlueButton() {
   BlueButton(
        onClick = { /* Do something */ },
    )
}

@Composable
fun CustomButton(
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    backgroundBrush: Brush? = null,
    backgroundColor: Color = Color(0xFFFFDD55),
    baseColorForEffects: Color? = null,
    cornerRadius: Dp = 10.dp
) {
    val baseColor = baseColorForEffects ?: backgroundColor

    val innerShadowTop = baseColor.copy(
        red = minOf(1f, baseColor.red + 0.2f),
        green = minOf(1f, baseColor.green + 0.2f),
        blue = minOf(1f, baseColor.blue + 0.2f)
    )

    val innerShadowBottom = baseColor.copy(
        red = maxOf(0f, baseColor.red - 0.3f),
        green = maxOf(0f, baseColor.green - 0.3f),
        blue = maxOf(0f, baseColor.blue - 0.3f)
    )

    val shadowColor = baseColor.copy(
        red = maxOf(0f, baseColor.red - 0.4f),
        green = maxOf(0f, baseColor.green - 0.4f),
        blue = maxOf(0f, baseColor.blue - 0.4f),
        alpha = 0.7f
    )

    val buttonBrush = backgroundBrush ?: Brush.radialGradient(
        colors = listOf(
            backgroundColor,
            backgroundColor.copy(alpha = 0.8f)
        ),
        radius = 120f
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(cornerRadius),
                spotColor = shadowColor
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(cornerRadius))
                .background(brush = buttonBrush)
                .border(
                    width = 4.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(innerShadowTop, innerShadowBottom)
                    ),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .clickable(onClick = onClick)
                .padding(8.dp)
        ) {
            content()
        }
    }
}
@Preview
@Composable
fun TTTT(){
    Column {

        CustomButton(
            modifier = Modifier.fillMaxWidth().height(42.dp),
            onClick = {  },
            backgroundColor = Color(0xFF3366FF),
            content = {
                Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة")
            }
        )

        CustomButton(
            onClick = {  },
            backgroundColor = Color(0xFFFF3333),
            modifier = Modifier.fillMaxWidth(),
            content = {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف")
            }
        )
    }
}