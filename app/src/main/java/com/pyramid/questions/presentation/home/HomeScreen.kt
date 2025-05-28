package com.pyramid.questions.presentation.home

import LevelUnlockDialog
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pyramid.questions.AppColors
import com.pyramid.questions.AppColors.GoldColor
import com.pyramid.questions.AppColors.SpecialTextColor
import com.pyramid.questions.R
import com.pyramid.questions.core.Constants.GameModeTab
import com.pyramid.questions.core.Constants.WordCategory
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.data.remote.AdMobManager
import com.pyramid.questions.domain.model.LevelData
import com.pyramid.questions.navigation.Route
import com.pyramid.questions.presentation.components.DailyRewardDialog
import com.pyramid.questions.presentation.components.GameProgressBarSpacial
import com.pyramid.questions.presentation.components.SettingsDialog
import com.pyramid.questions.presentation.components.TopHomeBar
import com.pyramid.questions.presentation.components.UsernameGloableDialog
import com.pyramid.questions.presentation.components.WhichAdvDialog
import com.pyramid.questions.presentation.levels.Divider3D
import com.pyramid.questions.presentation.test.ComposableBox3D
import com.pyramid.questions.presentation.test.EnhancedImageBox3D
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun HomeScreen(
    onStartGame: (levelId: Int, category: WordCategory) -> Unit = { _, _ -> },
    onOpenStore: () -> Unit = {},
    onOpenDailyChallenge: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    navController: NavHostController,
    adMobManager : AdMobManager = AdMobManager(navController.context),
) {
    val preferencesManager = remember { GamePreferencesManager(navController.context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    val context = LocalContext.current
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showWhichAdvDialog by remember { mutableStateOf(false) }
    var showUsernameDialog by remember { mutableStateOf(false) }
    var showDailyReward by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(GameModeTab.NORMAL) }
    var coins by remember { mutableIntStateOf(0) }
    val arabicFont = FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.fon2
    }))
    val playerStats = preferencesManager.getPlayerStats()
    CompositionLocalProvider(
            LocalContext provides LocalContext.current.createConfigurationContext(
                Configuration().apply { setLocale(currentLocale) }
            )
        ) {


           if (showDailyReward) {
                        DailyRewardDialog(onClose = { showDailyReward = false })
                    }
                        LaunchedEffect(Unit) {
                delay(1500)
                showDailyReward = true
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AppColors.BackgroundStart,
                                AppColors.BackgroundEnd
                            )
                        )
                    )
            ) {
                Column {
                    TopHomeBar(
                        playerStats = playerStats,
                        onOpenStore = onOpenStore,
                        onOpenSettings = { showSettingsDialog = true },
                        fontFamily = arabicFont,
                        navController = navController,
                        onVideoClick = { showWhichAdvDialog = true },
                        onWorldClick = { showUsernameDialog = true },
                    )


                    Divider3D()

                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (showSettingsDialog) {
                            SettingsDialog(
                                onCloseClick = { showSettingsDialog = false },
                                onSelectLanguageClick = {
//                                    preferencesManager.setLanguage(
//                                        if (currentLocale == Locale("ar")) "en" else "ar"
//                                    )
                                    navController.navigate(Route.HOME) {
                                        popUpTo(Route.HOME) { inclusive = true }
                                    }
                                },)
                        }
                        if (showUsernameDialog) {
                            UsernameGloableDialog(
                                onCloseClick = { showUsernameDialog = false },
                                onClickYes = { navController.navigate(Route.PLAYERS) }
                            )
                        }

                        if (showWhichAdvDialog) {
                            WhichAdvDialog(
                                onClose = { showWhichAdvDialog = false },
                                onRewardEarned = { earnedCoins ->
                                    coins += earnedCoins
                                    // Save coins to your game state/database
                                    Log.d("Game", context.getString(R.string.coins_earned, earnedCoins))
                                },
                                adMobManager = adMobManager
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        GameModeTabsEnhanced(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it },
                            fontFamily = arabicFont
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        when (selectedTab) {
                            GameModeTab.NORMAL -> NormalLevelsList(
                                onLevelSelected = { levelData ->
                                    onStartGame(levelData.levelNumber, levelData.category)
                                },
                                fontFamily = arabicFont
                            )
                            GameModeTab.PREMIUM -> PremiumLevelsList(
                                onLevelSelected = { levelData ->
                                    onStartGame(levelData.levelNumber, levelData.category)
                                },
                                fontFamily = arabicFont
                            )
                            GameModeTab.DAILY -> DailyLevelsList(
                                onLevelSelected = { levelData ->
                                    onStartGame(levelData.levelNumber, levelData.category)
                                },
                                fontFamily = arabicFont
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

@Composable
fun NormalLevelsList(
    onLevelSelected: (LevelData) -> Unit,
    fontFamily: FontFamily
) {
    LocalContext.current
    val levels = listOf(
        LevelData(
            levelNumber = 1,
            imageResId = R.drawable.img1,
            progress = stringResource(R.string.progress_6_18),
            isUnlocked = true,
            category = WordCategory.COUNTRIES,
            difficulty = 1,
            hasGift = true
        ),
        LevelData(
            levelNumber = 2,
            imageResId = R.drawable.img2,
            starsRequired = 10,
            coinsRequired = 100,
            category = WordCategory.TECH
        ),
        LevelData(
            levelNumber = 0,
            imageResId = R.drawable.img3,
            progress = stringResource(R.string.level_range),
            isSpecial = true,
            specialTitle = stringResource(R.string.special_level_title),
            specialSubtitle = stringResource(R.string.special_level_countries),
            category = WordCategory.COUNTRIES,
            isUnlocked = true,
            difficulty = 3
        ),
        LevelData(
            levelNumber = 3,
            imageResId = R.drawable.img4,
            starsRequired = 22,
            coinsRequired = 150,
            category = WordCategory.ANIMALS,
            difficulty = 2
        ),
        LevelData(
            levelNumber = 4,
            imageResId = R.drawable.img1,
            starsRequired = 34,
            coinsRequired = 200,
            category = WordCategory.COUNTRIES,
            difficulty = 3
        ),
        LevelData(
            levelNumber = 5,
            imageResId = R.drawable.img2,
            isUnlocked = true,
            category = WordCategory.ANIMALS,
            difficulty = 2,
            progress = stringResource(R.string.progress_0_15),
            newWordsCount = 5
        ),
        LevelData(
            levelNumber = 0,
            imageResId = R.drawable.img2,
            isSpecial = true,
            specialTitle = stringResource(R.string.special_level_title),
            category = WordCategory.ANIMALS,
            difficulty = 4,
            starsRequired = 40,
            coinsRequired = 300
        )
    )

    LevelSelectionList(
        levels = levels,
        onLevelSelected = onLevelSelected,
        fontFamily = fontFamily
    )
}

@Composable
fun PremiumLevelsList(
    onLevelSelected: (LevelData) -> Unit,
    fontFamily: FontFamily
) {
    LocalContext.current
    val levels = listOf(
        LevelData(
            levelNumber = 1,
            imageResId = R.drawable.img3,
            category = WordCategory.COUNTRIES,
            difficulty = 3,
            isUnlocked = true,
            progress = stringResource(R.string.progress_2_10),
            isSpecial = true,
            specialTitle = stringResource(R.string.special_level_premium),
            specialSubtitle = stringResource(R.string.special_level_premium_subtitle)
        ),
        LevelData(
            levelNumber = 2,
            imageResId = R.drawable.img4,
            category = WordCategory.MUSIC,
            starsRequired = 25,
            coinsRequired = 250,
            isSpecial = true,
            specialTitle = stringResource(R.string.special_level_music),
            difficulty = 4
        ),
        LevelData(
            levelNumber = 3,
            imageResId = R.drawable.img3,
            category = WordCategory.FOOD,
            difficulty = 3,
            starsRequired = 30,
            coinsRequired = 300,
            isSpecial = true,
            specialTitle = stringResource(R.string.special_level_food),
            specialSubtitle = stringResource(R.string.special_level_food_subtitle)
        )
    )

    LevelSelectionList(
        levels = levels,
        onLevelSelected = onLevelSelected,
        fontFamily = fontFamily
    )
}

@Composable
fun DailyLevelsList(
    onLevelSelected: (LevelData) -> Unit,
    fontFamily: FontFamily
) {
    val levels = listOf(
        LevelData(
            levelNumber = 0,
            imageResId = R.drawable.img3,
            isSpecial = true,
            specialTitle = stringResource(R.string.today_challenge),
            specialSubtitle = stringResource(R.string.date_may_14),
            isUnlocked = true,
            category = WordCategory.SPORTS,
            difficulty = 3,
            hasGift = true
        ),
        LevelData(
            levelNumber = 0,
            imageResId = R.drawable.img1,
            isSpecial = true,
            specialTitle = stringResource(R.string.yesterday_challenge),
            specialSubtitle = stringResource(R.string.date_may_13),
            isUnlocked = true,
            category = WordCategory.TECH,
            difficulty = 2,
            progress = stringResource(R.string.progress_8_10)
        ),
        LevelData(
            levelNumber = 0,
            imageResId = R.drawable.img4,
            isSpecial = true,
            specialTitle = stringResource(R.string.tomorrow_challenge),
            specialSubtitle = stringResource(R.string.date_may_15),
            category = WordCategory.FOOD,
            difficulty = 3,
            coinsRequired = 100
        )
    )

    LevelSelectionList(
        levels = levels,
        onLevelSelected = onLevelSelected,
        fontFamily = fontFamily
    )
}

@Composable
fun LevelItem(
    levelData: LevelData,
    onClick: () -> Unit,
    fontFamily: FontFamily,
    onLockCheck: () -> Unit = {}
) {
    LocalContext.current
    val isSpecial = levelData.isSpecial
    val backgroundGradient = if (isSpecial) {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFF7EBD7),
                Color(0xFFEBD1AF)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF3B6CB1),
                Color(0xFF5D8CCB)
            )
        )
    }
    val shadowColor = if (isSpecial) Color(0xFFA4825A) else Color(0xFF133478)
    var showUnlockDialog by remember { mutableStateOf(false) }
    val shadowEffectColor = if (isSpecial) Color(0xFFc3a275) else Color(0xFF5d7fb2)

    val handleClick = {
        if (levelData.isUnlocked) {
            onClick()
        } else {
            onLockCheck()
        }
    }

    ComposableBox3D(
        shadowEffectColor = shadowEffectColor,
        shadowBoxColor = shadowColor,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 4.dp)
            .clickable { handleClick() },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LevelItemImage(levelData)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (isSpecial) {
                        SpecialLevelContent(levelData, fontFamily)
                    } else {
                        NormalLevelContent(levelData, fontFamily)
                    }
                }

                if (!levelData.isUnlocked) {
                    LockIcon(onClick = { showUnlockDialog = true })
                } else {
                    Spacer(Modifier.width(22.dp))
                }

                if (showUnlockDialog) {
                    LevelUnlockDialog(
                        title = stringResource(R.string.unlock_level_title),
                        subtitle = stringResource(R.string.unlock_level_subtitle),
                        starsRequired = levelData.starsRequired ?: 10,
                        coinsCost = levelData.coinsRequired ?: 100,
                        onUnlock = { },
                        onClose = { showUnlockDialog = false },
                    )
                }

                if (levelData.coinsRequired != null && levelData.coinsRequired > 0) {
                    RequirementBadgeCoins(
                        coinsRequired = levelData.coinsRequired,
                        icon = R.drawable.coin1,
                        modifier = Modifier.offset(x = 12.dp, y = 0.dp),
                        levelData = levelData
                    )
                } else {
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        },
        containerGradient = backgroundGradient,
        borderColor = if (isSpecial) SpecialTextColor.copy(alpha = 0.3f) else AppColors.SecondaryColor.copy(alpha = 0.2f)
    )
}

