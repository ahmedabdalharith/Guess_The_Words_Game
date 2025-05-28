package com.pyramid.questions.domain.model

import com.pyramid.questions.core.Constants


data class LevelData(
    val levelNumber: Int,
    val imageResId: Int,
    val progress: String? = null,
    val starsRequired: Int? = null,
    val coinsRequired: Int? = null,
    val isUnlocked: Boolean = false,
    val isSpecial: Boolean = false,
    val specialTitle: String = "",
    val specialSubtitle: String = "",
    val category: Constants.WordCategory = Constants.WordCategory.COUNTRIES,
    val difficulty: Int = 1,
    val newWordsCount: Int = 0,
    val hasGift: Boolean = false
)
