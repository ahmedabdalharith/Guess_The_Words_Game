package com.pyramid.questions.data.local

import android.content.Context
import android.content.SharedPreferences
import com.pyramid.questions.core.Constants.WordCategory
import com.pyramid.questions.domain.model.Player
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

class GamePreferencesManager(private val context: Context) {

    companion object {
        private const val PREF_NAME = "guess_words_game_prefs"

        // Player Stats Keys
        private const val KEY_PLAYER_STARS = "player_stars"
        private const val KEY_PLAYER_COINS = "player_coins"
        private const val KEY_PLAYER_LEVEL = "player_level"
        private const val KEY_PLAYER_XP = "player_xp"
        private const val KEY_PLAYER_TOTAL_XP = "player_total_xp"
        private const val KEY_PLAYER_STREAK = "player_streak"
        private const val KEY_PLAYER_USERNAME = "player_username"

        // Game Progress Keys
        private const val KEY_LEVEL_PROGRESS = "level_progress_"
        private const val KEY_UNLOCKED_LEVELS = "unlocked_levels"
        private const val KEY_COMPLETED_LEVELS = "completed_levels"
        private const val KEY_LEVEL_STARS = "level_stars_"

        // Settings Keys
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_MUSIC_ENABLED = "music_enabled"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_LANGUAGE = "language"

        // Daily Challenge Keys
        private const val KEY_DAILY_CHALLENGE_DATE = "daily_challenge_date"
        private const val KEY_DAILY_CHALLENGE_PROGRESS = "daily_challenge_progress"
        private const val KEY_DAILY_CHALLENGE_COMPLETED = "daily_challenge_completed"
        private const val KEY_LAST_DAILY_REWARD = "last_daily_reward"
        private const val KEY_DAILY_STREAK = "daily_streak"

        // Premium Features
        private const val KEY_IS_PREMIUM = "is_premium"
        private const val KEY_PREMIUM_LEVELS_UNLOCKED = "premium_levels_unlocked"

        // Tutorial & First Launch
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_TUTORIAL_COMPLETED = "tutorial_completed"
        private const val KEY_SHOW_HINTS = "show_hints"

        // Ad Related
        private const val KEY_LAST_AD_WATCH = "last_ad_watch"
        private const val KEY_AD_FREE_UNTIL = "ad_free_until"
        private const val KEY_REWARDED_AD_COUNT = "rewarded_ad_count"

        // username id
        private const val KEY_PLAYER_USERNAME_ID = "player_username_id"


    }
    // Add to GamePreferencesManager class
    fun getLastDailyRewardDate(): String {
        return sharedPreferences.getString("last_daily_reward_date", "") ?: ""
    }

    fun setLastDailyRewardDate(date: String) {
        sharedPreferences.edit { putString("last_daily_reward_date", date) }
    }

    fun getDailyRewardStreak(): Int {
        return sharedPreferences.getInt("daily_reward_streak", 0)
    }

