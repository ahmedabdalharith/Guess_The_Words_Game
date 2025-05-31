package com.pyramid.questions.presentation.components

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.data.remote.AdMobManager
import com.pyramid.questions.data.remote.rememberAdMobManager
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhichAdvDialog(
    onClose: () -> Unit = {},
    onRewardEarned: (Int) -> Unit = {},
    navController: NavController
) {
    val context  = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    val arabicFont = FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.eng3
    }))
    val adMobManager = rememberAdMobManager()
    BasicAlertDialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ){
        CompositionLocalProvider(
            LocalContext provides LocalContext.current.createConfigurationContext(
                Configuration().apply { setLocale(currentLocale) }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(0.8f)
                    .background(Color.Transparent)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
            ) {
                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
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
                            width = 8.dp,
                            color = Color(0xFF3D9CFF),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Title with shadow effect
                        Text(
                            text = stringResource(R.string.watch_video_title),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    offset = Offset(3f, 3f),
                                    blurRadius = 5f
                                )
                            ),
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )

                        Image(
                            painterResource(R.drawable.video_icon),
                            contentDescription = stringResource(R.string.video_icon_description),
                            modifier = Modifier
                                .size(140.dp),
                            contentScale = ContentScale.Crop
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            GameProgressBar(current = 0, total = 3)
                        }

                        Text(
                            text = stringResource(R.string.watch_video_description),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        YellowButton(
                            onClick = {
                                adMobManager.showVideoAd(
                                    activity = navController.context as Activity,
                                    preferInterstitial = false,
                                    rewardListener = object : AdMobManager.RewardedAdListener {
                                        override fun onUserEarnedReward(amount: Int, type: String) {
                                            Log.d(
                                                "AdMob",
                                                "User earned reward: $amount $type"
                                            )
                                        }

                                        override fun onAdClosed() {
                                            Log.d("AdMob", "Ad closed")
                                        }
                                    }
                                )
                            },
                            text = stringResource(R.string.watch_button_text),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp),
                            fontFamily = arabicFont
                        )
                    }
                }

                // Close button
                RedCloseButton(
                    icon = R.drawable.close_ic,
                    onClick = onClose,
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-36).dp, y = 2.dp)
                )
            }
        }
    }
}