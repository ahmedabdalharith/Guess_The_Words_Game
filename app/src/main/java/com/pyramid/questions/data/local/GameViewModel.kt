package com.pyramid.questions.data.local

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val database = GameRoomDatabase.getDatabase(application)
    private val gameRepository = GameRepository(database)
    private val dailyRewardRepository = DailyRewardRepository(database)

    // UI States
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _gamePlayState = MutableStateFlow(GamePlayState())
    val gamePlayState: StateFlow<GamePlayState> = _gamePlayState.asStateFlow()

    private val _playerProgress = MutableLiveData<PlayerProgress>()
    val playerProgress: LiveData<PlayerProgress> = _playerProgress

    private val _categories = MutableLiveData<List<GameCategory>>()
    val categories: LiveData<List<GameCategory>> = _categories

    private val _currentCategory = MutableLiveData<GameCategory?>()
    val currentCategory: LiveData<GameCategory?> = _currentCategory

    private val _levelsInCategory = MutableLiveData<List<GameLevel>>()
    val levelsInCategory: LiveData<List<GameLevel>> = _levelsInCategory

    private val _currentLevel = MutableLiveData<GameLevel?>()
    val currentLevel: LiveData<GameLevel?> = _currentLevel

    private val _dailyRewards = MutableLiveData<List<DailyReward>>()
    val dailyRewards: LiveData<List<DailyReward>> = _dailyRewards

    private val _dailyRewardStats = MutableLiveData<DailyRewardStats>()
    val dailyRewardStats: LiveData<DailyRewardStats> = _dailyRewardStats

    init {
        initializeGame()
    }

    // ===== INITIALIZATION =====
    private fun initializeGame() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                gameRepository.initializeGame()
                dailyRewardRepository.initializeDailyRewards()

                loadPlayerProgress()
                loadCategories()
                loadDailyRewards()
                checkDailyRewardStreak()

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    // ===== PLAYER PROGRESS =====
    private fun loadPlayerProgress() {
        viewModelScope.launch {
            try {
                val progress = gameRepository.getPlayerProgress()
                _playerProgress.value = progress
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun updatePlayerProgress() {
        loadPlayerProgress()
    }

    // ===== CATEGORIES =====
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val allCategories = gameRepository.getAllCategories()
                _categories.value = allCategories
                updateCategoryUnlockStatus()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private suspend fun updateCategoryUnlockStatus() {
        val categories = _categories.value ?: return
        val progress = gameRepository.getPlayerProgress()

        categories.forEach { category ->
            if (!category.isUnlocked && gameRepository.canUnlockCategory(category.id)) {
                gameRepository.unlockCategory(category.id)
            }
        }

        // Reload categories after unlock status update
        val updatedCategories = gameRepository.getAllCategories()
        _categories.value = updatedCategories
    }

    fun selectCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                val category = gameRepository.getCategoryById(categoryId)
                _currentCategory.value = category

                if (category != null) {
                    loadLevelsForCategory(categoryId)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun getUnlockedCategories() {
        viewModelScope.launch {
            try {
                val unlockedCategories = gameRepository.getUnlockedCategories()
                _categories.value = unlockedCategories
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun getVipCategories() {
        viewModelScope.launch {
            try {
                val vipCategories = gameRepository.getVipCategories()
                _categories.value = vipCategories
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun getDailyCategories() {
        viewModelScope.launch {
            try {
                val dailyCategories = gameRepository.getDailyCategories()
                _categories.value = dailyCategories
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    // ===== LEVELS =====
    private fun loadLevelsForCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                val levels = gameRepository.getLevelsByCategory(categoryId)
                _levelsInCategory.value = levels
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun selectLevel(levelId: Int) {
        viewModelScope.launch {
            try {
                val level = gameRepository.getLevelById(levelId)
                _currentLevel.value = level

                if (level != null) {
                    startLevel(level)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun getNextUnlockedLevel() {
        viewModelScope.launch {
            try {
                val nextLevel = gameRepository.getNextUnlockedLevel()
                _currentLevel.value = nextLevel

                if (nextLevel != null) {
                    startLevel(nextLevel)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    // ===== GAME PLAY =====
    private suspend fun startLevel(level: GameLevel) {
        val existingState = gameRepository.getLevelState(level.id)

        val gameState = existingState ?: LevelGameState(
            levelId = level.id,
            currentLives = level.maxLives,
            playerAnswer = "",
            usedLetters = "",
            hintsUsedCount = 0,
            removedLetters = "",
            startTime = System.currentTimeMillis(),
            pauseTime = 0,
            isPaused = false
        )

        if (existingState == null) {
            gameRepository.saveLevelState(gameState)
        }

        _gamePlayState.value = GamePlayState(
            currentLevel = level,
            gameState = gameState,
            availableLetters = getAvailableLetters(level, gameState),
            isGameActive = true,
            isCompleted = false,
            isPaused = false
        )
    }

    private fun getAvailableLetters(level: GameLevel, state: LevelGameState): List<String> {
        val allLetters = level.availableLetters.split(",")
        val removedLetters = if (state.removedLetters.isNotEmpty()) {
            state.removedLetters.split(",")
        } else {
            emptyList()
        }
        return allLetters.filterNot { removedLetters.contains(it) }
    }

    fun selectLetter(letter: String) {
        val currentState = _gamePlayState.value
        val level = currentState.currentLevel ?: return
        val gameState = currentState.gameState ?: return

        if (currentState.playerAnswer.length >= level.answerLength) return

        val newAnswer = currentState.playerAnswer + letter
        val newUsedLetters = if (gameState.usedLetters.isEmpty()) {
            letter
        } else {
            "${gameState.usedLetters},$letter"
        }

        viewModelScope.launch {
            try {
                gameRepository.updatePlayerAnswer(level.id, newAnswer)

                val updatedState = gameState.copy(
                    playerAnswer = newAnswer,
                    usedLetters = newUsedLetters
                )

                _gamePlayState.value = currentState.copy(
                    playerAnswer = newAnswer,
                    gameState = updatedState
                )

                // Check if answer is complete
                if (newAnswer.length == level.answerLength) {
                    checkAnswer(newAnswer, level)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun removeLetter() {
        val currentState = _gamePlayState.value
        val level = currentState.currentLevel ?: return
        val gameState = currentState.gameState ?: return

        if (currentState.playerAnswer.isEmpty()) return

        val newAnswer = currentState.playerAnswer.dropLast(1)
        val usedLettersList = gameState.usedLetters.split(",").toMutableList()
        if (usedLettersList.isNotEmpty()) {
            usedLettersList.removeAt(usedLettersList.size - 1)
        }
        val newUsedLetters = usedLettersList.joinToString(",")

        viewModelScope.launch {
            try {
                gameRepository.updatePlayerAnswer(level.id, newAnswer)

                val updatedState = gameState.copy(
                    playerAnswer = newAnswer,
                    usedLetters = newUsedLetters
                )

                _gamePlayState.value = currentState.copy(
                    playerAnswer = newAnswer,
                    gameState = updatedState
                )
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private suspend fun checkAnswer(answer: String, level: GameLevel) {
        if (answer.equals(level.correctAnswer, ignoreCase = true)) {
            // Correct answer
            val completionTime = (System.currentTimeMillis() - _gamePlayState.value.gameState?.startTime!!) / 1000
            val starsEarned = calculateStarsEarned(level, _gamePlayState.value.gameState!!)

            gameRepository.completeLevel(level.id, starsEarned, completionTime)

            _gamePlayState.value = _gamePlayState.value.copy(
                isCompleted = true,
                isGameActive = false,
                starsEarned = starsEarned
            )

            loadPlayerProgress()

        } else {
            // Wrong answer
            val currentLives = _gamePlayState.value.gameState?.currentLives?.minus(1) ?: 0

            if (currentLives <= 0) {
                // Game over
                _gamePlayState.value = _gamePlayState.value.copy(
                    isGameActive = false,
                    isGameOver = true
                )
            } else {
                // Continue with reduced lives
                val updatedState = _gamePlayState.value.gameState?.copy(
                    currentLives = currentLives,
                    playerAnswer = ""
                )

                gameRepository.saveLevelState(updatedState!!)

                _gamePlayState.value = _gamePlayState.value.copy(
                    gameState = updatedState,
                    playerAnswer = ""
                )
            }
        }
    }

    private fun calculateStarsEarned(level: GameLevel, gameState: LevelGameState): Int {
        val maxStars = level.starsReward
        val hintsUsed = gameState.hintsUsedCount
        val livesLost = level.maxLives - gameState.currentLives

        return when {
            hintsUsed == 0 && livesLost == 0 -> maxStars // Perfect
            hintsUsed <= 1 && livesLost <= 1 -> maxStars - 1 // Good
            else -> maxStars - 2 // Minimum 1 star
        }.coerceAtLeast(1)
    }

    // ===== HINTS =====
    fun useHint(hintType: HintType) {
        val currentState = _gamePlayState.value
        val level = currentState.currentLevel ?: return

        viewModelScope.launch {
            try {
                val success = gameRepository.useHint(level.id, hintType)

                if (success) {
                    // Reload level state to get updated hint info
                    val updatedState = gameRepository.getLevelState(level.id)
                    val updatedAvailableLetters = getAvailableLetters(level, updatedState!!)

                    _gamePlayState.value = currentState.copy(
                        gameState = updatedState,
                        availableLetters = updatedAvailableLetters
                    )

                    loadPlayerProgress()
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Not enough coins for hint!"
                    )
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    // ===== GAME CONTROL =====
    fun pauseGame() {
        val currentState = _gamePlayState.value
        val gameState = currentState.gameState ?: return

        viewModelScope.launch {
            try {
                val pauseTime = System.currentTimeMillis()
                val updatedState = gameState.copy(
                    isPaused = true,
                    pauseTime = pauseTime
                )

                gameRepository.saveLevelState(updatedState)

                _gamePlayState.value = currentState.copy(
                    gameState = updatedState,
                    isPaused = true
                )
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun resumeGame() {
        val currentState = _gamePlayState.value
        val gameState = currentState.gameState ?: return

        viewModelScope.launch {
            try {
                val resumeTime = System.currentTimeMillis()
                val pausedDuration = resumeTime - gameState.pauseTime
                val adjustedStartTime = gameState.startTime + pausedDuration

                val updatedState = gameState.copy(
                    isPaused = false,
                    startTime = adjustedStartTime,
                    pauseTime = 0
                )

                gameRepository.saveLevelState(updatedState)

                _gamePlayState.value = currentState.copy(
                    gameState = updatedState,
                    isPaused = false
                )
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun restartLevel() {
        val currentLevel = _gamePlayState.value.currentLevel ?: return

        viewModelScope.launch {
            try {
                // Delete existing game state
                gameRepository.getLevelState(currentLevel.id)?.let { state ->
                    gameRepository.saveLevelState(state.copy(
                        currentLives = currentLevel.maxLives,
                        playerAnswer = "",
                        usedLetters = "",
                        hintsUsedCount = 0,
                        removedLetters = "",
                        startTime = System.currentTimeMillis(),
                        pauseTime = 0,
                        isPaused = false
                    ))
                }

                startLevel(currentLevel)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun exitLevel() {
        _gamePlayState.value = GamePlayState()
        _currentLevel.value = null
    }

    // ===== DAILY REWARDS =====
    private fun loadDailyRewards() {
        viewModelScope.launch {
            try {
                val rewards = dailyRewardRepository.getAllActiveRewards()
                _dailyRewards.value = rewards

                val stats = dailyRewardRepository.getRewardStats()
                _dailyRewardStats.value = stats
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private suspend fun checkDailyRewardStreak() {
        dailyRewardRepository.checkAndResetStreak()
        loadDailyRewardStats()
    }

    private fun loadDailyRewardStats() {
        viewModelScope.launch {
            try {
                val stats = dailyRewardRepository.getRewardStats()
                _dailyRewardStats.value = stats
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun canClaimDailyReward(): Boolean {
        viewModelScope.launch {
            try {
                val canClaim = dailyRewardRepository.canClaimTodayReward()
                _uiState.value = _uiState.value.copy(canClaimDailyReward = canClaim)
            } catch (e: Exception) {
                handleError(e)
            }
        }
        return _uiState.value.canClaimDailyReward
    }

    fun getCurrentDayReward() {
        viewModelScope.launch {
            try {
                val currentReward = dailyRewardRepository.getCurrentDayReward()
                _uiState.value = _uiState.value.copy(currentDayReward = currentReward)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    // ===== UTILITY =====
    fun getCategoryProgress(categoryId: Int) {
        viewModelScope.launch {
            try {
                val (completed, total) = gameRepository.getCategoryProgress(categoryId)
                _uiState.value = _uiState.value.copy(
                    categoryProgress = Pair(completed, total)
                )
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun handleError(exception: Exception) {
        _uiState.value = _uiState.value.copy(
            error = exception.message ?: "Unknown error occurred",
            isLoading = false
        )
    }

    // ===== DATA INSERTION HELPERS (for initial setup) =====
    fun insertGameCategories(categories: List<GameCategory>) {
        viewModelScope.launch {
            try {
                gameRepository.insertGameCategories(categories)
                loadCategories()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun insertGameLevels(levels: List<GameLevel>) {
        viewModelScope.launch {
            try {
                gameRepository.insertGameLevels(levels)
                // Reload current category levels if applicable
                _currentCategory.value?.let { category ->
                    loadLevelsForCategory(category.id)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
}

// ===== UI STATE DATA CLASSES =====
data class GameUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val canClaimDailyReward: Boolean = false,
    val currentDayReward: DailyReward? = null,
    val categoryProgress: Pair<Int, Int>? = null
)

data class GamePlayState(
    val currentLevel: GameLevel? = null,
    val gameState: LevelGameState? = null,
    val availableLetters: List<String> = emptyList(),
    val playerAnswer: String = "",
    val isGameActive: Boolean = false,
    val isCompleted: Boolean = false,
    val isGameOver: Boolean = false,
    val isPaused: Boolean = false,
    val starsEarned: Int = 0
)