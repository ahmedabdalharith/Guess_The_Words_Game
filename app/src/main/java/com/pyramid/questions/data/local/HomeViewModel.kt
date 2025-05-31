package com.pyramid.questions.data.local

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val repository: GameRepository,
    private val preferencesManager: GamePreferencesManager
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<GameCategory>>(emptyList())
    val categories: StateFlow<List<GameCategory>> = _categories.asStateFlow()

    private val _playerProgress = MutableStateFlow<PlayerProgress?>(null)
    val playerProgress: StateFlow<PlayerProgress?> = _playerProgress.asStateFlow()

    private val _currentLevel = MutableStateFlow<GameLevel?>(null)
    val currentLevel: StateFlow<GameLevel?> = _currentLevel.asStateFlow()

    private val _unlockedCategories = MutableStateFlow<List<GameCategory>>(emptyList())
    val unlockedCategories: StateFlow<List<GameCategory>> = _unlockedCategories.asStateFlow()

    private val _normalLevels = MutableStateFlow<List<GameCategory>>(emptyList())
    val normalLevels: StateFlow<List<GameCategory>> = _normalLevels.asStateFlow()
    private val _premiumLevels = MutableStateFlow<List<GameCategory>>(emptyList())
    val premiumLevels: StateFlow<List<GameCategory>> = _premiumLevels.asStateFlow()
    private val _dailyLevels = MutableStateFlow<List<GameCategory>>(emptyList())
    val dailyLevels: StateFlow<List<GameCategory>> = _dailyLevels.asStateFlow()
    private val _vipLevels = MutableStateFlow<List<GameCategory>>(emptyList())
    val vipLevels: StateFlow<List<GameCategory>> = _vipLevels.asStateFlow()

    private val _dailyRewards = MutableStateFlow<List<DailyReward>>(emptyList())
    val dailyRewards: StateFlow<List<DailyReward>> = _dailyRewards.asStateFlow()



    // =====================================================
    // Initialization
    // =====================================================

    init {
        viewModelScope.launch {
            initializeViewModel()
        }
    }

    private suspend fun initializeViewModel() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        try {
            // Initialize game if first time
            if (preferencesManager.isFirstLaunch()) {
                initializeGameData()
                preferencesManager.setFirstLaunchCompleted()
            }

            // Load all initial data
            loadAllData()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isInitialized = true
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing ViewModel", e)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = e.message
            )
        }
    }

    private suspend fun initializeGameData() {
        try {
            repository.initializeGame()
            Log.d(TAG, "Game initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing game", e)
            throw e
        }
    }

    private suspend fun loadAllData() {
        try {
            // Load player progress
            loadPlayerProgress()

            // Load categories
            loadCategories()

            // Load current level
            loadCurrentLevel()

            // Update UI state
            updateGameStats()

        } catch (e: Exception) {
            Log.e(TAG, "Error loading data", e)
            throw e
        }
    }

    // =====================================================
    // Data Loading Functions
    // =====================================================

    private suspend fun loadPlayerProgress() {
        try {
            val progress = repository.getPlayerProgress()
            _playerProgress.value = progress
        } catch (e: Exception) {
            Log.e(TAG, "Error loading player progress", e)
        }
    }

    private suspend fun loadCategories() {
        try {
            val allCategories = repository.getAllCategories()
            val unlockedCats = repository.getUnlockedCategories()

            _categories.value = allCategories
            _unlockedCategories.value = unlockedCats

            // Update category progress
            updateCategoriesWithProgress(allCategories)

        } catch (e: Exception) {
            Log.e(TAG, "Error loading categories", e)
        }
    }

    private suspend fun updateCategoriesWithProgress(categories: List<GameCategory>) {
        val updatedCategories = categories.map { category ->
            val (completed, total) = repository.getCategoryProgress(category.id)
            category.copy(
                completedLevels = completed,
                totalLevels = total
            )
        }
        _categories.value = updatedCategories
    }

    private suspend fun loadCurrentLevel() {
        try {
            val nextLevel = repository.getNextUnlockedLevel()
            _currentLevel.value = nextLevel
        } catch (e: Exception) {
            Log.e(TAG, "Error loading current level", e)
        }
    }

    private suspend fun updateGameStats() {
        val progress = _playerProgress.value ?: return
        val categories = _categories.value

        _uiState.value = _uiState.value.copy(
            totalStars = progress.totalStars,
            totalCoins = progress.totalCoins,
            vipPoints = progress.vipPoints,
            completedLevels = progress.totalCompletedLevels,
            totalCategories = categories.size,
            unlockedCategories = categories.count { it.isUnlocked }
        )
    }

    // =====================================================
    // Category Management
    // =====================================================

    fun onCategorySelected(categoryId: Int) {
        viewModelScope.launch {
            try {
                val category = repository.getCategoryById(categoryId)
                if (category != null) {
                    if (category.isUnlocked) {
                        // Navigate to category levels
                        _uiState.value = _uiState.value.copy(
                            selectedCategoryId = categoryId,
                            showCategoryDialog = false
                        )
                    } else {
                        // Show unlock dialog
                        showUnlockCategoryDialog(category)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error selecting category", e)
                showError("خطأ في اختيار التصنيف")
            }
        }
    }

    private fun showUnlockCategoryDialog(category: GameCategory) {
        _uiState.value = _uiState.value.copy(
            showUnlockDialog = true,
            unlockDialogCategory = category
        )
    }

    fun onUnlockCategoryConfirmed(categoryId: Int) {
        viewModelScope.launch {
            try {
                val canUnlock = repository.canUnlockCategory(categoryId)
                if (canUnlock) {
                    repository.unlockCategory(categoryId)

                    // Deduct required resources
                    val category = repository.getCategoryById(categoryId)
                    category?.let {
                        deductUnlockCost(it)
                    }

                    // Refresh data
                    loadCategories()
                    loadPlayerProgress()
                    updateGameStats()

                    showSuccess("تم فتح التصنيف بنجاح!")
                } else {
                    showError("لا تملك الموارد الكافية لفتح هذا التصنيف")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error unlocking category", e)
                showError("خطأ في فتح التصنيف")
            }
        }

        dismissUnlockDialog()
    }

    private suspend fun deductUnlockCost(category: GameCategory) {
        val progress = _playerProgress.value ?: return

        val newProgress = progress.copy(
            totalStars = maxOf(0, progress.totalStars - category.requiredStars),
            totalCoins = maxOf(0, progress.totalCoins - category.requiredCoins),
            vipPoints = maxOf(0, progress.vipPoints - category.requiredVipPoints)
        )

        repository.updatePlayerProgress(newProgress)
    }

    fun dismissUnlockDialog() {
        _uiState.value = _uiState.value.copy(
            showUnlockDialog = false,
            unlockDialogCategory = null
        )
    }

    // =====================================================
    // Level Management
    // =====================================================

    fun onContinueGameClicked() {
        viewModelScope.launch {
            try {
                val nextLevel = repository.getNextUnlockedLevel()
                if (nextLevel != null) {
                    _uiState.value = _uiState.value.copy(
                        selectedLevelId = nextLevel.id,
                        navigateToLevel = true
                    )
                } else {
                    showError("لا توجد مستويات متاحة للعب")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error continuing game", e)
                showError("خطأ في متابعة اللعب")
            }
        }
    }

    fun onLevelSelected(levelId: Int) {
        _uiState.value = _uiState.value.copy(
            selectedLevelId = levelId,
            navigateToLevel = true
        )
    }

    fun onNavigationCompleted() {
        _uiState.value = _uiState.value.copy(
            navigateToLevel = false,
            selectedLevelId = null,
            selectedCategoryId = null
        )
    }

    // =====================================================
    // Game Progress Management
    // =====================================================

    fun onLevelCompleted(levelId: Int, starsEarned: Int, completionTime: Long) {
        viewModelScope.launch {
            try {
                repository.completeLevel(levelId, starsEarned, completionTime)

                // Refresh all data
                loadAllData()

                showSuccess("مبروك! تم إكمال المستوى بنجاح")

            } catch (e: Exception) {
                Log.e(TAG, "Error completing level", e)
                showError("خطأ في حفظ تقدم المستوى")
            }
        }
    }

    fun updateCoins(amount: Int) {
        viewModelScope.launch {
            try {
                val progress = _playerProgress.value ?: return@launch
                val newProgress = progress.copy(
                    totalCoins = maxOf(0, progress.totalCoins + amount)
                )
                repository.updatePlayerProgress(newProgress)
                _playerProgress.value = newProgress
                updateGameStats()
            } catch (e: Exception) {
                Log.e(TAG, "Error updating coins", e)
            }
        }
    }

    fun updateStars(amount: Int) {
        viewModelScope.launch {
            try {
                val progress = _playerProgress.value ?: return@launch
                val newProgress = progress.copy(
                    totalStars = maxOf(0, progress.totalStars + amount)
                )
                repository.updatePlayerProgress(newProgress)
                _playerProgress.value = newProgress
                updateGameStats()
            } catch (e: Exception) {
                Log.e(TAG, "Error updating stars", e)
            }
        }
    }

    fun updateVipPoints(amount: Int) {
        viewModelScope.launch {
            try {
                val progress = _playerProgress.value ?: return@launch
                val newProgress = progress.copy(
                    vipPoints = maxOf(0, progress.vipPoints + amount)
                )
                repository.updatePlayerProgress(newProgress)
                _playerProgress.value = newProgress
                updateGameStats()
            } catch (e: Exception) {
                Log.e(TAG, "Error updating VIP points", e)
            }
        }
    }

    // =====================================================
    // Hint System
    // =====================================================

    fun useHint(levelId: Int, hintType: HintType, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val success = repository.useHint(levelId, hintType)
                if (success) {
                    loadPlayerProgress() // Refresh coins
                    updateGameStats()
                }

                withContext(Dispatchers.Main) {
                    onResult(success)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error using hint", e)
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }

    fun canAffordHint(hintCost: Int): Boolean {
        return (_playerProgress.value?.totalCoins ?: 0) >= hintCost
    }

    // =====================================================
    // Game State Management
    // =====================================================

    fun saveLevelState(levelState: LevelGameState) {
        viewModelScope.launch {
            try {
                repository.saveLevelState(levelState)
            } catch (e: Exception) {
                Log.e(TAG, "Error saving level state", e)
            }
        }
    }

    fun loadLevelState(levelId: Int, onResult: (LevelGameState?) -> Unit) {
        viewModelScope.launch {
            try {
                val state = repository.getLevelState(levelId)
                withContext(Dispatchers.Main) {
                    onResult(state)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading level state", e)
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }

    // =====================================================
    // Utility Functions
    // =====================================================

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                loadAllData()
            } catch (e: Exception) {
                Log.e(TAG, "Error refreshing data", e)
                showError("خطأ في تحديث البيانات")
            } finally {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(
            error = message,
            showErrorDialog = true
        )
    }

    private fun showSuccess(message: String) {
        _uiState.value = _uiState.value.copy(
            successMessage = message,
            showSuccessDialog = true
        )
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(
            error = null,
            showErrorDialog = false
        )
    }

    fun dismissSuccess() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            showSuccessDialog = false
        )
    }

    // =====================================================
    // Statistics and Analytics
    // =====================================================

    fun getGameStatistics(): GameStatistics {
        val progress = _playerProgress.value
        val categories = _categories.value

        return GameStatistics(
            totalStars = progress?.totalStars ?: 0,
            totalCoins = progress?.totalCoins ?: 0,
            vipPoints = progress?.vipPoints ?: 0,
            completedLevels = progress?.totalCompletedLevels ?: 0,
            totalPlayTime = progress?.totalPlayTime ?: 0,
            hintsUsed = progress?.hintsUsed ?: 0,
            perfectLevels = progress?.perfectLevels ?: 0,
            totalCategories = categories.size,
            unlockedCategories = categories.count { it.isUnlocked },
            completionPercentage = calculateCompletionPercentage(categories)
        )
    }

    private fun calculateCompletionPercentage(categories: List<GameCategory>): Float {
        if (categories.isEmpty()) return 0f

        val totalLevels = categories.sumOf { it.totalLevels }
        val completedLevels = categories.sumOf { it.completedLevels }

        return if (totalLevels > 0) {
            (completedLevels.toFloat() / totalLevels.toFloat()) * 100f
        } else 0f
    }
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "HomeViewModel cleared")
    }

    fun insertGameCategories(categories: List<GameCategory>) {
        viewModelScope.launch {
            try {
                repository.insertGameCategories(categories)
                loadCategories() // Refresh categories after insert
                Log.d(
                    TAG, "Game categories inserted successfully: ${categories.size} categories"
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting game categories", e)
            }
        }
    }
    fun insertGameLevels(levels: List<GameLevel>) {
        viewModelScope.launch {
            try {
                repository.insertGameLevels(levels)
                loadCurrentLevel() // Refresh current level after insert
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting game levels", e)
            }
        }
    }
    fun getGameCategories(categoryId: Int): GameCategory? {
        return _categories.value.find { it.id == categoryId }
    }
    fun getVipCategories(): List<GameCategory> {
        return _categories.value.filter { it.isVipCategory }
    }
    fun getNormalCategories(): List<GameCategory> {
        return _categories.value.filter { !it.isVipCategory && !it.isDailyCategory }
    }
    fun getDailyCategories(): List<GameCategory> {
        return _categories.value.filter { it.isDailyCategory  }
    }
    fun updateCategory(updatedCategory: GameCategory) {
        viewModelScope.launch {
            try {
                repository.updateCategory(updatedCategory)
                loadCategories() // Refresh categories after update
            } catch (e: Exception) {
                Log.e(TAG, "Error updating category", e)
            }
        }
    }

    fun loadDailyRewards() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val dailyRewards = repository.getDailyRewards()
                _dailyRewards.value = dailyRewards
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading daily rewards", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "خطأ في تحميل المكافآت اليومية"
                )
            }
        }
    }

}

// =====================================================
// Data Classes
// =====================================================

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isInitialized: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val showErrorDialog: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val showUnlockDialog: Boolean = false,
    val showCategoryDialog: Boolean = false,
    val unlockDialogCategory: GameCategory? = null,
    val selectedCategoryId: Int? = null,
    val selectedLevelId: Int? = null,
    val navigateToLevel: Boolean = false,
    val totalStars: Int = 0,
    val totalCoins: Int = 0,
    val vipPoints: Int = 0,
    val completedLevels: Int = 0,
    val totalCategories: Int = 0,
    val unlockedCategories: Int = 0
)

data class GameStatistics(
    val totalStars: Int,
    val totalCoins: Int,
    val vipPoints: Int,
    val completedLevels: Int,
    val totalPlayTime: Long,
    val hintsUsed: Int,
    val perfectLevels: Int,
    val totalCategories: Int,
    val unlockedCategories: Int,
    val completionPercentage: Float
)
