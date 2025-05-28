package com.pyramid.questions

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColors {
    // Background Colors
    val BackgroundStart = Color(0xFF0C1638)
    val BackgroundEnd = Color(0xFF1A2749)
    val TabBackground = Color(0xFF1E2746)

    // Tab Colors - New
    val TabSelected = Color(0xFFFFD700) // Gold
    val TabUnselected = Color(0xFF5A7099) // Slate Blue
    val TabSelectedText = Color(0xFF333333) // Dark text for selected tabs
    val TabUnselectedText = Color.White // White text for unselected tabs
    val TabSelectedShadow = Color(0xFFD4AF37) // Darker Gold
    val TabUnselectedShadow = Color(0xFF3A4B66) // Dark Slate Blue

    // Player Rank Item Colors - New
    // Regular Player Item
    val RegularPlayerGradientStart = Color(0xFF64A0FF)
    val RegularPlayerGradientEnd = Color(0xFF3C82FF)
    val RegularPlayerContainer = Color(0xFF5A7099)
    val regularPlayerGradient = Brush.horizontalGradient(
        colors = listOf(RegularPlayerGradientStart, RegularPlayerGradientEnd)
    )

    // Top Three Player Item
    val TopThreeGradientStart = Color(0xFFFFF9C4)
    val TopThreeGradientEnd = Color(0xFFFFEB3B)
    val TopThreeContainer = Color(0xFFFFF59D)
    val topThreeGradient = Brush.horizontalGradient(
        colors = listOf(TopThreeGradientStart, TopThreeGradientEnd)
    )

    // Highlighted (Current User) Player Item
    val HighlightedPlayerGradientStart = Color(0xFFDCF8FF)
    val HighlightedPlayerGradientEnd = Color(0xFF8ED6FF)
    val HighlightedPlayerContainer = Color(0xFFD4E9FF)
    val highlightedPlayerGradient = Brush.horizontalGradient(
        colors = listOf(HighlightedPlayerGradientStart, HighlightedPlayerGradientEnd)
    )

    // Rank Colors - New
    val RankGold = Color(0xFFFFD700)  // 1st place
    val RankSilver = Color(0xFFC0C0C0) // 2nd place
    val RankBronze = Color(0xFFCD7F32) // 3rd place
    val RankDefault = Color(0xFF1E3F66) // Other ranks

    // Text Colors - New
    val TextWhite = Color.White
    val TextGold = Color(0xFFFFD700)
    val TextDark = Color(0xFF333333)
    val TextTopThree = Color(0xFF5D4037) // Brown for top three players
    val TextPrimary = Color.White // الاسم القديم
    val TextHighlighted = Color(0xFF333333) // الاسم القديم Dark Gray

    // Border Colors - Expanded
    val BorderWhite = Color.White
    val BorderYellow = Color.Yellow
    val BorderGold = Color(0xFFFFD700)
    val BorderSilver = Color(0xFFC0C0C0)
    val BorderBronze = Color(0xFFCD7F32)
    val BorderWhiteOpaque = Color.White.copy(alpha = 0.7f)
    val BorderRegular = Color.White.copy(alpha = 0.7f)
    val BorderHighlighted = Color.Yellow
    val BorderStatsCounter = Color.Yellow.copy(alpha = 0.7f) // الاسم القديم

    // Score Star Colors - New
    val ScoreRegularStart = Color(0xFF1A237E)
    val ScoreRegularEnd = Color(0xFF3949AB)
    val ScoreBorder = Color(0xFF9FA8DA)

    val ScoreHighlightedStart = Color(0xFF1976D2)
    val ScoreHighlightedEnd = Color(0xFF42A5F5)
    val ScoreHighlightedBorder = Color(0xFFFFEB3B)

    val ScoreGoldStart = Color(0xFFF57F17)
    val ScoreGoldEnd = Color(0xFFFFD600)
    val ScoreGoldBorder = Color(0xFFFFD700)

    val ScoreSilverStart = Color(0xFF757575)
    val ScoreSilverEnd = Color(0xFFBDBDBD)
    val ScoreSilverBorder = Color(0xFFC0C0C0)

    val ScoreBronzeStart = Color(0xFF8D6E63)
    val ScoreBronzeEnd = Color(0xFFBCAAA4)
    val ScoreBronzeBorder = Color(0xFFCD7F32)

    // Shadow Colors
    val TextShadow = Color.Black.copy(alpha = 0.3f)

    // Original Colors - Keeping All
    val LockColor = Color(0xFFFF9800)
    val CoinsGradientStart = Color(0xFF2E59BC)
    val CoinsGradientEnd = Color(0xFF0C1B35)
    val StarsGradientStart = Color(0xFFBC942E)
    val StarsGradientEnd = Color(0xFF351C0C)
    val DailyStreakColor = Color(0xFFFF6F00)
    val XpBarColor = Color(0xFFE91E63)
    val PremiumAccent = Color(0xFFE040FB)
    val DailyAccent = Color(0xFFFFAB00)
    val PrimaryColor = Color(0xFF3F51B5)  // أزرق غامق مائل للبنفسجي
    val SecondaryColor = Color(0xFFFF9800)  // برتقالي دافئ
    val AccentColor = Color(0xFFE91E63)  // وردي فاتح
    val ButtonBlue = Color(0xFF5A7099)
    val NavyBlue = Color(0xFF1E3F66)
    val GoldColor = Color(0xFFFFD700)

    // Keeping all other original colors
    val SpecialLevelBackground = Color(0xFF1976D2)
    val NormalLevelBackground = Color(0xFF3949AB)
    val SpecialSecondaryColor = Color(0xFF1A2530)
    val SpecialTextColor = Color(0xFFF9A825)
    val GreenAccent = Color(0xFF4CAF50)  // أخضر زمردي
    val ProgressBarTrack = Color(0xFF455A64).copy(alpha = 0.3f)  // لون داكن شفاف
    val AlertRed = Color(0xFFF44336)
    val UnlockedContent = Color(0xFF8BC34A)
    val StarColor = Color(0xFFFFD54F)
    val InactiveStarColor = Color(0xFF78909C).copy(alpha = 0.5f)  // رمادي مزرق شفاف للنجوم غير النشطة
    val ProgressBarBackground = Color(0xFFB0BEC5).copy(alpha = 0.3f)  // رمادي مزرق شفاف
    val ProgressBarForeground = Color(0xFF4CAF50)  // أخضر زمردي
    val ProgressBarIndicator = Color(0xFF4CAF50).copy(alpha = 0.8f)  // أخضر زمردي شفاف

    fun getRankColor(rank: Int): Color {
        return when (rank) {
            1 -> RankGold
            2 -> RankSilver
            3 -> RankBronze
            else -> RankDefault
        }
    }

    fun getScoreGradientColors(rank: Int, isHighlighted: Boolean): Pair<Color, Color> {
        return when {
            isHighlighted -> Pair(ScoreHighlightedStart, ScoreHighlightedEnd)
            rank == 1 -> Pair(ScoreGoldStart, ScoreGoldEnd)
            rank == 2 -> Pair(ScoreSilverStart, ScoreSilverEnd)
            rank == 3 -> Pair(ScoreBronzeStart, ScoreBronzeEnd)
            else -> Pair(ScoreRegularStart, ScoreRegularEnd)
        }
    }

    fun getScoreBorderColor(rank: Int, isHighlighted: Boolean): Color {
        return when {
            isHighlighted -> ScoreHighlightedBorder
            rank == 1 -> ScoreGoldBorder
            rank == 2 -> ScoreSilverBorder
            rank == 3 -> ScoreBronzeBorder
            else -> ScoreBorder
        }
    }


}