@Composable
private fun RequirementBadgeCoins(
    modifier: Modifier = Modifier,
    icon: Int,
    coinsRequired: Int,
    onClick: () -> Unit = {},
    levelData: LevelData
) {
    LocalContext.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (levelData.isSpecial) {
            CoinButton(
                background = R.drawable.saved_gold,
                text = stringResource(R.string.vip_text),
                showIcon = false,
                onClick = onClick
            )

            Text(
                text = stringResource(R.string.or_text),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                color = Color.White,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        } else {
            Spacer(modifier = modifier.weight(2f))
        }
        CoinButton(
            background = R.drawable.saved_green,
            text = "$coinsRequired",
            icon = icon,
            showIcon = true,
            onClick = onClick
        )
    }
}

@Composable
private fun CoinButton(
    background: Int,
    text: String,
    icon: Int = 0,
    showIcon: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(30.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(background),
            contentDescription = "bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(30.dp)
                .width(70.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.Transparent)
                .padding(start = if (showIcon) 8.dp else 12.dp, end = if (showIcon) 8.dp else 12.dp)
        ) {
            if (showIcon) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = text,
                fontSize = if (showIcon) 13.sp else 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                color = Color.White,
            )
        }
    }
}

@Composable
fun NormalLevelContent(levelData: LevelData, fontFamily: FontFamily) {
    LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.level_text, levelData.levelNumber),
            fontSize = 14.sp,
            fontFamily = fontFamily,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        levelData.progress?.let { progress ->
            val progressParts = progress.split("/")
            val currentProgress = progressParts.getOrNull(0)?.trim()?.toFloatOrNull() ?: 0f
            val maxProgress = progressParts.getOrNull(1)?.trim()?.toFloatOrNull() ?: 1f
            GameProgressBarSpacial(
                current = currentProgress.toInt(),
                total = maxProgress.toInt(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
        }

        levelData.starsRequired?.let { stars ->
            StarsRequiredBadge(
                starsRequired = stars,
                fontFamily = fontFamily,
                badgeColor = AppColors.SpecialSecondaryColor,
                textColor = Color.White
            )
        }
    }
}