    fun setDailyRewardStreak(streak: Int) {
        sharedPreferences.edit { putInt("daily_reward_streak", streak) }
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // Player Stats Management
    fun savePlayer(player: Player) {
        sharedPreferences.edit {
            putInt(KEY_PLAYER_STARS, player.stars)
            putInt(KEY_PLAYER_COINS, player.coins)
            putInt(KEY_PLAYER_LEVEL, player.currentLevel)
            putInt(KEY_PLAYER_XP, player.xp)
            putInt(KEY_PLAYER_TOTAL_XP, player.totalXp)
            putInt(KEY_PLAYER_STREAK, player.streak)
        }
    }

    fun getPlayer(): Player {
        return Player(
            stars = sharedPreferences.getInt(KEY_PLAYER_STARS, 0),
            coins = sharedPreferences.getInt(KEY_PLAYER_COINS, 100),
            currentLevel = sharedPreferences.getInt(KEY_PLAYER_LEVEL, 1),
            xp = sharedPreferences.getInt(KEY_PLAYER_XP, 0),
            totalXp = sharedPreferences.getInt(KEY_PLAYER_TOTAL_XP, 100),
            streak = sharedPreferences.getInt(KEY_PLAYER_STREAK, 0)
        )
    }

    fun addCoins(amount: Int) {
        val currentCoins = sharedPreferences.getInt(KEY_PLAYER_COINS, 0)
        sharedPreferences.edit {
            putInt(KEY_PLAYER_COINS, currentCoins + amount)
        }
    }

    fun spendCoins(amount: Int): Boolean {
        val currentCoins = sharedPreferences.getInt(KEY_PLAYER_COINS, 0)
        return if (currentCoins >= amount) {
            sharedPreferences.edit {
                putInt(KEY_PLAYER_COINS, currentCoins - amount)
            }
            true
        } else {
            false
        }
    }

    fun addStars(amount: Int) {
        val currentStars = sharedPreferences.getInt(KEY_PLAYER_STARS, 0)
        sharedPreferences.edit {
            putInt(KEY_PLAYER_STARS, currentStars + amount)
        }
    }

    // Username Management
    fun setUsername(username: String) {
        sharedPreferences.edit {
            putString(KEY_PLAYER_USERNAME, username)
        }
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_PLAYER_USERNAME, null)
    }
    fun setUsernameId(username: String) {
        sharedPreferences.edit {
            putString(KEY_PLAYER_USERNAME_ID, username)
        }
    }

    fun getUsernameId(): String? {
        return sharedPreferences.getString(KEY_PLAYER_USERNAME_ID, null)
    }

    // Level Progress Management
    fun saveLevelProgress(levelId: Int, category: WordCategory, currentWords: Int, totalWords: Int) {
        val progressKey = "$KEY_LEVEL_PROGRESS${levelId}_${category.name}"
        sharedPreferences.edit {
            putString(progressKey, "$currentWords/$totalWords")
        }
    }

    fun getLevelProgress(levelId: Int, category: WordCategory): String? {
        val progressKey = "$KEY_LEVEL_PROGRESS${levelId}_${category.name}"
        return sharedPreferences.getString(progressKey, null)
    }

    fun setLevelCompleted(levelId: Int, category: WordCategory, stars: Int) {
        val completedLevels = getCompletedLevels().toMutableSet()
        completedLevels.add("${levelId}_${category.name}")

        val starsKey = "$KEY_LEVEL_STARS${levelId}_${category.name}"

        sharedPreferences.edit {
            putStringSet(KEY_COMPLETED_LEVELS, completedLevels)
            putInt(starsKey, stars)
        }
    }

    fun isLevelCompleted(levelId: Int, category: WordCategory): Boolean {
        val completedLevels = getCompletedLevels()
        return completedLevels.contains("${levelId}_${category.name}")
    }

    fun getLevelStars(levelId: Int, category: WordCategory): Int {
        val starsKey = "$KEY_LEVEL_STARS${levelId}_${category.name}"
        return sharedPreferences.getInt(starsKey, 0)
    }

    private fun getCompletedLevels(): Set<String> {
        return sharedPreferences.getStringSet(KEY_COMPLETED_LEVELS, emptySet()) ?: emptySet()
    }

    // Level Unlock Management
    fun unlockLevel(levelId: Int, category: WordCategory) {
        val unlockedLevels = getUnlockedLevels().toMutableSet()
        unlockedLevels.add("${levelId}_${category.name}")

        sharedPreferences.edit {
            putStringSet(KEY_UNLOCKED_LEVELS, unlockedLevels)
        }
    }

    fun isLevelUnlocked(levelId: Int, category: WordCategory): Boolean {
        // Level 1 is always unlocked
        if (levelId == 1) return true

        val unlockedLevels = getUnlockedLevels()
        return unlockedLevels.contains("${levelId}_${category.name}")
    }

    private fun getUnlockedLevels(): Set<String> {
        return sharedPreferences.getStringSet(KEY_UNLOCKED_LEVELS, setOf("1_COUNTRIES"))
            ?: setOf("1_COUNTRIES")
    }

