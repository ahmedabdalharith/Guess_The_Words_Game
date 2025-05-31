package com.pyramid.questions.domain.model


data class Player(
    val username: String = "",
    val email: String = "",
    val stars: Int = 0,
    val coins: Int = 0,
    val currentLevel: Int = 1,
    val xp: Int = 0,
    val totalXp: Int = 100,
    val streak: Int = 0,
    val experience: Int = 0,
    val completedLevels: Int = 0,
    val totalPlayTime: Int = 0,
    val hintsUsed: Int = 0
)

