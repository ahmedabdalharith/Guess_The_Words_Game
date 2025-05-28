package com.pyramid.questions.core

import androidx.compose.ui.graphics.Color
import com.pyramid.questions.R
object Constants {
        // App
        const val APP_NAME = "Trivia Game"

        // Navigation
        const val SPLASH_SCREEN = "splash_screen"
        const val HOME_SCREEN = "home_screen"
        const val GAME_SCREEN = "game_screen"
        const val RESULT_SCREEN = "result_screen"

        // Arguments
        const val SCORE_ARG = "score"
        const val TOTAL_QUESTIONS_ARG = "total_questions"

        // Game Settings
        const val DEFAULT_QUESTION_COUNT = 10
        const val QUESTION_TIMER_SECONDS = 15

        // Animations
        const val SPLASH_DURATION = 2000L

        enum class GameModeTab {
                NORMAL,
                PREMIUM,
                DAILY
        }
        enum class WordCategory(val arabicName: String, val iconResId: Int, val colorGradient: List<Color>) {
                COUNTRIES("البلاد", R.drawable.img1, listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))),
                ANIMALS("الحيوانات", R.drawable.idea_ic, listOf(Color(0xFFF44336), Color(0xFFC62828))),
                FOOD("الطعام", R.drawable.idea_ic, listOf(Color(0xFFFF9800), Color(0xFFE65100))),
                SPORTS("الرياضة", R.drawable.idea_ic, listOf(Color(0xFF2196F3), Color(0xFF0D47A1))),
                MUSIC("الموسيقى", R.drawable.idea_ic, listOf(Color(0xFF9C27B0), Color(0xFF6A1B9A))),
                TECH("التكنولوجيا", R.drawable.idea_ic, listOf(Color(0xFF607D8B), Color(0xFF37474F))),
                TRAVEL( "السفر", R.drawable.idea_ic, listOf(Color(0xFF8BC34A), Color(0xFF33691E))),
                FAMILY( "العائلة", R.drawable.idea_ic, listOf(Color(0xFF3F51B5), Color(0xFF1A237E))),
                PREMIUM( "المميز", R.drawable.idea_ic, listOf(Color(0xFF9E9D24), Color(0xFF827717))),
        }
}