    // Settings Management
    fun setSoundEnabled(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_SOUND_ENABLED, enabled)
        }
    }

    fun isSoundEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_SOUND_ENABLED, true)
    }

    fun setMusicEnabled(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_MUSIC_ENABLED, enabled)
        }
    }

    fun isMusicEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_MUSIC_ENABLED, true)
    }

    fun setVibrationEnabled(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_VIBRATION_ENABLED, enabled)
        }
    }

    fun isVibrationEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_VIBRATION_ENABLED, true)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
        }
    }

    fun areNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    }

    fun setLanguage(language: String) {
        sharedPreferences.edit {
            putString(KEY_LANGUAGE, language)
        }
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    // Daily Challenge Management
    fun setDailyChallengeProgress(date: String, progress: String) {
        sharedPreferences.edit {
            putString(KEY_DAILY_CHALLENGE_DATE, date)
            putString(KEY_DAILY_CHALLENGE_PROGRESS, progress)
        }
    }

    fun getDailyChallengeProgress(): Pair<String?, String?> {
        val date = sharedPreferences.getString(KEY_DAILY_CHALLENGE_DATE, null)
        val progress = sharedPreferences.getString(KEY_DAILY_CHALLENGE_PROGRESS, null)
        return Pair(date, progress)
    }

    fun setDailyChallengeCompleted(date: String, completed: Boolean) {
        sharedPreferences.edit()
            .putBoolean("${KEY_DAILY_CHALLENGE_COMPLETED}_$date", completed)
            .apply()
    }

    fun isDailyChallengeCompleted(date: String): Boolean {
        return sharedPreferences.getBoolean("${KEY_DAILY_CHALLENGE_COMPLETED}_$date", false)
    }

    fun setLastDailyReward(timestamp: Long) {
        sharedPreferences.edit {
            putLong(KEY_LAST_DAILY_REWARD, timestamp)
        }
    }

    fun getLastDailyReward(): Long {
        return sharedPreferences.getLong(KEY_LAST_DAILY_REWARD, 0)
    }

    fun incrementDailyStreak() {
        val currentStreak = sharedPreferences.getInt(KEY_DAILY_STREAK, 0)
        sharedPreferences.edit()
            .putInt(KEY_DAILY_STREAK, currentStreak + 1)
            .apply()
    }

    fun resetDailyStreak() {
        sharedPreferences.edit()
            .putInt(KEY_DAILY_STREAK, 0)
            .apply()
    }

    fun getDailyStreak(): Int {
        return sharedPreferences.getInt(KEY_DAILY_STREAK, 0)
    }

    // Premium Features
    fun setPremium(isPremium: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_IS_PREMIUM, isPremium)
        }
    }

    fun isPremium(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_PREMIUM, false)
    }

    fun unlockPremiumLevels() {
        sharedPreferences.edit {
            putBoolean(KEY_PREMIUM_LEVELS_UNLOCKED, true)
        }
    }

    fun arePremiumLevelsUnlocked(): Boolean {
        return sharedPreferences.getBoolean(KEY_PREMIUM_LEVELS_UNLOCKED, false)
    }

    // Tutorial & First Launch
    fun setFirstLaunch(isFirst: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_FIRST_LAUNCH, isFirst)
            .apply()
    }

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setTutorialCompleted(completed: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_TUTORIAL_COMPLETED, completed)
            .apply()
    }

    fun isTutorialCompleted(): Boolean {
        return sharedPreferences.getBoolean(KEY_TUTORIAL_COMPLETED, false)
    }

    fun setShowHints(show: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_SHOW_HINTS, show)
            .apply()
    }

    fun shouldShowHints(): Boolean {
        return sharedPreferences.getBoolean(KEY_SHOW_HINTS, true)
    }

    // Ad Management
    fun setLastAdWatch(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(KEY_LAST_AD_WATCH, timestamp)
            .apply()
    }

    fun getLastAdWatch(): Long {
        return sharedPreferences.getLong(KEY_LAST_AD_WATCH, 0)
    }

    fun setAdFreeUntil(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(KEY_AD_FREE_UNTIL, timestamp)
            .apply()
    }

    fun getAdFreeUntil(): Long {
        return sharedPreferences.getLong(KEY_AD_FREE_UNTIL, 0)
    }

    fun incrementRewardedAdCount() {
        val currentCount = sharedPreferences.getInt(KEY_REWARDED_AD_COUNT, 0)
        sharedPreferences.edit()
            .putInt(KEY_REWARDED_AD_COUNT, currentCount + 1)
            .apply()
    }

    fun getRewardedAdCount(): Int {
        return sharedPreferences.getInt(KEY_REWARDED_AD_COUNT, 0)
    }

    // Utility Methods
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }

    fun exportData(): String {
        val allPrefs = sharedPreferences.all
        return gson.toJson(allPrefs)
    }

    fun importData(jsonData: String): Boolean {
        return try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val dataMap: Map<String, Any> = gson.fromJson(jsonData, type)

            with(sharedPreferences.edit()) {
                clear()
                dataMap.forEach { (key, value) ->
                    when (value) {
                        is String -> putString(key, value)
                        is Int -> putInt(key, value)
                        is Boolean -> putBoolean(key, value)
                        is Float -> putFloat(key, value)
                        is Long -> putLong(key, value)
                        is Set<*> -> putStringSet(key, value as Set<String>)
                    }
                }
                apply()
            }
            true
        } catch (e: Exception) {
            false
        }
    }


    fun setLastSpinTime(time: Long) {
        sharedPreferences.edit().putLong("last_spin_time", time).apply()
    }

    fun getLastSpinTime(): Long {
        return sharedPreferences.getLong("last_spin_time", 0L)
    }

    fun isSpinAllowed(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastSpinTime = getLastSpinTime()
        return currentTime - lastSpinTime >= 8 * 60 * 60 * 1000L
    }

    fun getTimeUntilNextSpin(): Long {
        val currentTime = System.currentTimeMillis()
        val lastSpinTime = getLastSpinTime()
        val waitTime = 8 * 60 * 60 * 1000L
        return waitTime - (currentTime - lastSpinTime)
    }
    fun saveLives(lives: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("current_lives", lives)
        editor.apply()
    }

    fun getLives(): Int {
        return sharedPreferences.getInt("current_lives", 5)
    }

    fun saveIsRecharging(isRecharging: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_recharging", isRecharging)
        editor.apply()
    }

    fun getIsRecharging(): Boolean {
        return sharedPreferences.getBoolean("is_recharging", false)
    }

    fun saveRechargeStartTime(timeMillis: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong("recharge_start_time", timeMillis)
        editor.apply()
    }

    fun getRechargeStartTime(): Long {
        return sharedPreferences.getLong("recharge_start_time", 0L)
    }

    fun saveTimeRemaining(timeSeconds: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("time_remaining", timeSeconds)
        editor.apply()
    }

    fun getTimeRemaining(): Int {
        return sharedPreferences.getInt("time_remaining", 1800)
    }
    fun resetLastSpinTime() {
        val sharedPrefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putLong("last_spin_time", 0L)
            .apply()
    }

    fun setLastDailyRewardClaim(lng: Long) {
        sharedPreferences.edit {
            putLong("last_daily_reward_claim", lng)
        }
    }

    fun setFirstLaunchCompleted() = sharedPreferences.edit()
        .putBoolean(KEY_FIRST_LAUNCH, false)
        .apply()

}

/**
 * تنسيق الوقت المتبقي لعرضه للمستخدم
 */
fun formatTimeRemaining(timeRemaining: Long): String {
    val hours = timeRemaining / (1000 * 60 * 60)
    val minutes = (timeRemaining % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (timeRemaining % (1000 * 60)) / 1000

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
        else -> String.format("00:%02d", seconds)
    }
}


// Extension functions for easier usage
fun GamePreferencesManager.hasEnoughCoins(amount: Int): Boolean {
    return getPlayer().coins >= amount
}

fun GamePreferencesManager.hasEnoughStars(amount: Int): Boolean {
    return getPlayer().stars >= amount
}

fun GamePreferencesManager.canWatchRewardedAd(): Boolean {
    val lastWatch = getLastAdWatch()
    val currentTime = System.currentTimeMillis()
    val timeDiff = currentTime - lastWatch
    // Allow watching ad every 5 minutes (300000 ms)
    return timeDiff >= 300000
}

fun GamePreferencesManager.isAdFree(): Boolean {
    return System.currentTimeMillis() < getAdFreeUntil()
}
