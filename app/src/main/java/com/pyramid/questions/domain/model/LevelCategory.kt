package com.pyramid.questions.domain.model

import androidx.compose.ui.graphics.Color

data class LevelCategory(
    val id: Int,
    val name: String,
    val description: String,
    val iconResId: Int,
    val backgroundColor: Color,
    val levels: List<GameLevel>
)
