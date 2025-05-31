package com.pyramid.questions.data.local

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pyramid.questions.R

@Entity(tableName = "categories")
data class GameCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val category: GameCategoryType,

    @ColumnInfo(name = "special_title")
    val specialTitle: String= "", // عنوان خاص للتصنيف (اختياري)

    @ColumnInfo(name = "description")
    val description: String, // وصف التصنيف

    @ColumnInfo(name = "icon_resource")
    val iconResource: Int, // أيقونة التصنيف

    @ColumnInfo(name = "background_color")
    val backgroundColor: String, // لون خلفية التصنيف

    @ColumnInfo(name = "required_stars")
    var requiredStars: Int = 0, // النجوم المطلوبة لفتح التصنيف

    @ColumnInfo(name = "required_coins")
    var requiredCoins: Int = 0, // العملات المطلوبة لفتح التصنيف

    @ColumnInfo(name = "required_vip_points")
    val requiredVipPoints: Int = 0, // نقاط VIP المطلوبة

    @ColumnInfo(name = "is_vip_category")
    val isVipCategory: Boolean = false, // هل التصنيف مميز
    @ColumnInfo(name = "is_daily_category")
    val isDailyCategory: Boolean = false,
    @ColumnInfo(name = "is_unlocked")
    var isUnlocked: Boolean = false, // هل التصنيف مفتوح

    @ColumnInfo(name = "order_index")
    val orderIndex: Int = 0, // ترتيب عرض التصنيف

    @ColumnInfo(name = "total_levels")
    var totalLevels: Int = 0, // إجمالي المستويات في هذا التصنيف

    @ColumnInfo(name = "completed_levels")
    var completedLevels: Int = 0 // المستويات المكتملة
)

@Entity(
    tableName = "game_levels",
    foreignKeys = [
        ForeignKey(
            entity = GameCategory::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["category_id"], name = "index_levels_category_id")]
)
data class GameLevel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "category_id")
    val categoryId: Int, // ربط بالتصنيف

    @ColumnInfo(name = "level_number")
    val levelNumber: Int, // رقم المستوى في التصنيف

    @ColumnInfo(name = "question_text")
    val questionText: String, // نص السؤال

    @ColumnInfo(name = "question_image")
    val questionImage: Int, // صورة السؤال

    @ColumnInfo(name = "correct_answer")
    val correctAnswer: String, // الإجابة الصحيحة

    @ColumnInfo(name = "available_letters")
    val availableLetters: String, // الحروف المتاحة للاختيار (مفصولة بفاصلة)

    @ColumnInfo(name = "answer_length")
    val answerLength: Int, // طول الإجابة المطلوبة

    @ColumnInfo(name = "max_lives")
    val maxLives: Int = 3, // عدد المحاولات المسموحة

    @ColumnInfo(name = "hint_cost")
    val hintCost: Int = 10, // تكلفة التلميح بالعملات

    @ColumnInfo(name = "stars_reward")
    val starsReward: Int = 3, // مكافأة النجوم عند الإكمال

    @ColumnInfo(name = "coins_reward")
    val coinsReward: Int = 5, // مكافأة العملات عند الإكمال

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false, // هل تم إكمال المستوى

    @ColumnInfo(name = "is_unlocked")
    val isUnlocked: Boolean = false, // هل المستوى مفتوح

    @ColumnInfo(name = "stars_earned")
    val starsEarned: Int = 0, // النجوم المكتسبة من هذا المستوى

    @ColumnInfo(name = "completion_time")
    val completionTime: Long = 0 // وقت إكمال المستوى (بالثواني)
)