@Composable
fun GameModeTabsEnhanced(
    selectedTab: GameModeTab,
    onTabSelected: (GameModeTab) -> Unit,
    fontFamily: FontFamily
) {
    LocalContext.current
    val darkNavyBlue = Color(0xFF001034)
    val normalAccent = AppColors.GreenAccent
    val premiumAccent = GoldColor
    val dailyAccent = AppColors.DailyAccent

    ComposableBox3D(
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkNavyBlue.copy(alpha = 0.7f)),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (selectedTab == GameModeTab.NORMAL)
                                Brush.linearGradient(
                                    colors = listOf(normalAccent, normalAccent.copy(alpha = 0.8f))
                                )
                            else Brush.linearGradient(
                                colors = listOf(Color.Transparent, Color.Transparent)
                            )
                        )
                        .clickable { onTabSelected(GameModeTab.NORMAL) }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.normal_mode),
                        fontFamily = fontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == GameModeTab.NORMAL) darkNavyBlue else Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (selectedTab == GameModeTab.PREMIUM)
                                Brush.linearGradient(
                                    colors = listOf(
                                        premiumAccent,
                                        premiumAccent.copy(alpha = 0.8f)
                                    )
                                )
                            else Brush.linearGradient(
                                colors = listOf(Color.Transparent, Color.Transparent)
                            )
                        )
                        .clickable { onTabSelected(GameModeTab.PREMIUM) }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.premium_mode),
                        fontFamily = fontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == GameModeTab.PREMIUM) darkNavyBlue else Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (selectedTab == GameModeTab.DAILY)
                                Brush.linearGradient(
                                    colors = listOf(dailyAccent, dailyAccent.copy(alpha = 0.8f))
                                )
                            else Brush.linearGradient(
                                colors = listOf(Color.Transparent, Color.Transparent)
                            )
                        )
                        .clickable { onTabSelected(GameModeTab.DAILY) }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.daily_challenge),
                        fontFamily = fontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == GameModeTab.DAILY) darkNavyBlue else Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(darkNavyBlue.copy(alpha = 0.7f)),
        containerColor = darkNavyBlue.copy(alpha = 0.7f),
        borderColor = GoldColor.copy(alpha = 0.5f),
    )
}

