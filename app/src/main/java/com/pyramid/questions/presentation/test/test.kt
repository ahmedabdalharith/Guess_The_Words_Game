//package com.pyramid.questions.presentation.test
//
//// Firebase Data Models for Trivia AI Game - Jetpack Compose
//// استخدام Firebase Firestore مع Kotlin و Jetpack Compose
//
//import androidx.compose.runtime.Stable
//import com.google.firebase.Timestamp
//import com.google.firebase.firestore.DocumentId
//import com.google.firebase.firestore.ServerTimestamp
//
//// 1. نموذج بيانات المستخدم
//@Stable
//data class User(
//    @DocumentId
//    val id: String = "",
//    val username: String = "",
//    val email: String = "",
//    val profilePicture: String = "",
//    val totalScore: Long = 0,
//    val level: Int = 1,
//    val experience: Long = 0,
//    val gamesPlayed: Int = 0,
//    val correctAnswers: Int = 0,
//    val wrongAnswers: Int = 0,
//    val streakBest: Int = 0,
//    val streakCurrent: Int = 0,
//    val coins: Int = 0,
//    val hints: Int = 3,
//    val achievements: List<String> = emptyList(),
//    @ServerTimestamp
//    val createdAt: Timestamp? = null,
//    @ServerTimestamp
//    val lastActive: Timestamp? = null,
//    val preferences: UserPreferences = UserPreferences()
//) {
//    fun updateScore(points: Int): User {
//        val newScore = totalScore + points
//        val newLevel = calculateLevel(newScore)
//        return this.copy(
//            totalScore = newScore,
//            level = newLevel,
//            experience = experience + points
//        )
//    }
//
//    fun canAfford(price: Int): Boolean = coins >= price
//
//    private fun calculateLevel(score: Long): Int = (score / 100).toInt() + 1
//}
//
//@Stable
//data class UserPreferences(
//    val soundEnabled: Boolean = true,
//    val vibrationEnabled: Boolean = true,
//    val language: String = "ar",
//    val difficulty: String = "medium",
//    val theme: String = "system" // light, dark, system
//)
//
//// 2. نموذج بيانات السؤال
//@Stable
//data class Question(
//    @DocumentId
//    val id: String = "",
//    val imageUrl: String = "",
//    val imagePrompt: String = "",
//    val correctAnswer: String = "",
//    val alternativeAnswers: List<String> = emptyList(),
//    val category: String = "",
//    val difficulty: String = "medium",
//    val hints: List<String> = emptyList(),
//    @ServerTimestamp
//    val createdAt: Timestamp? = null,
//    val isActive: Boolean = true,
//    val statistics: QuestionStatistics = QuestionStatistics()
//)
//
//@Stable
//data class QuestionStatistics(
//    val timesShown: Int = 0,
//    val correctAnswers: Int = 0,
//    val wrongAnswers: Int = 0,
//    val averageTime: Double = 0.0
//)
//
//// 3. نموذج بيانات اللعبة
//@Stable
//data class Game(
//    @DocumentId
//    val id: String = "",
//    val playerId: String = "",
//    @ServerTimestamp
//    val startTime: Timestamp? = null,
//    @ServerTimestamp
//    val endTime: Timestamp? = null,
//    val gameMode: GameMode = GameMode.CLASSIC,
//    val totalQuestions: Int = 10,
//    val correctAnswers: Int = 0,
//    val score: Int = 0,
//    val timeSpent: Long = 0, // بالثواني
//    val hintsUsed: Int = 0,
//    val questions: List<GameQuestion> = emptyList(),
//    val completed: Boolean = false
//)
//
//@Stable
//data class GameQuestion(
//    val questionId: String = "",
//    val userAnswer: String = "",
//    val isCorrect: Boolean = false,
//    val timeSpent: Long = 0,
//    val hintsUsed: Int = 0
//)
//
//enum class GameMode {
//    CLASSIC, TIMED, SURVIVAL, MULTIPLAYER
//}
//
//// 4. نموذج بيانات الإنجازات
//@Stable
//data class Achievement(
//    @DocumentId
//    val id: String = "",
//    val name: String = "",
//    val nameAr: String = "",
//    val description: String = "",
//    val descriptionAr: String = "",
//    val icon: String = "",
//    val requirement: AchievementRequirement = AchievementRequirement(),
//    val reward: AchievementReward = AchievementReward(),
//    val isActive: Boolean = true
//)
//
//@Stable
//data class AchievementRequirement(
//    val type: String = "",
//    val value: Int = 0
//)
//
//@Stable
//data class AchievementReward(
//    val coins: Int = 0,
//    val experience: Int = 0
//)
//
//// 5. نموذج بيانات المتجر
//@Stable
//data class ShopItem(
//    @DocumentId
//    val id: String = "",
//    val name: String = "",
//    val nameAr: String = "",
//    val description: String = "",
//    val descriptionAr: String = "",
//    val icon: String = "",
//    val price: Int = 0,
//    val category: String = "",
//    val isActive: Boolean = true,
//    val discount: Int = 0
//)
//
//// 6. نموذج بيانات الفئات
//@Stable
//data class Category(
//    @DocumentId
//    val id: String = "",
//    val name: String = "",
//    val nameAr: String = "",
//    val icon: String = "",
//    val color: String = "#4CAF50",
//    val isActive: Boolean = true,
//    val questionsCount: Int = 0
//)
//
//// 7. نموذج بيانات الألعاب الجماعية
//@Stable
//data class MultiplayerGame(
//    @DocumentId
//    val id: String = "",
//    val host: String = "",
//    val players: List<String> = emptyList(),
//    val maxPlayers: Int = 4,
//    val status: MultiplayerStatus = MultiplayerStatus.WAITING,
//    val currentQuestion: Int = 0,
//    val questions: List<String> = emptyList(),
//    @ServerTimestamp
//    val createdAt: Timestamp? = null,
//    val settings: MultiplayerSettings = MultiplayerSettings(),
//    val scores: Map<String, Int> = emptyMap()
//)
//
//@Stable
//data class MultiplayerSettings(
//    val questionsCount: Int = 10,
//    val timePerQuestion: Int = 30,
//    val category: String = "all"
//)
//
//enum class MultiplayerStatus {
//    WAITING, PLAYING, FINISHED
//}
//
////// 8. نموذج الإحصائيات اليومية
////@Stable
////data class DailyStats(
////    @DocumentId
////    val id: String = "", // userId_date format
////    val date: String = "", // YYYY-MM-DD
////    val userId: String = "",
////    val gamesPlayed: Int = 0,
////    val correctAnswers: Int = 0,
////    val wrongAnswers: Int = 0,
////    val timeSpent: Long = 0,
////    val coinsEarned: Int = 0,
////    val experienceGained: Int = 0
////)
//
//// Repository Classes للتعامل مع Firebase
//
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.tasks.await
//
//class UserRepository {
//    private val db = FirebaseFirestore.getInstance()
//    private val usersCollection = db.collection("users")
//
//    suspend fun getUser(userId: String): User? {
//        return try {
//            val document = usersCollection.document(userId).get().await()
//            document.toObject(User::class.java)
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    suspend fun updateUser(user: User) {
//        usersCollection.document(user.id).set(user).await()
//    }
//
//    fun getUserFlow(userId: String): Flow<User?> = flow {
//        usersCollection.document(userId)
//            .addSnapshotListener { snapshot, _ ->
//                val user = snapshot?.toObject(User::class.java)
//                // emit user through flow
//            }
//    }
//}
//
//class GameRepository {
//    private val db = FirebaseFirestore.getInstance()
//    private val gamesCollection = db.collection("games")
//
//    suspend fun saveGame(game: Game): String {
//        val docRef = gamesCollection.add(game).await()
//        return docRef.id
//    }
//
//    suspend fun getUserGames(userId: String): List<Game> {
//        return gamesCollection
//            .whereEqualTo("playerId", userId)
//            .orderBy("startTime", com.google.firebase.firestore.Query.Direction.DESCENDING)
//            .get()
//            .await()
//            .toObjects(Game::class.java)
//    }
//}
//
//class QuestionRepository {
//    private val db = FirebaseFirestore.getInstance()
//    private val questionsCollection = db.collection("questions")
//
//    suspend fun getRandomQuestions(
//        count: Int,
//        category: String = "all",
//        difficulty: String = "all"
//    ): List<Question> {
//        var query = questionsCollection.whereEqualTo("isActive", true)
//
//        if (category != "all") {
//            query = query.whereEqualTo("category", category)
//        }
//
//        if (difficulty != "all") {
//            query = query.whereEqualTo("difficulty", difficulty)
//        }
//
//        val allQuestions = query.get().await().toObjects(Question::class.java)
//        return allQuestions.shuffled().take(count)
//    }
//}
//
//// ViewModel للشاشات الرئيسية
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.State
//import kotlinx.coroutines.launch
//
//class GameViewModel : ViewModel() {
//    private val userRepository = UserRepository()
//    private val gameRepository = GameRepository()
//    private val questionRepository = QuestionRepository()
//
//    private val _gameState = mutableStateOf(GameState())
//    val gameState: State<GameState> = _gameState
//
//    private val _user = mutableStateOf<User?>(null)
//    val user: State<User?> = _user
//
//    fun startGame(userId: String, gameMode: GameMode = GameMode.CLASSIC) {
//        viewModelScope.launch {
//            val questions = questionRepository.getRandomQuestions(10)
//            _gameState.value = _gameState.value.copy(
//                isPlaying = true,
//                questions = questions,
//                currentQuestionIndex = 0
//            )
//        }
//    }
//
//    fun submitAnswer(answer: String) {
//        viewModelScope.launch {
//            val currentQuestion = _gameState.value.currentQuestion
//            val isCorrect = answer.equals(currentQuestion?.correctAnswer, ignoreCase = true)
//
//            // Update game state
//            _gameState.value = _gameState.value.copy(
//                score = if (isCorrect) _gameState.value.score + 10 else _gameState.value.score,
//                currentQuestionIndex = _gameState.value.currentQuestionIndex + 1
//            )
//        }
//    }
//}
//
//@Stable
//data class GameState(
//    val isPlaying: Boolean = false,
//    val questions: List<Question> = emptyList(),
//    val currentQuestionIndex: Int = 0,
//    val score: Int = 0,
//    val timeLeft: Int = 30,
//    val hintsUsed: Int = 0
//) {
//    val currentQuestion: Question?
//        get() = questions.getOrNull(currentQuestionIndex)
//
//    val isGameFinished: Boolean
//        get() = currentQuestionIndex >= questions.size
//}