@Entity(tableName = "player_progress")
data class PlayerProgress(
    @PrimaryKey
    val id: Int = 1, // ID ثابت لأنه لاعب واحد فقط

    @ColumnInfo(name = "total_stars")
    val totalStars: Int = 0, // إجمالي النجوم المكتسبة

    @ColumnInfo(name = "total_coins")
    val totalCoins: Int = 100, // إجمالي العملات (البداية 100)

    @ColumnInfo(name = "vip_points")
    val vipPoints: Int = 0, // نقاط VIP

    @ColumnInfo(name = "current_level_id")
    val currentLevelId: Int = 1, // ID المستوى الحالي

    @ColumnInfo(name = "total_completed_levels")
    val totalCompletedLevels: Int = 0, // إجمالي المستويات المكتملة

    @ColumnInfo(name = "total_play_time")
    val totalPlayTime: Long = 0, // إجمالي وقت اللعب (بالثواني)

    @ColumnInfo(name = "hints_used")
    val hintsUsed: Int = 0, // إجمالي التلميحات المستخدمة

    @ColumnInfo(name = "perfect_levels")
    val perfectLevels: Int = 0, // المستويات المكتملة بدون أخطاء

    @ColumnInfo(name = "last_played")
    val lastPlayed: Long = System.currentTimeMillis() // آخر وقت لعب
)


