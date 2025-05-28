package com.pyramid.questions.presentation.leaderboard

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pyramid.questions.AppColors
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.presentation.components.CustomButton
import com.pyramid.questions.presentation.components.TopGameBar
import java.util.Locale

@Composable
fun LeaderboardScreen(
    navController: NavHostController
) {
    val preferencesManager = remember { GamePreferencesManager(navController.context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    val arabicFont = FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.en_font
    }))
    val playerStats =preferencesManager.getPlayerStats()
    CompositionLocalProvider(
        LocalContext provides LocalContext.current.createConfigurationContext(
            Configuration().apply { setLocale(currentLocale) }
        )
    ) {
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopGameBar(
                    playerStats = playerStats,
                    onOpenStore = { /* Handle open store */ },
                    onOpenProfile = { /* Handle open profile */ },
                    showDriver = true,
                    onBackClicked = {
                        navController.popBackStack()
                    },
                    fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tab selector
                    LeaderboardTabs(arabicFont)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Filter tabs
                    LeaderboardFilterTabs(arabicFont)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Leaderboard list
                    LeaderboardList(
                        players = samplePlayers(),
                        currentUserRank = stringResource(R.string.current_rank).toInt(),
                        currentUserName = stringResource(R.string.current_user_name),
                        currentUserCountry = stringResource(R.string.country_egypt),
                        currentUserScore = 0,
                        arabicFont = arabicFont
                    )
                }

            }
        }
    }
}
@Composable
fun LeaderboardList(
    players: List<PlayerRank>,
    currentUserRank: Int,
    currentUserName: String,
    currentUserCountry: String,
    currentUserScore: Int,
    arabicFont: FontFamily
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(players) { player ->
                PlayerRankItem(
                    player = player,
                    fontFamily = arabicFont,
                    isHighlighted = false,
                    isTopThree = player.rank <= 3
                )
            }
        }

        // Current user's rank (floating above the rest)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            PlayerRankItem(
                player = PlayerRank(
                    rank = currentUserRank,
                    name = currentUserName,
                    score = currentUserScore,
                    country = "egypt"
                ),
                fontFamily = arabicFont,
                isHighlighted = true,
                isTopThree = false,
                modifier = Modifier
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

@Composable
fun PlayerRankItem(
    player: PlayerRank,
    fontFamily: FontFamily,
    isHighlighted: Boolean,
    isTopThree: Boolean = false,
    modifier: Modifier = Modifier
) {
    val backgroundGradient = when {
        isHighlighted -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFDCF8FF), Color(0xFF8ED6FF))
        )
        isTopThree -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFFFF9C4), Color(0xFFFFEB3B))
        )
        else -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF64A0FF), Color(0xFF3C82FF))
        )
    }

    when {
        isHighlighted -> Color.Yellow
        isTopThree -> Color(0xFFFFD700)
        else -> Color.White.copy(alpha = 0.7f)
    }

    val elevationDp = when {
        isHighlighted -> 8.dp
        isTopThree -> 4.dp
        else -> 1.dp
    }

    val scale = when {
        isHighlighted -> 1.05f
        isTopThree -> 1.02f
        else -> 1f
    }

    CustomButton(
        onClick = { },
        backgroundColor = when {
            isHighlighted -> Color(0xFFD4E9FF)
            isTopThree -> Color(0xFFFFF59D)
            else -> Color(0xFF5A7099)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp * scale)
            .shadow(
                elevation = elevationDp,
                shape = RoundedCornerShape(12.dp)
            ),
        content = {
            Row {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rank number with background
                    RankNumberWithBackground(player.rank)

                    Spacer(modifier = Modifier.width(8.dp))

                    // Country flag
                    CountryFlag(player.country)

                    Spacer(modifier = Modifier.width(16.dp))

                    // Player name
                    Text(
                        text = player.name,
                        color = when {
                            isHighlighted -> Color(0xFF333333)
                            isTopThree -> Color(0xFF5D4037)
                            else -> Color.White
                        },
                        fontSize = when {
                            isTopThree -> 22.sp
                            else -> 20.sp
                        },
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }

                StarWithScore(
                    score = player.score.toFloat(),
                    backgroundColor = when {
                        isHighlighted -> listOf(Color(0xFF1976D2), Color(0xFF42A5F5))
                        player.rank == 1 -> listOf(Color(0xFFF57F17), Color(0xFFFFD600))  // Gold
                        player.rank == 2 -> listOf(Color(0xFF757575), Color(0xFFBDBDBD))  // Silver
                        player.rank == 3 -> listOf(Color(0xFF8D6E63), Color(0xFFBCAAA4))  // Bronze
                        else -> listOf(Color(0xFF1A237E), Color(0xFF3949AB))  // Blue gradient
                    },
                    borderColor = when {
                        isHighlighted -> Color(0xFFFFEB3B)
                        player.rank == 1 -> Color(0xFFFFD700)  // Gold
                        player.rank == 2 -> Color(0xFFC0C0C0)  // Silver
                        player.rank == 3 -> Color(0xFFCD7F32)  // Bronze
                        else -> Color(0xFF9FA8DA)  // Light blue border
                    }
                )
            }
        },
        backgroundBrush = backgroundGradient
    )
}

