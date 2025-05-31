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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pyramid.questions.AppColors
import com.pyramid.questions.AppColors.GoldColor
import com.pyramid.questions.AppColors.SpecialTextColor
import com.pyramid.questions.R
import com.pyramid.questions.core.Constants.GameModeTab
import com.pyramid.questions.data.local.GameCategory
import com.pyramid.questions.data.local.GameCategoryType
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.data.local.GameRepository
import com.pyramid.questions.data.local.GameRoomDatabase
import com.pyramid.questions.data.local.HomeViewModel
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    onStartGame: (levelId: Int, category: GameCategoryType) -> Unit = { _, _ -> },
    onOpenStore: () -> Unit = {},
    navController: NavHostController,
) {
    val database = remember { GameRoomDatabase.getDatabase(navController.context) }
    val repository = remember { GameRepository(database) }
    val preferencesManager = remember { GamePreferencesManager(navController.context) }

    val viewModel: HomeViewModel = remember {
        HomeViewModel(repository, preferencesManager)
    }
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    var coins by remember { mutableIntStateOf(preferencesManager.getPlayer().coins) }
    val usernameId = if (preferencesManager.getUsernameId() == null) {
        preferencesManager.setUsernameId("user_${System.currentTimeMillis()}").toString()
    } else {
        preferencesManager.getUsernameId()
    }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    val context = LocalContext.current
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showWhichAdvDialog by remember { mutableStateOf(false) }
    var showUsernameDialog by remember { mutableStateOf(false) }
    var showDailyReward by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(GameModeTab.NORMAL) }
    val arabicFont = FontFamily(
        Font(
            if (currentLocale == Locale("ar")) {
                R.font.arbic_font_bold_2
            } else {
                R.font.eng3
            }
        )
    )
    val player = preferencesManager.getPlayer()
    CompositionLocalProvider(
        LocalContext provides LocalContext.current.createConfigurationContext(
            Configuration().apply { setLocale(currentLocale) }
        )
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            LaunchedEffect(Unit) {
                delay(1500)
                if (shouldShowDailyReward(preferencesManager)) {
                    showDailyReward = true
                }
            }
            if (showDailyReward) {
                DailyRewardDialog(
                    currentDay = 1,
                    onCollectReward = { reward ->
                        claimDailyReward(preferencesManager)
                        coins = preferencesManager.getPlayer().coins
                    },
                    onClose = { showDailyReward = false }
                )
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
                        player = player,
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
                                    navController.navigate(Route.HOME) {
                                        popUpTo(Route.HOME) { inclusive = true }
                                    }
                                },
                            )
                        }
                        if (showUsernameDialog) {
                            if (preferencesManager.getUsername().toString().isEmpty()) {
                                UsernameGloableDialog(
                                    onCloseClick = { showUsernameDialog = false },
                                    onClickYes = { navController.navigate(Route.PLAYERS) }
                                )
                            } else {
                                navController.navigate(Route.PLAYERS) {
                                    popUpTo(Route.HOME) { inclusive = true }
                                }
                            }
                        }

                        if (showWhichAdvDialog) {
                            WhichAdvDialog(
                                onClose = { showWhichAdvDialog = false },
                                onRewardEarned = { earnedCoins ->
                                    coins += earnedCoins
                                    Log.d(
                                        "Game",
                                        context.getString(R.string.coins_earned, earnedCoins)
                                    )
                                },
                                navController = navController
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
                                onLevelSelected = { gameCategory ->
                                    onStartGame(gameCategory.id, gameCategory.category)
                                },
                                fontFamily = arabicFont,
                                userId = usernameId.toString(),
                                viewModel = viewModel,
                                isLoading = uiState.isLoading
                            )

                            GameModeTab.PREMIUM -> PremiumLevelsList(
                                viewModel = viewModel,
                                onLevelSelected = { gameCategory ->
                                    onStartGame(gameCategory.id, gameCategory.category)
                                },
                                fontFamily = arabicFont,
                                isLoading = uiState.isLoading
                            )

                            GameModeTab.DAILY -> DailyLevelsList(
                                viewModel = viewModel,
                                onLevelSelected = { gameCategory ->
                                    onStartGame(gameCategory.id, gameCategory.category)
                                },
                                fontFamily = arabicFont,
                                isLoading = uiState.isLoading
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun NormalLevelsList(
    onLevelSelected: (GameCategory) -> Unit,
    fontFamily: FontFamily,
    userId: String,
    viewModel: HomeViewModel,
    isLoading: Boolean
) {
    LevelSelectionList(
        gameCategorys = viewModel.getNormalCategories(),
        onLevelSelected = onLevelSelected,
        fontFamily = fontFamily,
        isLoading = isLoading
    )
}

@Composable
fun PremiumLevelsList(
    onLevelSelected: (GameCategory) -> Unit,
    fontFamily: FontFamily,
    viewModel: HomeViewModel,
    isLoading: Boolean,
) {
    LevelSelectionList(
        gameCategorys = viewModel.getVipCategories(),
        onLevelSelected = onLevelSelected,
        fontFamily = fontFamily,
        isLoading = isLoading
    )
}

@Composable
fun DailyLevelsList(
    onLevelSelected: (GameCategory) -> Unit,
    fontFamily: FontFamily,
    viewModel: HomeViewModel,
    isLoading: Boolean
) {
    LevelSelectionList(
        gameCategorys = viewModel.getDailyCategories(),
        onLevelSelected = onLevelSelected,
        fontFamily = fontFamily,
        isLoading = isLoading
    )
}

@Composable
fun LevelItem(
    gameCategory: GameCategory,
    onClick: () -> Unit,
    fontFamily: FontFamily,
    onLockCheck: () -> Unit = {},
) {

    val isSpecial = gameCategory.isVipCategory || gameCategory.isDailyCategory
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
    val context = LocalContext.current
    val shadowColor = if (isSpecial) Color(0xFFA4825A) else Color(0xFF133478)
    var showUnlockDialog by remember { mutableStateOf(false) }
    val shadowEffectColor = if (isSpecial) Color(0xFFc3a275) else Color(0xFF5d7fb2)
    val preferencesManager = remember { GamePreferencesManager(context) }

    val viewModel = remember {
        mutableStateOf(
            HomeViewModel(
                repository = GameRepository(GameRoomDatabase.getDatabase(context)),
                preferencesManager = preferencesManager
            )
        )
    }
    val handleClick = {
        if (gameCategory.isUnlocked) {
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
            .height(110.dp)
            .padding(vertical = 4.dp)
            .clickable { handleClick() },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EnhancedImageBox3D(
                    image = gameCategory.iconResource,
                    size = 80,
                    isRtl = true,
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (gameCategory.isVipCategory) {
                        SpecialLevelContent(gameCategory, fontFamily)
                    } else if (!gameCategory.isDailyCategory) {
                        NormalLevelContent(gameCategory, fontFamily)
                    } else {
                        SpecialLevelContent(gameCategory, fontFamily)
                    }
                }

                if (!gameCategory.isUnlocked) {
                    LockIcon(onClick = { showUnlockDialog = true })
                } else {
                    Spacer(Modifier.width(22.dp))
                }
                if (showUnlockDialog) {
                    LevelUnlockDialog(
                        onUnlock = {
                            viewModel.value.updateCategory(
                                gameCategory.copy(
                                    isUnlocked = true,
                                    completedLevels = 0,
                                    totalLevels = gameCategory.totalLevels,
                                    requiredStars = 0,
                                    requiredCoins = 0
                                )
                            )
                            gameCategory.isUnlocked = true
                            gameCategory.completedLevels = 0
                            gameCategory.totalLevels = gameCategory.totalLevels
                            gameCategory.requiredStars = 0
                            gameCategory.requiredCoins = 0
                            showUnlockDialog = false
                        },
                        coinsCost = gameCategory.requiredCoins,
                        onClose = { showUnlockDialog = false },
                    )
                }

                if (!gameCategory.isUnlocked
                    || gameCategory.category == GameCategoryType.YESTERDAY_CHALLENGE
                    || gameCategory.category == GameCategoryType.DAY_CHALLENGE
                ) {
                    RequirementBadgeCoins(
                        coinsRequired = gameCategory.requiredCoins,
                        icon = R.drawable.coin1,
                        modifier = Modifier.offset(x = 12.dp, y = 0.dp),
                        levelData = gameCategory
                    )
                } else {
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        },
        containerGradient = backgroundGradient,
        borderColor = if (isSpecial) SpecialTextColor.copy(alpha = 0.3f) else AppColors.SecondaryColor.copy(
            alpha = 0.2f
        )
    )
}

@Composable
private fun RequirementBadgeCoins(
    modifier: Modifier = Modifier,
    icon: Int,
    coinsRequired: Int,
    onClick: () -> Unit = {},
    levelData: GameCategory
) {
    LocalContext.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (levelData.isVipCategory || levelData.category == GameCategoryType.TOMORROW_CHALLENGE) {
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
fun NormalLevelContent(gameCategory: GameCategory, fontFamily: FontFamily) {
    LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.level_text, gameCategory.id),
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            lineHeight = 15.sp,
            fontFamily = fontFamily
        )
        Spacer(
            modifier = Modifier.height(
                if (gameCategory.isUnlocked) 12.dp else 8.dp
            )
        )
        if (gameCategory.isUnlocked)
            GameProgressBarSpacial(
                current = gameCategory.completedLevels,
                total = gameCategory.totalLevels,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
        if (!gameCategory.isUnlocked) {
            StarsRequiredBadge(
                starsRequired = gameCategory.requiredStars,
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
            .height(70.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(darkNavyBlue.copy(alpha = 0.7f)),
        containerColor = darkNavyBlue.copy(alpha = 0.7f),
        borderColor = GoldColor.copy(alpha = 0.5f),
    )
}

@Composable
fun LevelSelectionList(
    gameCategorys: List<GameCategory>,
    onLevelSelected: (GameCategory) -> Unit,
    fontFamily: FontFamily,
    isLoading: Boolean = false,
) {
    if (!isLoading) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(gameCategorys) { levelData ->
                LevelItem(
                    gameCategory = levelData,
                    onClick = { if (levelData.isUnlocked) onLevelSelected(levelData) },
                    fontFamily = fontFamily
                )
            }
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp),
            color = AppColors.GreenAccent,
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun SpecialLevelContent(gameCategory: GameCategory, fontFamily: FontFamily) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = gameCategory.category.name.replace("_", " ")
                .replaceFirstChar { it.uppercase() },
            color = SpecialTextColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 15.sp,
            fontFamily = fontFamily
        )
        Spacer(
            modifier = Modifier.height(
                if (gameCategory.isUnlocked) 4.dp else 0.dp
            )
        )
        GameProgressBarSpacial(
            current = gameCategory.completedLevels,
            total = gameCategory.totalLevels,
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

// Add to HomeScreen
private fun shouldShowDailyReward(preferencesManager: GamePreferencesManager): Boolean {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val lastRewardDate = preferencesManager.getLastDailyRewardDate()
    return lastRewardDate != today
}

private fun getDailyRewardAmount(preferencesManager: GamePreferencesManager): Int {
    val streak = preferencesManager.getDailyRewardStreak()
    return when {
        streak >= 7 -> 500
        streak >= 5 -> 300
        streak >= 3 -> 200
        streak >= 1 -> 100
        else -> 50
    }
}

private fun claimDailyReward(preferencesManager: GamePreferencesManager) {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val yesterday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
        Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
    )
    val lastRewardDate = preferencesManager.getLastDailyRewardDate()

    val currentStreak = if (lastRewardDate == yesterday) {
        preferencesManager.getDailyRewardStreak() + 1
    } else {
        1
    }

    val rewardAmount = getDailyRewardAmount(preferencesManager)
    val currentStats = preferencesManager.getPlayer()
    val newStats = currentStats.copy(coins = currentStats.coins + rewardAmount)

    preferencesManager.savePlayer(newStats)
    preferencesManager.setLastDailyRewardDate(today)
    preferencesManager.setDailyRewardStreak(currentStreak)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onStartGame = { _, _ -> },
        onOpenStore = {},
        navController = rememberNavController(),
    )
}