@Entity(
    tableName = "level_game_states",
    foreignKeys = [
        ForeignKey(
            entity = GameLevel::class,
            parentColumns = ["id"],
            childColumns = ["level_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LevelGameState(
    @PrimaryKey
    @ColumnInfo(name = "level_id")
    val levelId: Int,

    @ColumnInfo(name = "current_lives")
    val currentLives: Int, // المحاولات المتبقية

    @ColumnInfo(name = "player_answer")
    val playerAnswer: String = "", // إجابة اللاعب الحالية

    @ColumnInfo(name = "used_letters")
    val usedLetters: String = "", // الحروف المستخدمة (مفصولة بفاصلة)

    @ColumnInfo(name = "hints_used_count")
    val hintsUsedCount: Int = 0, // عدد التلميحات المستخدمة

    @ColumnInfo(name = "removed_letters")
    val removedLetters: String = "", // الحروف المحذوفة بالتلميح

    @ColumnInfo(name = "start_time")
    val startTime: Long = System.currentTimeMillis(), // وقت بداية المستوى

    @ColumnInfo(name = "pause_time")
    val pauseTime: Long = 0, // وقت الإيقاف المؤقت

    @ColumnInfo(name = "is_paused")
    val isPaused: Boolean = false // هل اللعبة متوقفة مؤقتاً
)

@Entity(
    tableName = "category_stats",
    foreignKeys = [
        ForeignKey(
            entity = GameCategory::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategoryStats(
    @PrimaryKey
    @ColumnInfo(name = "category_id")
    val categoryId: Int,

    @ColumnInfo(name = "completed_levels")
    val completedLevels: Int = 0, // المستويات المكتملة

    @ColumnInfo(name = "total_levels")
    val totalLevels: Int = 0, // إجمالي المستويات

    @ColumnInfo(name = "stars_earned")
    val starsEarned: Int = 0, // النجوم المكتسبة من هذا التصنيف

    @ColumnInfo(name = "best_time")
    val bestTime: Long = 0, // أفضل وقت في التصنيف

    @ColumnInfo(name = "last_played_level")
    val lastPlayedLevel: Int = 0 // آخر مستوى تم لعبه
)

@Entity(tableName = "daily_reward")
data class DailyReward(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "current_Day")
    val currentDay: Int,

    @ColumnInfo(name = "reward_type")
    val rewardType: DailyRewardType, // نوع المكافأة

    @ColumnInfo(name = "reward_amount")
    val rewardAmount: Int, // كمية المكافأة

    @ColumnInfo(name = "reward_icon")
    val rewardIcon: Int,

    @ColumnInfo(name = "is_special_reward")
    val isSpecialReward: Boolean = false,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true ,

    @ColumnInfo(name = "is_collected")
    val isCollected: Boolean = false

)


@Entity(
    tableName = "daily_reward_claims",
    foreignKeys = [
        ForeignKey(
            entity = DailyReward::class,
            parentColumns = ["id"],
            childColumns = ["reward_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["reward_id"], name = "index_claims_reward_id")]
)
data class DailyRewardClaim(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "reward_id")
    val rewardId: Int, // ربط بالمكافأة

    @ColumnInfo(name = "claim_date")
    val claimDate: Long = System.currentTimeMillis(), // تاريخ الاستلام

    @ColumnInfo(name = "day_streak")
    val dayStreak: Int, // عدد الأيام المتتالية

    @ColumnInfo(name = "claimed_amount")
    val claimedAmount: Int, // الكمية المستلمة

    @ColumnInfo(name = "bonus_multiplier")
    val bonusMultiplier: Float = 1.0f // مضاعف المكافأة (للأيام الخاصة)
)


@Entity(tableName = "daily_reward_stats")
data class DailyRewardStats(
    @PrimaryKey
    val id: Int = 1, // ID ثابت

    @ColumnInfo(name = "current_streak")
    val currentStreak: Int = 0, // السلسلة الحالية من الأيام

    @ColumnInfo(name = "longest_streak")
    val longestStreak: Int = 0, // أطول سلسلة أيام

    @ColumnInfo(name = "total_claims")
    val totalClaims: Int = 0, // إجمالي المكافآت المستلمة

    @ColumnInfo(name = "last_claim_date")
    val lastClaimDate: Long = 0, // آخر يوم استلام

    @ColumnInfo(name = "next_reset_date")
    val nextResetDate: Long = 0, // تاريخ إعادة تعيين السلسلة

    @ColumnInfo(name = "total_coins_earned")
    val totalCoinsEarned: Int = 0, // إجمالي العملات من المكافآت

    @ColumnInfo(name = "total_stars_earned")
    val totalStarsEarned: Int = 0, // إجمالي النجوم من المكافآت

    @ColumnInfo(name = "total_vip_points_earned")
    val totalVipPointsEarned: Int = 0, // إجمالي نقاط VIP

    @ColumnInfo(name = "missed_days")
    val missedDays: Int = 0 // الأيام المفقودة
)
enum class DailyRewardType {
    COINS,
    STARS,
    VIP_POINTS,
    SPECIAL_GIFT
}
@Dao
interface GameCategoryDao {
    @Query("SELECT * FROM categories ORDER BY order_index ASC")
    suspend fun getAllCategories(): List<GameCategory>

    @Query("SELECT * FROM categories WHERE is_unlocked = 1 ORDER BY order_index ASC")
    suspend fun getUnlockedCategories(): List<GameCategory>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): GameCategory?

    @Query("SELECT * FROM categories WHERE is_vip_category = 1")
    suspend fun getVipCategories(): List<GameCategory>

    @Query("SELECT * FROM categories WHERE is_daily_category = 1")
    suspend fun getDailyCategories(): List<GameCategory>

    // استعلامات الكتابة
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: GameCategory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<GameCategory>)

    @Update
    suspend fun updateCategory(category: GameCategory)

    @Query("UPDATE categories SET is_unlocked = :isUnlocked WHERE id = :categoryId")
    suspend fun updateCategoryUnlockStatus(categoryId: Int, isUnlocked: Boolean)

    @Query("UPDATE categories SET completed_levels = :completedLevels WHERE id = :categoryId")
    suspend fun updateCompletedLevels(categoryId: Int, completedLevels: Int)

    @Delete
    suspend fun deleteCategory(category: GameCategory)
}

@Dao
interface GameLevelDao {

    @Query("SELECT * FROM game_levels WHERE category_id = :categoryId ORDER BY level_number ASC")
    suspend fun getLevelsByCategory(categoryId: Int): List<GameLevel>

    @Query("SELECT * FROM game_levels WHERE id = :levelId")
    suspend fun getLevelById(levelId: Int): GameLevel?

    @Query("SELECT * FROM game_levels WHERE category_id = :categoryId AND level_number = :levelNumber")
    suspend fun getLevelByNumber(categoryId: Int, levelNumber: Int): GameLevel?

    @Query("SELECT * FROM game_levels WHERE is_unlocked = 1 AND is_completed = 0 ORDER BY category_id, level_number ASC LIMIT 1")
    suspend fun getNextUnlockedLevel(): GameLevel?

    @Query("SELECT * FROM game_levels WHERE is_completed = 1")
    suspend fun getCompletedLevels(): List<GameLevel>

    @Query("SELECT COUNT(*) FROM game_levels WHERE category_id = :categoryId")
    suspend fun getLevelCountByCategory(categoryId: Int): Int

    @Query("SELECT COUNT(*) FROM game_levels WHERE category_id = :categoryId AND is_completed = 1")
    suspend fun getCompletedLevelCountByCategory(categoryId: Int): Int

    // استعلامات الكتابة
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevel(level: GameLevel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevels(levels: List<GameLevel>)

    @Update
    suspend fun updateLevel(level: GameLevel)

    @Query("UPDATE game_levels SET is_completed = :isCompleted, stars_earned = :starsEarned, completion_time = :completionTime WHERE id = :levelId")
    suspend fun completeLevel(levelId: Int, isCompleted: Boolean, starsEarned: Int, completionTime: Long)

    @Query("UPDATE game_levels SET is_unlocked = :isUnlocked WHERE id = :levelId")
    suspend fun updateLevelUnlockStatus(levelId: Int, isUnlocked: Boolean)

    @Delete
    suspend fun deleteLevel(level: GameLevel)
}

@Dao
interface PlayerProgressDao {

    @Query("SELECT * FROM player_progress WHERE id = 1")
    suspend fun getPlayerProgress(): PlayerProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerProgress(playerProgress: PlayerProgress)

    @Update
    suspend fun updatePlayerProgress(playerProgress: PlayerProgress)

    // تحديثات سريعة للقيم المهمة
    @Query("UPDATE player_progress SET total_stars = total_stars + :stars WHERE id = 1")
    suspend fun addStars(stars: Int)

    @Query("UPDATE player_progress SET total_coins = total_coins + :coins WHERE id = 1")
    suspend fun addCoins(coins: Int)

    @Query("UPDATE player_progress SET total_coins = total_coins - :coins WHERE id = 1 AND total_coins >= :coins")
    suspend fun spendCoins(coins: Int): Int

    @Query("UPDATE player_progress SET vip_points = vip_points + :points WHERE id = 1")
    suspend fun addVipPoints(points: Int)

    @Query("UPDATE player_progress SET current_level_id = :levelId WHERE id = 1")
    suspend fun updateCurrentLevel(levelId: Int)

    @Query("UPDATE player_progress SET total_completed_levels = total_completed_levels + 1 WHERE id = 1")
    suspend fun incrementCompletedLevels()

    @Query("UPDATE player_progress SET hints_used = hints_used + 1 WHERE id = 1")
    suspend fun incrementHintsUsed()

    @Query("UPDATE player_progress SET perfect_levels = perfect_levels + 1 WHERE id = 1")
    suspend fun incrementPerfectLevels()
}

@Dao
interface LevelGameStateDao {

    @Query("SELECT * FROM level_game_states WHERE level_id = :levelId")
    suspend fun getLevelState(levelId: Int): LevelGameState?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevelState(levelState: LevelGameState)

    @Update
    suspend fun updateLevelState(levelState: LevelGameState)

    @Query("UPDATE level_game_states SET current_lives = :lives WHERE level_id = :levelId")
    suspend fun updateLives(levelId: Int, lives: Int)

    @Query("UPDATE level_game_states SET player_answer = :answer WHERE level_id = :levelId")
    suspend fun updatePlayerAnswer(levelId: Int, answer: String)

    @Query("UPDATE level_game_states SET used_letters = :usedLetters WHERE level_id = :levelId")
    suspend fun updateUsedLetters(levelId: Int, usedLetters: String)

    @Query("UPDATE level_game_states SET hints_used_count = hints_used_count + 1, removed_letters = :removedLetters WHERE level_id = :levelId")
    suspend fun useHint(levelId: Int, removedLetters: String)

    @Query("UPDATE level_game_states SET is_paused = :isPaused, pause_time = :pauseTime WHERE level_id = :levelId")
    suspend fun updatePauseState(levelId: Int, isPaused: Boolean, pauseTime: Long)

    @Delete
    suspend fun deleteLevelState(levelState: LevelGameState)

    @Query("DELETE FROM level_game_states WHERE level_id = :levelId")
    suspend fun deleteLevelStateById(levelId: Int)
}

@Dao
interface CategoryStatsDao {

    @Query("SELECT * FROM category_stats WHERE category_id = :categoryId")
    suspend fun getCategoryStats(categoryId: Int): CategoryStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryStats(stats: CategoryStats)

    @Update
    suspend fun updateCategoryStats(stats: CategoryStats)

    @Query("UPDATE category_stats SET completed_levels = completed_levels + 1, stars_earned = stars_earned + :stars WHERE category_id = :categoryId")
    suspend fun updateLevelCompletion(categoryId: Int, stars: Int)

    @Query("UPDATE category_stats SET last_played_level = :levelNumber WHERE category_id = :categoryId")
    suspend fun updateLastPlayedLevel(categoryId: Int, levelNumber: Int)
}

@Dao
interface DailyRewardDao {

    // استعلامات القراءة
    @Query("SELECT * FROM daily_reward WHERE is_active = 1 ORDER BY current_Day ASC")
    suspend fun getAllActiveRewards(): List<DailyReward>

    @Query("SELECT * FROM daily_reward WHERE current_Day = :dayNumber AND is_active = 1")
    suspend fun getRewardByDay(dayNumber: Int): DailyReward?

    @Query("SELECT * FROM daily_reward WHERE is_special_reward = 1 AND is_active = 1")
    suspend fun getSpecialRewards(): List<DailyReward>

    // استعلامات الكتابة
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReward(reward: DailyReward): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRewards(rewards: List<DailyReward>)

    @Update
    suspend fun updateReward(reward: DailyReward)

    @Delete
    suspend fun deleteReward(reward: DailyReward)
}

@Dao
interface DailyRewardClaimDao {

    @Query("SELECT * FROM daily_reward_claims ORDER BY claim_date DESC")
    suspend fun getAllClaims(): List<DailyRewardClaim>

    @Query("SELECT * FROM daily_reward_claims WHERE DATE(claim_date/1000, 'unixepoch') = DATE('now') LIMIT 1")
    suspend fun getTodayClaim(): DailyRewardClaim?

    @Query("SELECT * FROM daily_reward_claims ORDER BY claim_date DESC LIMIT 7")
    suspend fun getLastWeekClaims(): List<DailyRewardClaim>

    @Query("SELECT COUNT(*) FROM daily_reward_claims WHERE claim_date >= :startDate")
    suspend fun getClaimsCountSince(startDate: Long): Int

    // استعلامات الكتابة
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(claim: DailyRewardClaim): Long

    @Delete
    suspend fun deleteClaim(claim: DailyRewardClaim)

    @Query("DELETE FROM daily_reward_claims WHERE claim_date < :cutoffDate")
    suspend fun deleteOldClaims(cutoffDate: Long)
}

@Dao
interface DailyRewardStatsDao {

    @Query("SELECT * FROM daily_reward_stats WHERE id = 1")
    suspend fun getStats(): DailyRewardStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: DailyRewardStats)

    @Update
    suspend fun updateStats(stats: DailyRewardStats)

    // تحديثات سريعة
    @Query("UPDATE daily_reward_stats SET current_streak = :streak, last_claim_date = :date WHERE id = 1")
    suspend fun updateStreak(streak: Int, date: Long)

    @Query("UPDATE daily_reward_stats SET longest_streak = :streak WHERE id = 1 AND longest_streak < :streak")
    suspend fun updateLongestStreak(streak: Int)

    @Query("UPDATE daily_reward_stats SET total_claims = total_claims + 1 WHERE id = 1")
    suspend fun incrementTotalClaims()

    @Query("UPDATE daily_reward_stats SET total_coins_earned = total_coins_earned + :coins WHERE id = 1")
    suspend fun addCoinsEarned(coins: Int)

    @Query("UPDATE daily_reward_stats SET total_stars_earned = total_stars_earned + :stars WHERE id = 1")
    suspend fun addStarsEarned(stars: Int)

    @Query("UPDATE daily_reward_stats SET total_vip_points_earned = total_vip_points_earned + :points WHERE id = 1")
    suspend fun addVipPointsEarned(points: Int)

    @Query("UPDATE daily_reward_stats SET current_streak = 0, missed_days = missed_days + 1 WHERE id = 1")
    suspend fun resetStreakAndIncrementMissedDays()
}


@Database(
    entities = [
        GameCategory::class,
        GameLevel::class,
        PlayerProgress::class,
        LevelGameState::class,
        CategoryStats::class,
        DailyReward::class,
        DailyRewardClaim::class,
        DailyRewardStats::class
    ],
    version = 3,
    exportSchema = false
)
abstract class GameRoomDatabase : RoomDatabase() {
    abstract fun categoryDao(): GameCategoryDao
    abstract fun levelDao(): GameLevelDao
    abstract fun playerProgressDao(): PlayerProgressDao
    abstract fun levelStateDao(): LevelGameStateDao
    abstract fun categoryStatsDao(): CategoryStatsDao
    abstract fun DailyRewardDao(): DailyRewardDao
    abstract fun dailyRewardClaimDao(): DailyRewardClaimDao
    abstract fun dailyRewardStatsDao(): DailyRewardStatsDao
    companion object {
        @Volatile
        private var INSTANCE: GameRoomDatabase? = null

        fun getDatabase(context: Context): GameRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameRoomDatabase::class.java,
                    "game_room_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration() // للتطوير فقط
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

            }
        }
    }
}


class GameRepository(database: GameRoomDatabase) {

    private val categoryDao = database.categoryDao()
    private val levelDao = database.levelDao()
    private val playerProgressDao = database.playerProgressDao()
    private val levelStateDao = database.levelStateDao()
    private val categoryStatsDao = database.categoryStatsDao()
    private val dailyRewardDao = database.DailyRewardDao()
    suspend fun getAllCategories() = categoryDao.getAllCategories()
    suspend fun getUnlockedCategories() = categoryDao.getUnlockedCategories()
    suspend fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)
    suspend fun unlockCategory(categoryId: Int) {
        categoryDao.updateCategoryUnlockStatus(categoryId, true)
    }
    suspend fun lockCategory(categoryId: Int) {
        categoryDao.updateCategoryUnlockStatus(categoryId, false)
    }
    suspend fun canUnlockCategory(categoryId: Int): Boolean {
        val category = categoryDao.getCategoryById(categoryId) ?: return false
        val progress = getPlayerProgress()

        return when {
            category.isVipCategory -> progress.vipPoints >= category.requiredVipPoints
            else -> progress.totalStars >= category.requiredStars &&
                    progress.totalCoins >= category.requiredCoins
        }
    }

    suspend fun getLevelsByCategory(categoryId: Int) = levelDao.getLevelsByCategory(categoryId)
    suspend fun getLevelById(id: Int) = levelDao.getLevelById(id)
    suspend fun getNextUnlockedLevel() = levelDao.getNextUnlockedLevel()

    suspend fun completeLevel(levelId: Int, starsEarned: Int, completionTime: Long) {
        val level = levelDao.getLevelById(levelId) ?: return

        // تحديث المستوى
        levelDao.completeLevel(levelId, true, starsEarned, completionTime)

        // إضافة المكافآت للاعب
        playerProgressDao.addStars(starsEarned)
        playerProgressDao.addCoins(level.coinsReward)
        playerProgressDao.incrementCompletedLevels()

        // تحديث إحصائيات التصنيف
        categoryStatsDao.updateLevelCompletion(level.categoryId, starsEarned)

        // حذف حالة اللعب المؤقتة
        levelStateDao.deleteLevelStateById(levelId)

        // فتح المستوى التالي
        unlockNextLevel(level.categoryId, level.levelNumber)
    }

    private suspend fun unlockNextLevel(categoryId: Int, currentLevelNumber: Int) {
        val nextLevel = levelDao.getLevelByNumber(categoryId, currentLevelNumber + 1)
        nextLevel?.let {
            levelDao.updateLevelUnlockStatus(it.id, true)
        }
    }
    suspend fun getPlayerProgress(): PlayerProgress {
        return playerProgressDao.getPlayerProgress() ?: PlayerProgress().also {
            playerProgressDao.insertPlayerProgress(it)
        }
    }

    suspend fun updatePlayerProgress(progress: PlayerProgress) {
        playerProgressDao.updatePlayerProgress(progress)
    }

    // ===== عمليات حالة اللعب =====
    suspend fun saveLevelState(levelState: LevelGameState) {
        levelStateDao.insertLevelState(levelState)
    }

    suspend fun getLevelState(levelId: Int) = levelStateDao.getLevelState(levelId)

    suspend fun updatePlayerAnswer(levelId: Int, answer: String) {
        levelStateDao.updatePlayerAnswer(levelId, answer)
    }

    suspend fun useHint(levelId: Int, hintType: HintType): Boolean {
        val level = levelDao.getLevelById(levelId) ?: return false
        val progress = getPlayerProgress()

        if (progress.totalCoins < level.hintCost) return false

        // خصم العملات
        playerProgressDao.spendCoins(level.hintCost)
        playerProgressDao.incrementHintsUsed()

        // تطبيق التلميح
        when (hintType) {
            HintType.REMOVE_RANDOM_LETTERS -> removeRandomLetters(levelId)
            HintType.REMOVE_TWO_LETTERS -> removeTwoLetters(levelId)
        }

        return true
    }

    private suspend fun removeRandomLetters(levelId: Int) {
        val state = levelStateDao.getLevelState(levelId) ?: return
        val level = levelDao.getLevelById(levelId) ?: return

        val availableLetters = level.availableLetters.split(",").toMutableList()
        val correctLetters = level.correctAnswer.toCharArray().map { it.toString() }

        // إزالة حروف عشوائية (ليست من الإجابة الصحيحة)
        val lettersToRemove = availableLetters.filter { !correctLetters.contains(it) }
            .shuffled()
            .take(2)

        val newRemovedLetters = if (state.removedLetters.isNotEmpty()) {
            "${state.removedLetters},${lettersToRemove.joinToString(",")}"
        } else {
            lettersToRemove.joinToString(",")
        }

        levelStateDao.useHint(levelId, newRemovedLetters)
    }

    private suspend fun removeTwoLetters(levelId: Int) {
        // نفس المنطق السابق ولكن مع إزالة حرفين فقط
        removeRandomLetters(levelId)
    }

    // ===== عمليات مساعدة =====
    suspend fun initializeGame() {
        // إنشاء تقدم اللاعب الافتراضي
        val defaultProgress = PlayerProgress()
        playerProgressDao.insertPlayerProgress(defaultProgress)

        // فتح التصنيف الأول والمستوى الأول
        val categories = categoryDao.getAllCategories()
        if (categories.isNotEmpty()) {
            categoryDao.updateCategoryUnlockStatus(categories.first().id, true)

            val firstLevels = levelDao.getLevelsByCategory(categories.first().id)
            if (firstLevels.isNotEmpty()) {
                levelDao.updateLevelUnlockStatus(firstLevels.first().id, true)
            }
        }
    }

    suspend fun getCategoryProgress(categoryId: Int): Pair<Int, Int> {
        val completed = levelDao.getCompletedLevelCountByCategory(categoryId)
        val total = levelDao.getLevelCountByCategory(categoryId)
        return Pair(completed, total)
    }

    suspend fun insertGameCategories(categories: List<GameCategory>) {
        categoryDao.insertCategories(categories)
    }
    suspend fun insertGameLevels(levels: List<GameLevel>) {
        levelDao.insertLevels(levels)
    }
    suspend fun getVipCategories(): List<GameCategory> {
        return categoryDao.getVipCategories()
    }
    suspend fun getDailyCategories(): List<GameCategory> {
        return categoryDao.getDailyCategories()
    }

    suspend fun updateCategory(category: GameCategory) {
        categoryDao.updateCategory(category)
    }

    suspend fun getDailyRewards(): List<DailyReward> {
        return dailyRewardDao.getAllActiveRewards()
    }
    suspend fun getDailyRewardByDay(dayNumber: Int): DailyReward? {
        return dailyRewardDao.getRewardByDay(dayNumber)
    }
    suspend fun getSpecialDailyRewards(): List<DailyReward> {
        return dailyRewardDao.getSpecialRewards()
    }
    suspend fun insertDailyReward(reward: DailyReward): Long {
        return dailyRewardDao.insertReward(reward)
    }
    suspend fun insertDailyRewards(rewards: List<DailyReward>) {
        dailyRewardDao.insertRewards(rewards)
    }
    suspend fun updateDailyReward(reward: DailyReward) {
        dailyRewardDao.updateReward(reward)
    }
    suspend fun deleteDailyReward(reward: DailyReward) {
        dailyRewardDao.deleteReward(reward)
    }

}

class DailyRewardRepository(database: GameRoomDatabase) {

    private val rewardGiftDao = database.DailyRewardDao()
    private val rewardClaimDao = database.dailyRewardClaimDao()
    private val rewardStatsDao = database.dailyRewardStatsDao()
    private val playerProgressDao = database.playerProgressDao()

    suspend fun getAllActiveRewards() = rewardGiftDao.getAllActiveRewards()

    suspend fun canClaimTodayReward(): Boolean {
        val todayClaim = rewardClaimDao.getTodayClaim()
        return todayClaim == null
    }

    suspend fun getCurrentDayReward(): DailyReward? {
        val stats = getRewardStats()
        val currentDay = (stats.currentStreak % 7) + 1 // دورة أسبوعية
        return rewardGiftDao.getRewardByDay(currentDay)
    }

    suspend fun getRewardStats(): DailyRewardStats {
        return rewardStatsDao.getStats() ?: DailyRewardStats().also {
            rewardStatsDao.insertStats(it)
        }
    }

    suspend fun checkAndResetStreak() {
        val stats = getRewardStats()
        val now = System.currentTimeMillis()
        val dayInMillis = 24 * 60 * 60 * 1000L
        val daysSinceLastClaim = (now - stats.lastClaimDate) / dayInMillis

        if (daysSinceLastClaim > 1) {
            // انقطعت السلسلة
            rewardStatsDao.resetStreakAndIncrementMissedDays()
        }
    }

    suspend fun initializeDailyRewards() {
        val defaultRewards = createDefaultDailyRewards()
        rewardGiftDao.insertRewards(defaultRewards)

        val defaultStats = DailyRewardStats()
        rewardStatsDao.insertStats(defaultStats)
    }

    private fun createDefaultDailyRewards(): List<DailyReward> {
        return listOf(
            DailyReward(currentDay = 1, rewardType = DailyRewardType.COINS, rewardAmount = 100, rewardIcon = R.drawable.coin1),
            DailyReward(currentDay = 2, rewardType = DailyRewardType.COINS, rewardAmount = 200, rewardIcon = R.drawable.coin2),
            DailyReward(currentDay = 3, rewardType = DailyRewardType.COINS, rewardAmount = 300, rewardIcon = R.drawable.coin3),
            DailyReward(currentDay = 4, rewardType = DailyRewardType.COINS, rewardAmount = 500, rewardIcon = R.drawable.coins),
            DailyReward(currentDay = 5, rewardType = DailyRewardType.COINS, rewardAmount = 600, rewardIcon = R.drawable.coin4),
            DailyReward(currentDay = 6, rewardType = DailyRewardType.COINS, rewardAmount = 800, rewardIcon = R.drawable.coin4),
            DailyReward(currentDay = 7, rewardType = DailyRewardType.COINS, rewardAmount = 1000, isSpecialReward = true, rewardIcon = R.drawable.coin5)
        )
    }

}

enum class HintType {
    REMOVE_RANDOM_LETTERS,
    REMOVE_TWO_LETTERS
}

enum class GameCategoryType{
    SPORTS,
    GENERAL_KNOWLEDGE,
    ENTERTAINMENT,
    SCIENCE,
    HISTORY,
    GEOGRAPHY,
    ART,
    LITERATURE,
    TECHNOLOGY,
    MUSIC ,
    FILM,
    FOOD,
    TRAVEL,
    LANGUAGE,
    NATURE,
    HEALTH,
    RELIGION,
    POLITICS,
    BUSINESS,
    EDUCATION,
    PSYCHOLOGY,
    FASHION,
    ANIMALS,
    OTHERS,
    DAY_CHALLENGE,
    YESTERDAY_CHALLENGE,
    TOMORROW_CHALLENGE,

}