@Composable
fun LevelSelectionList(
    levels: List<LevelData>,
    onLevelSelected: (LevelData) -> Unit,
    fontFamily: FontFamily
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(levels) { levelData ->
            LevelItem(
                levelData = levelData,
                onClick = { if (levelData.isUnlocked) onLevelSelected(levelData) },
                fontFamily = fontFamily
            )
        }
    }
}

@Composable
fun LevelItemImage(levelData: LevelData) {
    LocalContext.current
    Box(modifier = Modifier.size(80.dp)) {
        EnhancedImageBox3D(
            image = levelData.imageResId,
            size = 80,
            isRtl = true,
        )
        if (levelData.newWordsCount > 0 && levelData.isUnlocked) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(AppColors.AlertRed)
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.plus_words, levelData.newWordsCount),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (levelData.hasGift && levelData.isUnlocked) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(GoldColor)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gift_ic),
                    contentDescription = stringResource(R.string.gift),
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun SpecialLevelContent(levelData: LevelData, fontFamily: FontFamily) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = levelData.specialTitle,
            color = SpecialTextColor,
            fontWeight = FontWeight.Bold,
            fontSize =  14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 15.sp,
            fontFamily = fontFamily
        )
        Spacer(modifier = Modifier.height(
            if (levelData.isUnlocked) 4.dp else 0.dp
        ))
        GameProgressBarSpacial(
            current = 7,
            total = 22,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )
    }
}

@Composable
private fun LockIcon(onClick: () -> Unit) {
    LocalContext.current
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        AppColors.ProgressBarTrack,
                        AppColors.ProgressBarBackground.copy(alpha = 0.7f)
                    )
                )
            )
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.7f),
                        Color.White.copy(alpha = 0.3f)
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.lock),
            contentDescription = stringResource(R.string.locked),
            modifier = Modifier.size(48.dp)
        )
    }
}
@Composable
fun StarsRequiredBadge(
    starsRequired: Int,
    modifier: Modifier = Modifier
        .width(70.dp)
        .height(30.dp),
    fontFamily: FontFamily = FontFamily.Default,
    badgeColor: Color = Color(0xFF3A3F55),
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(badgeColor)
            .border(
                width = 1.dp,
                color = GoldColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = stringResource(R.string.stars),
                modifier = Modifier.size(22.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$starsRequired",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                color = textColor
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
        HomeScreen(
            onStartGame = { _, _ -> },
            onOpenStore = {},
            onOpenDailyChallenge = {},
            onOpenProfile = {},
            navController = rememberNavController(),
            adMobManager = AdMobManager(LocalContext.current)
        )
}