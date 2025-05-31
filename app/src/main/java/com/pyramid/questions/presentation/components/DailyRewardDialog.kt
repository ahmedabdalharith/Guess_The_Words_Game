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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.pyramid.questions.R
import com.pyramid.questions.data.local.DailyReward
import com.pyramid.questions.data.local.DailyRewardType
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.data.local.GameRepository
import com.pyramid.questions.data.local.GameRoomDatabase
import com.pyramid.questions.data.local.HomeViewModel
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyRewardDialog(
    currentDay: Int,
    onClose: () -> Unit = {},
    onCollectReward: (DailyReward) -> Unit = {}
) {

    val context = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    val database = remember { GameRoomDatabase.getDatabase(context) }
    val repository = remember { GameRepository(database) }
    val viewModel: HomeViewModel = remember {
        HomeViewModel(repository, preferencesManager)
    }
    viewModel.loadDailyRewards()
    val arabicFont = FontFamily(
        Font(
            if (currentLocale == Locale("ar")) {
                R.font.arbic_font_bold_2
            } else {
                R.font.eng3
            }
        )
    )
    val dailyRewards = viewModel.dailyRewards.collectAsState().value

    BasicAlertDialog(
        onDismissRequest = onClose,
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onClose() }
                    .aspectRatio(0.7f)
                    .background(Color.Transparent)
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
                            .padding(top = 130.dp)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(dailyRewards) { reward ->
                                RewardItem(
                                    reward = reward,
                                    currentDay = currentDay,
                                    arabicFont = arabicFont,
                                    onClick = {
                                        if (reward.isActive && !reward.isCollected) {
                                            onCollectReward(reward)
                                        }
                                    }
                                )
                            }
                        }

                        Text(
                            text = stringResource(R.string.daily_reward_collect_instruction),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = arabicFont,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .offset(y = (-80).dp)
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.gift_daily_rewards),
                        contentDescription = null,
                        modifier = Modifier.size(280.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
fun RewardItem(
    reward: DailyReward,
    currentDay: Int,
    arabicFont: FontFamily,
    onClick: () -> Unit
) {
    val isCurrentDay = reward.currentDay == currentDay
    val isClickable = !reward.isCollected && (isCurrentDay)
    Box(
        modifier = Modifier
            .aspectRatio(0.8f)
            .clickable(
                enabled = isClickable,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                if (reward.isCollected) R.drawable.day_done
                else R.drawable.day
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(top =2.dp , bottom = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.daily_reward_day, reward.isActive),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (reward.isCollected) Color.White.copy(alpha = 0.9f)
                else Color(0xFFFFFFFF),
                fontFamily = arabicFont,
                style = TextStyle(
                    shadow = if (reward.isCollected) Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(1f, 1f),
                        blurRadius = 2f
                    ) else null
                ),
                modifier = Modifier.padding(bottom = 6.dp).align(
                    Alignment.TopCenter,
                )
            )
            if (!reward.isCollected){
                Image(
                    painter = painterResource(reward.rewardIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.Center)
                        .padding(2.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        if (reward.isCollected) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Collected",
                modifier = Modifier
                    .size(28.dp)
                    .scale(1.2f)
                    .align(Alignment.Center),
                tint = Color(0xFF4CAF50) // Green color for collected items

            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DailyRewardDialogPreview() {
    DailyRewardDialog(
        currentDay = 5,
        onClose = {},
        onCollectReward = {}
    )
}