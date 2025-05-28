package com.pyramid.questions.presentation.components
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyramid.questions.R@Composable
fun LevelProgressUI(
    level: Int = 5,
    currentXP: Int = 75,
    maxXP: Int = 100,
    currency: Int = 3,
    currencyType: String = stringResource(R.string.days_currency),
    modifier: Modifier = Modifier
) {
    Color(0xFF0C1339)
    val progressColor = Color(0xFFFF3368)
    val progressBackgroundColor = Color(0xFF444B69)
    val levelCircleBorderColor = Color(0xFFFFD700)
    val levelCircleBackgroundColor = Color.White

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Level indicator (circle)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                LevelCircle(
                    level = level,
                    borderColor = levelCircleBorderColor,
                    backgroundColor = levelCircleBackgroundColor,
                    modifier = Modifier.size(48.dp)
                )
            }

            // XP Progress section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                // XP text and values
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(
                            R.string.xp_progress_format,
                            stringResource(R.string.xp_label),
                            currentXP,
                            maxXP
                        ),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Progress bar
                ProgressBarGift(
                    progress = currentXP.toFloat() / maxXP.toFloat(),
                    progressColor = progressColor,
                    backgroundColor = progressBackgroundColor,
                    height = 12.dp
                )
            }

            // Currency indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.currency_format, currency, currencyType),
                    color = Color(0xFFFFA500),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun LevelCircle(
    level: Int,
    borderColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = size.width * 0.08f
            drawCircle(
                color = borderColor,
                style = Stroke(width = strokeWidth)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${stringResource(R.string.level_prefix)}$level",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun ProgressBarGift(
    progress: Float,
    progressColor: Color,
    backgroundColor: Color,
    height: Dp = 12.dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        // Progress part (filled)
        Box(
            modifier = Modifier
                .weight(progress)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(progressColor)
        )

        // Small gap between progress sections
        Spacer(modifier = Modifier.width(3.dp))

        // Background part (unfilled)
        Box(
            modifier = Modifier
                .weight(1f - progress)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(backgroundColor)
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun LevelProgressUIPreview() {
    LevelProgressUI()
}


@Composable
fun GameProgressBar(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val progressBackground = Color(0xFF2196F3)  // Dark blue background
    val progressTrack = Color(0xFF061D74)       // Light blue track
    val progressText = Color.White              // White text
    val progress by animateFloatAsState(
        targetValue = if (total > 0) current.toFloat() / total else 0f,
        label = "progressAnimation"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp )
            .background(Color.Transparent)
            .clip(RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(32.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(progressBackground)
                .padding(4.dp)
        ) {
            // Progress track (light blue bar)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(progressTrack.copy(alpha = 0.5f))
            )

            // Actual progress (filled portion)
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(progressTrack)
            )

            // Progress text
            Text(
                text = stringResource(R.string.progress_format, current, total),
                color = progressText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )


        }
        // Treasure chest icon
        Image(
            painterResource(R.drawable.gift_box),
            contentDescription = stringResource(R.string.gift_content_description),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(60.dp)
                .offset(
                    x = (10).dp,
                    y = (0).dp
                )
                .padding(end = 4.dp)           ,
            contentScale = ContentScale.Crop
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GameProgressBarPreview() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        GameProgressBar(current = 0, total = 3)
    }
}
@Preview(showBackground = true)
@Composable
fun GameProgressBarSpacialPreview() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .width(120.dp)
            .height(40.dp)
    ) {
        GameProgressBarSpacial(current = 8, total = 10)
    }
}
@Composable
fun GameProgressBarSpacial(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val progressBackground = Color(0xFF2196F3)  // Dark blue background
    val progressTrack = Color(0xFF061D74)       // Light blue track
    val progressText = Color.White              // White text
    val progress by animateFloatAsState(
        targetValue = if (total > 0) current.toFloat() / total else 0f,
        label = "progressAnimation"
    )

    Row(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp)
            .background(Color.Transparent)
            .clip(RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(0.6f)
                .clip(RoundedCornerShape(8.dp))
                .background(progressBackground)
                .padding(4.dp)
        ) {
            // Progress track (light blue bar)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(progressTrack.copy(alpha = 0.5f))
            )

            // Actual progress (filled portion)
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(6.dp))
                    .background(progressTrack)
            )

            // Progress text
            Text(
                text = stringResource(R.string.progress_format, current, total),
                color = progressText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Treasure chest icon
        Image(
            painterResource(R.drawable.my_gift),
            contentDescription = stringResource(R.string.gift_content_description),
            modifier = Modifier
                .size(40.dp)
                .offset(x = (-15).dp, y = 0.dp),
            contentScale = ContentScale.Crop
        )
    }
}