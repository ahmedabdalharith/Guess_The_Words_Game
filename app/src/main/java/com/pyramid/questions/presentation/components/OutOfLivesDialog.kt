package com.pyramid.questions.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.ui.theme.GuessTheWordsGameTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutOfLivesDialog(
    onCloseClick: () -> Unit = {},
    onBuyLivesClick: () -> Unit = {},
    onWatchAdClick: () -> Unit = {},
    onBecomeVipClick: () -> Unit = {},
    onJoinClick: () -> Unit = {},
    showDialog: Boolean = true,
    timeRemaining: String = "59:36"
) {
    val context = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    val hapticFeedback = LocalHapticFeedback.current

    val fontFamily = FontFamily(
        Font(
            if (currentLocale == Locale("ar")) {
                R.font.arbic_font_bold_2
            } else {
                R.font.eng3
            }
        )
    )

    if (showDialog) {
        BasicAlertDialog(
            onDismissRequest = onCloseClick,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            CompositionLocalProvider(
                LocalContext provides LocalContext.current.createConfigurationContext(
                    Configuration().apply { setLocale(currentLocale) }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .aspectRatio(0.5f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { /* Prevent clicks from propagating */ }
                ) {
                    Box {
                        // Main Dialog
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center)
                                .clip(RoundedCornerShape(24.dp))
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color(0xFF4C7ED3), Color(0xFF1E4998)),
                                        center = Offset.Infinite,
                                        radius = 900f
                                    )
                                )
                                .border(
                                    width = 6.dp,
                                    color = Color(0xFF3D9CFF),
                                    shape = RoundedCornerShape(24.dp)
                                )
                        ) {
                            OutOfLivesDialogContent(
                                onBuyLivesClick = onBuyLivesClick,
                                onWatchAdClick = onWatchAdClick,
                                fontFamily = fontFamily,
                                timeRemaining = timeRemaining
                            )
                        }

                        // Close Button
                        RedCloseButton(
                            icon = R.drawable.close_ic,
                            onClick = onCloseClick,
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = (-32).dp, y = 2.dp)
                        )

                    }
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xffC001FF),
                                        Color(0xFFc001ff)
                                    ),
                                    center = Offset.Infinite,
                                    radius = 900f
                                )
                            )
                            .border(
                                width = 6.dp,
                                color = Color(0xFFfee157),
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        VipUpgradeSection(
                            onBecomeVipClick = onBecomeVipClick,
                            onJoinClick = onJoinClick,
                            fontFamily = fontFamily,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OutOfLivesDialogContent(
    onBuyLivesClick: () -> Unit,
    onWatchAdClick: () -> Unit,
    fontFamily: FontFamily,
    timeRemaining: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = stringResource(R.string.out_of_lives_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = fontFamily,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(3f, 3f),
                    blurRadius = 5f
                )
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        // Heart with Zero
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.heart),
                contentDescription = stringResource(R.string.empty_heart_desc),
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "0",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = fontFamily
            )
        }

        Text(
            text = stringResource(R.string.next_life_time),
            fontSize = 16.sp,
            color = Color.White,
            fontFamily = fontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Timer with clock icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "🕒",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = timeRemaining,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = fontFamily
            )
        }

        Text(
            text = stringResource(R.string.add_life_continue),
            fontSize = 20.sp,
            color = Color.White,
            fontFamily = fontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CustomButton(
            onClick = onBuyLivesClick,
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.refill),
                            fontSize = 20.sp,
                            color = Color.White,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.coin1),
                        contentDescription = stringResource(R.string.heart_icon_desc),
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "100",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            backgroundColor = Color(0xFF4CAF50),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            onClick = onWatchAdClick,
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(R.string.get_one_life),
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🎬",
                        fontSize = 20.sp,
                    )
                }
            },
            backgroundColor = Color(0xFFFF9800),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun VipUpgradeSection(
    onBecomeVipClick: () -> Unit,
    onJoinClick: () -> Unit,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
    ) {
        CustomButton(
            onClick = onBecomeVipClick,
            content = {
                Text(
                    text = stringResource(R.string.become_vip),
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            backgroundColor = Color(0xFFfedc3c),
            modifier = Modifier
                .height(60.dp)
                .align(Alignment.TopStart)
                .width(120.dp)
                .clip(RoundedCornerShape(bottomEnd = 16.dp))

        )

        Text(
            text = stringResource(R.string.unlock_special_levels),
            fontSize = 18.sp,
            color = Color.White,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.TopEnd)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp)
                .padding(8.dp)
        )

        // Join Button (Green)
        CustomButton(
            onClick = onJoinClick,
            content = {
                Text(
                    text = stringResource(R.string.join),
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            backgroundColor = Color(0xFF2ae272),
            modifier = Modifier
                .padding(16.dp)
                .width(140.dp)
                .height(60.dp)
                .align(Alignment.BottomEnd)
        )
        Image(
            painter = painterResource(id = R.drawable.two_hearts),
            contentDescription = stringResource(R.string.vip_background_desc),
            modifier = Modifier
                .width(200.dp)
                .height(100.dp)
                .padding(start = 32.dp)
                .align(Alignment.BottomStart),
            contentScale = ContentScale.Crop
        )
    }

}

@Preview(showBackground = true)
@Composable
fun OutOfLivesDialogPreview() {
    GuessTheWordsGameTheme {
        OutOfLivesDialog(
            onBuyLivesClick = { println("Buy lives clicked") },
            onWatchAdClick = { println("Watch ad clicked") },
            onBecomeVipClick = { println("Become VIP clicked") },
            onJoinClick = { println("Join clicked") },
            onCloseClick = { println("Close clicked") }
        )
    }
}