@Composable
fun RankNumberWithBackground(rank: Int) {
    val backgroundColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color(0xFF1E3F66) // Blue for others
    }

    val size = when (rank) {
        1 -> 46.dp
        2 -> 44.dp
        3 -> 42.dp
        else -> 40.dp
    }

    val borderWidth = when (rank) {
        1 -> 2.5.dp
        2 -> 2.dp
        3 -> 2.dp
        else -> 1.5.dp
    }

    when (rank) {
        1 -> Color(0xFFFFD700).copy(alpha = 0.7f) // Gold glow
        2 -> Color(0xFFC0C0C0).copy(alpha = 0.7f) // Silver glow
        3 -> Color(0xFFCD7F32).copy(alpha = 0.7f) // Bronze glow
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = if (rank <= 3) 8.dp else 2.dp,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(backgroundColor)
            .border(borderWidth, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rank.toString(),
            color = if (rank <= 3) Color(0xFF333333) else Color.White,
            fontSize = when (rank) {
                1 -> 24.sp
                2 -> 22.sp
                3 -> 22.sp
                else -> 20.sp
            },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StarWithScore(
    score: Float,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = FontFamily.Default,
    fontSize: TextUnit = 16.sp,
    textColor: Color = Color.White,
    backgroundColor: List<Color> = listOf(Color(0xFF4A148C), Color(0xFF7B1FA2)),  // Purple gradient
    borderColor: Color = Color(0xFFCE93D8),  // Light purple border
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(backgroundColor),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painterResource(R.drawable.star),
                contentDescription = stringResource(R.string.stars_collected_description),
                modifier = Modifier
                    .size(32.dp)
                    .offset(y = (-1).dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = score.toInt().toString(),
                color = textColor,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize
            )
        }
    }
}
@Composable
fun LeaderboardTabs(fontFamily: FontFamily) {
    var selectedTab by remember { mutableStateOf("global") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Global tab
        LeaderboardTab(
            title = stringResource(R.string.global_leaderboard),
            isSelected = selectedTab == "global",
            onClick = { selectedTab = "global" },
            fontFamily = fontFamily,
            modifier = Modifier.weight(1f).height(50.dp)
        )

        // Local tab
        LeaderboardTab(
            title = stringResource(R.string.local_leaderboard),
            isSelected = selectedTab == "local",
            onClick = { selectedTab = "local" },
            fontFamily = fontFamily,
            modifier = Modifier.weight(1f).height(50.dp)
        )
    }
}

