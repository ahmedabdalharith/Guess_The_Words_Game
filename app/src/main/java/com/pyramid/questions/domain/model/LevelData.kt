package com.pyramid.questions.domain.model

import com.pyramid.questions.core.Constants


data class LevelData(
    val levelNumber: Int,
    val imageResId: Int,
    var progress: String? = null,
    var starsRequired: Int? = null,
    var coinsRequired: Int? = null,
    var isUnlocked: Boolean = false,
    val isSpecial: Boolean = false,
    val specialTitle: String = "",
    val specialSubtitle: String = "",
    val category: QuestionCategory = QuestionCategory.GENERAL,
    val difficulty: Int = 1,
    val newWordsCount: Int = 0,
    val hasGift: Boolean = false
)
