package com.pyramid.questions.domain.model

data class PlayerStats(
    val stars: Int = 0,
    val coins: Int = 0,
    val level: Int = 1,
    val xp: Int = 0,
    val totalXp: Int = 100,
    val streak: Int = 0
)