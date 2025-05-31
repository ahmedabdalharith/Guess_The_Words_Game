package com.pyramid.questions.data.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.pyramid.questions.core.Constants.WordCategory
import com.pyramid.questions.domain.model.LevelData
import com.pyramid.questions.domain.model.Player
import com.pyramid.questions.domain.model.QuestionCategory
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FirebaseGameManager {

    private val db: FirebaseFirestore = Firebase.firestore
    private val TAG = "FirebaseGameManager"

    // Collections
    private companion object {
        const val USERS_COLLECTION = "users"
        const val LEVELS_COLLECTION = "levels"
        const val PLAYER_STATS_COLLECTION = "player_stats"
        const val GAME_PROGRESS_COLLECTION = "game_progress"
        const val LEADERBOARD_COLLECTION = "leaderboard"
        const val DAILY_CHALLENGES_COLLECTION = "daily_challenges"
    }

    // User Management
    suspend fun createUser(userId: String, userData: Map<String, Any>): Boolean {
        return try {
            val userDoc = userData.toMutableMap()
            userDoc["createdAt"] = FieldValue.serverTimestamp()
            userDoc["lastActive"] = FieldValue.serverTimestamp()

            db.collection(USERS_COLLECTION)
                .document(userId)
                .set(userDoc)
                .await()

            Log.d(TAG, "User created successfully: $userId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error creating user: $userId", e)
            false
        }
    }

    suspend fun getUserData(userId: String): Map<String, Any>? {
        return try {
            val document = db.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                Log.d(TAG, "User data retrieved: $userId")
                document.data
            } else {
                Log.d(TAG, "User not found: $userId")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user data: $userId", e)
            null
        }
    }

    suspend fun updateUserLastActive(userId: String): Boolean {
        return try {
            db.collection(USERS_COLLECTION)
                .document(userId)
                .update("lastActive", FieldValue.serverTimestamp())
                .await()

            Log.d(TAG, "User last active updated: $userId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user last active: $userId", e)
            false
        }
    }

    // Level Management
    suspend fun saveLevelsData(gameMode: String, levels: List<LevelData>): Boolean {
        return try {
            val levelsMap = hashMapOf<String, Any>()
            levels.forEachIndexed { index, level ->
                levelsMap["level_$index"] = mapOf(
                    "levelNumber" to level.levelNumber,
                    "imageResId" to level.imageResId,
                    "category" to level.category.name,
                    "difficulty" to level.difficulty,
                    "isUnlocked" to level.isUnlocked,
                    "isSpecial" to level.isSpecial,
                    "specialTitle" to (level.specialTitle ?: ""),
                    "specialSubtitle" to (level.specialSubtitle ?: ""),
                    "progress" to (level.progress ?: ""),
                    "starsRequired" to (level.starsRequired ?: 0),
                    "coinsRequired" to (level.coinsRequired ?: 0),
                    "hasGift" to level.hasGift,
                    "newWordsCount" to level.newWordsCount
                )
            }

            db.collection(LEVELS_COLLECTION)
                .document(gameMode)
                .set(levelsMap)
                .await()

            Log.d(TAG, "Levels saved successfully for mode: $gameMode")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error saving levels for mode: $gameMode", e)
            false
        }
    }

    suspend fun getLevelsData(gameMode: String): List<LevelData>? {
        return try {
            val document = db.collection(LEVELS_COLLECTION)
                .document(gameMode)
                .get()
                .await()

            if (document.exists()) {
                val levelsData = document.data
                val levels = mutableListOf<LevelData>()

                levelsData?.forEach { (key, value) ->
                    if (key.startsWith("level_") && value is Map<*, *>) {
                        val levelMap = value as Map<String, Any>
                        val level = LevelData(
                            levelNumber = (levelMap["levelNumber"] as? Long)?.toInt() ?: 0,
                            imageResId = (levelMap["imageResId"] as? Long)?.toInt() ?: 0,
                            category = QuestionCategory.valueOf(levelMap["category"] as? String ?: "COUNTRIES"),
                            difficulty = (levelMap["difficulty"] as? Long)?.toInt() ?: 1,
                            isUnlocked = levelMap["isUnlocked"] as? Boolean == true,
                            isSpecial = levelMap["isSpecial"] as? Boolean == true,
                            specialTitle = (levelMap["specialTitle"] as? String).toString(),
                            specialSubtitle = (levelMap["specialSubtitle"] as? String).toString(),
                            progress = levelMap["progress"] as? String,
                            starsRequired = (levelMap["starsRequired"] as? Long)?.toInt(),
                            coinsRequired = (levelMap["coinsRequired"] as? Long)?.toInt(),
                            hasGift = levelMap["hasGift"] as? Boolean ?: false,
                            newWordsCount = (levelMap["newWordsCount"] as? Long)?.toInt() ?: 0
                        )
                        levels.add(level)
                    }
                }

                Log.d(TAG, "Levels retrieved successfully for mode: $gameMode")
                levels.sortedBy { it.levelNumber }
            } else {
                Log.d(TAG, "No levels found for mode: $gameMode")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting levels for mode: $gameMode", e)
            null
        }
    }

    suspend fun updateLevelProgress(userId: String, gameMode: String, levelNumber: Int, progress: String): Boolean {
        return try {
            val progressData = mapOf(
                "userId" to userId,
                "gameMode" to gameMode,
                "levelNumber" to levelNumber,
                "progress" to progress,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            db.collection(GAME_PROGRESS_COLLECTION)
                .document("${userId}_${gameMode}_$levelNumber")
                .set(progressData)
                .await()

            Log.d(TAG, "Level progress updated: $userId, $gameMode, Level $levelNumber")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error updating level progress", e)
            false
        }
    }

    suspend fun unlockLevel(userId: String, gameMode: String, levelNumber: Int, costCoins: Int? = null, costStars: Int? = null): Boolean {
        return try {
            val unlockData = mapOf(
                "userId" to userId,
                "gameMode" to gameMode,
                "levelNumber" to levelNumber,
                "unlockedAt" to FieldValue.serverTimestamp(),
                "costCoins" to costCoins,
                "costStars" to costStars
            )

            db.collection(GAME_PROGRESS_COLLECTION)
                .document("${userId}_${gameMode}_unlock_$levelNumber")
                .set(unlockData)
                .await()

            Log.d(TAG, "Level unlocked: $userId, $gameMode, Level $levelNumber")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error unlocking level", e)
            false
        }
    }

    // Player Stats Management
    suspend fun savePlayer(userId: String, stats: Player): Boolean {
        return try {
            val statsData = mapOf(
                "coins" to stats.coins,
                "stars" to stats.stars,
                "level" to stats.currentLevel,
                "experience" to stats.experience,
                "completedLevels" to stats.completedLevels,
                "totalPlayTime" to stats.totalPlayTime,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            db.collection(PLAYER_STATS_COLLECTION)
                .document(userId)
                .set(statsData)
                .await()

            Log.d(TAG, "Player stats saved: $userId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error saving player stats: $userId", e)
            false
        }
    }

    suspend fun getPlayer(userId: String): Player? {
        return try {
            val document = db.collection(PLAYER_STATS_COLLECTION)
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val data = document.data!!
                Player(
                    coins = (data["coins"] as? Long)?.toInt() ?: 0,
                    stars = (data["stars"] as? Long)?.toInt() ?: 0,
                    currentLevel = (data["level"] as? Long)?.toInt() ?: 1,
                    experience = (data["experience"] as? Long)?.toInt() ?: 0,
                    completedLevels = (data["completedLevels"] as? Long)?.toInt() ?: 0,
                    totalPlayTime = (data["totalPlayTime"] as? Long)?.toInt() ?: 0
                )
            } else {
                Log.d(TAG, "No player stats found: $userId")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting player stats: $userId", e)
            null
        }
    }

    suspend fun updatePlayerCoins(userId: String, coinsToAdd: Int): Boolean {
        return try {
            db.collection(PLAYER_STATS_COLLECTION)
                .document(userId)
                .update(
                    "coins", FieldValue.increment(coinsToAdd.toLong()),
                    "updatedAt", FieldValue.serverTimestamp()
                )
                .await()

            Log.d(TAG, "Player coins updated: $userId, added: $coinsToAdd")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error updating player coins: $userId", e)
            false
        }
    }

    suspend fun updatePlayerStars(userId: String, starsToAdd: Int): Boolean {
        return try {
            db.collection(PLAYER_STATS_COLLECTION)
                .document(userId)
                .update(
                    "stars", FieldValue.increment(starsToAdd.toLong()),
                    "updatedAt", FieldValue.serverTimestamp()
                )
                .await()

            Log.d(TAG, "Player stars updated: $userId, added: $starsToAdd")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error updating player stars: $userId", e)
            false
        }
    }

    // Daily Challenges
    suspend fun createDailyChallenge(date: String, challengeData: Map<String, Any>): Boolean {
        return try {
            val challenge = challengeData.toMutableMap()
            challenge["date"] = date
            challenge["createdAt"] = FieldValue.serverTimestamp()

            db.collection(DAILY_CHALLENGES_COLLECTION)
                .document(date)
                .set(challenge)
                .await()

            Log.d(TAG, "Daily challenge created: $date")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error creating daily challenge: $date", e)
            false
        }
    }

    suspend fun getDailyChallenge(date: String): Map<String, Any>? {
        return try {
            val document = db.collection(DAILY_CHALLENGES_COLLECTION)
                .document(date)
                .get()
                .await()

            if (document.exists()) {
                Log.d(TAG, "Daily challenge retrieved: $date")
                document.data
            } else {
                Log.d(TAG, "No daily challenge found: $date")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting daily challenge: $date", e)
            null
        }
    }

    suspend fun getTodaysDailyChallenge(): Map<String, Any>? {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return getDailyChallenge(today)
    }

    suspend fun completeDailyChallenge(userId: String, date: String, reward: Int): Boolean {
        return try {
            val completionData = mapOf(
                "userId" to userId,
                "date" to date,
                "reward" to reward,
                "completedAt" to FieldValue.serverTimestamp()
            )

            db.collection("daily_challenge_completions")
                .document("${userId}_$date")
                .set(completionData)
                .await()

            // Also update player coins
            updatePlayerCoins(userId, reward)

            Log.d(TAG, "Daily challenge completed: $userId, $date")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error completing daily challenge", e)
            false
        }
    }

    // Leaderboard Management
    suspend fun updateLeaderboard(userId: String, username: String, score: Int, gameMode: String): Boolean {
        return try {
            val leaderboardData = mapOf(
                "userId" to userId,
                "username" to username,
                "score" to score,
                "gameMode" to gameMode,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            db.collection(LEADERBOARD_COLLECTION)
                .document("${gameMode}_$userId")
                .set(leaderboardData)
                .await()

            Log.d(TAG, "Leaderboard updated: $userId, $gameMode, Score: $score")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error updating leaderboard", e)
            false
        }
    }

    suspend fun getLeaderboard(gameMode: String, limit: Int = 50): List<Map<String, Any>>? {
        return try {
            val querySnapshot = db.collection(LEADERBOARD_COLLECTION)
                .whereEqualTo("gameMode", gameMode)
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            val leaderboard = querySnapshot.documents.mapNotNull { it.data }
            Log.d(TAG, "Leaderboard retrieved: $gameMode, ${leaderboard.size} entries")
            leaderboard
        } catch (e: Exception) {
            Log.e(TAG, "Error getting leaderboard: $gameMode", e)
            null
        }
    }

    // Analytics and Tracking
    suspend fun logGameEvent(userId: String, eventType: String, eventData: Map<String, Any> = emptyMap()): Boolean {
        return try {
            val event = eventData.toMutableMap()
            event["userId"] = userId
            event["eventType"] = eventType
            event["timestamp"] = FieldValue.serverTimestamp()

            db.collection("game_events")
                .add(event)
                .await()

            Log.d(TAG, "Game event logged: $userId, $eventType")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error logging game event", e)
            false
        }
    }

    // Utility Methods
    suspend fun batchUpdateLevels(gameMode: String, updates: Map<Int, Map<String, Any>>): Boolean {
        return try {
            val batch = db.batch()
            val docRef = db.collection(LEVELS_COLLECTION).document(gameMode)

            updates.forEach { (levelNumber, updateData) ->
                val levelKey = "level_${levelNumber - 1}" // Assuming 0-based indexing
                updateData.forEach { (field, value) ->
                    batch.update(docRef, "$levelKey.$field", value)
                }
            }

            batch.commit().await()
            Log.d(TAG, "Batch level updates completed: $gameMode")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error in batch level updates: $gameMode", e)
            false
        }
    }

    suspend fun deleteUserData(userId: String): Boolean {
        return try {
            val batch = db.batch()

            // Delete from all collections
            val collections = listOf(
                USERS_COLLECTION,
                PLAYER_STATS_COLLECTION,
                "$GAME_PROGRESS_COLLECTION/$userId",
                "$LEADERBOARD_COLLECTION/$userId"
            )

            collections.forEach { collection ->
                val docRef = db.collection(collection).document(userId)
                batch.delete(docRef)
            }

            batch.commit().await()
            Log.d(TAG, "User data deleted: $userId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting user data: $userId", e)
            false
        }
    }

    // Callback-based methods for easier integration with existing code
    fun createUserWithCallback(userId: String, userData: Map<String, Any>, callback: (Boolean) -> Unit) {
        val userDoc = userData.toMutableMap()
        userDoc["createdAt"] = FieldValue.serverTimestamp()
        userDoc["lastActive"] = FieldValue.serverTimestamp()

        db.collection(USERS_COLLECTION)
            .document(userId)
            .set(userDoc)
            .addOnSuccessListener {
                Log.d(TAG, "User created successfully: $userId")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error creating user: $userId", e)
                callback(false)
            }
    }

    fun saveLevelsWithCallback(gameMode: String, levels: List<LevelData>, callback: (Boolean) -> Unit) {
        val levelsMap = hashMapOf<String, Any>()
        levels.forEachIndexed { index, level ->
            levelsMap["level_$index"] = mapOf(
                "levelNumber" to level.levelNumber,
                "imageResId" to level.imageResId,
                "category" to level.category.name,
                "difficulty" to level.difficulty,
                "isUnlocked" to level.isUnlocked,
                "isSpecial" to level.isSpecial,
                "specialTitle" to (level.specialTitle ?: ""),
                "specialSubtitle" to (level.specialSubtitle ?: ""),
                "progress" to (level.progress ?: ""),
                "starsRequired" to (level.starsRequired ?: 0),
                "coinsRequired" to (level.coinsRequired ?: 0),
                "hasGift" to level.hasGift,
                "newWordsCount" to level.newWordsCount
            )
        }

        db.collection(LEVELS_COLLECTION)
            .document(gameMode)
            .set(levelsMap)
            .addOnSuccessListener {
                Log.d(TAG, "Levels saved successfully for mode: $gameMode")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error saving levels for mode: $gameMode", e)
                callback(false)
            }
    }

    fun getLevelsWithCallback(gameMode: String, callback: (List<LevelData>?) -> Unit) {
        db.collection(LEVELS_COLLECTION)
            .document(gameMode)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val levelsData = document.data
                    val levels = mutableListOf<LevelData>()

                    levelsData?.forEach { (key, value) ->
                        if (key.startsWith("level_") && value is Map<*, *>) {
                            val levelMap = value as Map<String, Any>
                            val level = LevelData(
                                levelNumber = (levelMap["levelNumber"] as? Long)?.toInt() ?: 0,
                                imageResId = (levelMap["imageResId"] as? Long)?.toInt() ?: 0,
                                category = QuestionCategory.valueOf(levelMap["category"] as? String ?: "COUNTRIES"),
                                difficulty = (levelMap["difficulty"] as? Long)?.toInt() ?: 1,
                                isUnlocked = levelMap["isUnlocked"] as? Boolean == true,
                                isSpecial = levelMap["isSpecial"] as? Boolean == true,
                                specialTitle = (levelMap["specialTitle"] as? String).toString(),
                                specialSubtitle = (levelMap["specialSubtitle"] as? String).toString(),
                                progress = levelMap["progress"] as? String,
                                starsRequired = (levelMap["starsRequired"] as? Long)?.toInt(),
                                coinsRequired = (levelMap["coinsRequired"] as? Long)?.toInt(),
                                hasGift = levelMap["hasGift"] as? Boolean ?: false,
                                newWordsCount = (levelMap["newWordsCount"] as? Long)?.toInt() ?: 0
                            )
                            levels.add(level)
                        }
                    }

                    Log.d(TAG, "Levels retrieved successfully for mode: $gameMode")
                    callback(levels.sortedBy { it.levelNumber })
                } else {
                    Log.d(TAG, "No levels found for mode: $gameMode")
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error getting levels for mode: $gameMode", e)
                callback(null)
            }
    }
}