@Composable
fun LeaderboardTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFFFFD700) else Color(0xFF5A7099)
    if (isSelected) Color(0xFFD4AF37) else Color(0xFF3A4B66)
    CustomButton(
        onClick = onClick,
        backgroundColor = backgroundColor,
        content = {
            Text(
                text = title,
                color = if (isSelected) Color(0xFF333333) else Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(1f, 1f),
                        blurRadius = 2f
                    )
                )
            )
        },
        modifier = modifier
    )
}

@Composable
fun LeaderboardFilterTabs(fontFamily: FontFamily) {
    var selectedFilter by remember { mutableStateOf("week") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Weekly filter
        FilterTab(
            title = stringResource(R.string.weekly_filter),
            isSelected = selectedFilter == "week",
            onClick = { selectedFilter = "week" },
            fontFamily = fontFamily,
            modifier = Modifier.weight(1f).height(50.dp)
        )

        // Monthly filter
        FilterTab(
            title = stringResource(R.string.monthly_filter),
            isSelected = selectedFilter == "month",
            onClick = { selectedFilter = "month" },
            fontFamily = fontFamily,
            modifier = Modifier.weight(1f).height(50.dp)
        )

        // All-time filter
        FilterTab(
            title = stringResource(R.string.all_time_filter),
            isSelected = selectedFilter == "all",
            onClick = { selectedFilter = "all" },
            fontFamily = fontFamily,
            modifier = Modifier.weight(1f).height(50.dp)
        )
    }
}

@Composable
fun FilterTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFFFFD700) else Color(0xFF5A7099)
    if (isSelected) Color(0xFFD4AF37) else Color(0xFF3A4B66)
    CustomButton(
        onClick = onClick,
        backgroundColor = backgroundColor,
        content = {
            Text(
                text = title,
                color = if (isSelected) Color(0xFF333333) else Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(1f, 1f),
                        blurRadius = 2f
                    )
                )
            )
        },
        modifier = modifier
    )
}

@Composable
fun CountryFlag(countryCode: String) {
    val flagResource = when (countryCode.lowercase()) {
        "venezuela" -> R.drawable.venezuela
        "germany" -> R.drawable.germany
        "liechtenstein" -> R.drawable.liechtenstein
        "montenegro" -> R.drawable.montenegro
        "spain" -> R.drawable.spain
        "usa" -> R.drawable.usa
        "russia" -> R.drawable.russia
        "serbia" -> R.drawable.serbia
        "egypt" -> R.drawable.egypt
        else -> R.drawable.egypt
        // Default flag if country code is not recogn
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .border(1.dp, Color.White, CircleShape)
    ) {
        Image(
            painter = painterResource(id = flagResource),
            contentDescription = stringResource(R.string.flag_description, countryCode),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

data class PlayerRank(
    val rank: Int,
    val name: String,
    val score: Int,
    val country: String
)

@Composable
fun samplePlayers(): List<PlayerRank> {
    return listOf(
        PlayerRank(rank = 1, name = stringResource(R.string.player_yepy), score = 432, country = "venezuela"),
        PlayerRank(rank = 2, name = stringResource(R.string.player_tanja), score = 387, country = "germany"),
        PlayerRank(rank = 3, name = stringResource(R.string.player_heidi), score = 361, country = "usa"),
        PlayerRank(rank = 4, name = stringResource(R.string.player_ksenia), score = 322, country = "russia"),
        PlayerRank(rank = 5, name = stringResource(R.string.player_meda), score = 318, country = "serbia"),
        PlayerRank(rank = 6, name = stringResource(R.string.player_yuliet), score = 290, country = "venezuela"),
        PlayerRank(rank = 7, name = stringResource(R.string.player_nikol), score = 275, country = "liechtenstein"),
        PlayerRank(rank = 8, name = stringResource(R.string.player_daniel), score = 260, country = "montenegro"),
        PlayerRank(rank = 9, name = stringResource(R.string.player_daniel), score = 250, country = "spain"),
        PlayerRank(rank = 10, name = stringResource(R.string.player_daniel), score = 240, country = "usa"),
    )
}

@Preview
@Composable
fun LeaderboardScreenPreview() {
    LeaderboardScreen(navController = rememberNavController())
}