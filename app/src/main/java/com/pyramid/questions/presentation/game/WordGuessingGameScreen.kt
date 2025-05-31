package com.pyramid.questions.presentation.game

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.data.remote.BannerAdView
import com.pyramid.questions.data.remote.rememberAdMobManager
import com.pyramid.questions.domain.model.LetterData
import com.pyramid.questions.domain.model.Question
import com.pyramid.questions.domain.model.QuestionCategory
import com.pyramid.questions.presentation.components.ControlButton
import com.pyramid.questions.presentation.components.LivesProgressIndicator
import com.pyramid.questions.presentation.components.OutOfLivesDialog
import com.pyramid.questions.presentation.components.SuccessSection
import com.pyramid.questions.presentation.components.TopGameBar
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.util.Locale


data class LetterPosition(
    val letter: String,
    var startPosition: Offset = Offset.Zero,
    var endPosition: Offset = Offset.Zero,
    var size: IntSize = IntSize.Zero
)

@Composable
fun WordGuessingGameApp() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        WordGuessingGameScreen(navController = rememberNavController())
    }
}

@Composable
fun LetterBox(
    letter: String,
    onClick: () -> Unit,
    isSelected: Boolean = false,
    isDeleted: Boolean = false,
    onPositionCaptured: (Offset, IntSize) -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    isDeleted -> Brush.radialGradient(
                        colors = listOf(
                            Color(0x44CCCCCC),
                            Color(0x44CCCCCC)
                        )
                    )

                    isSelected -> Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0E1E58),
                            Color(0xFF0E1E58)
                        )
                    )

                    else -> Brush.radialGradient(
                        colors = listOf(Color.White, Color(0xFFF0F0F0))
                    )
                }
            )
            .border(
                1.dp,
                if (!isSelected && !isDeleted) Color(0xFF5270BF) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .clickable(
                enabled = !isSelected && !isDeleted,
                onClick = onClick
            )
            .onGloballyPositioned { coordinates ->
                onPositionCaptured(coordinates.positionInWindow(), coordinates.size)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isDeleted) "" else letter,
            color = if (isSelected)
                Color(0xFF0E1E58) else Color.Black,
            fontSize = 26.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun QuestionBar(
    question: Question
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF284BB4), Color(0xFF1F3A8A))
                )
            )
            .border(1.dp, Color(0xFF284BB4)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question.questionText,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun ControlButtons(
    onHintClick: () -> Unit,
    onDeleteLetter: () -> Unit,
    onClearAll: () -> Unit,
    onDeleteRandomLetters: () -> Unit,
    isDeleteRandomEnabled: Boolean,
    selectedLettersCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3766E0), Color(0xFF2E59BC))
                )
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlButton(
            iconRes = R.drawable.close_ic,
            backgroundColor = Color(0xFFE74C3C),
            onClick = onDeleteLetter,
            label = stringResource(R.string.delete_button),
            enabled = selectedLettersCount > 0,
        )
        ControlButton(
            iconRes = R.drawable.refresh_ic,
            backgroundColor = Color(0xFFE67E22),
            onClick = onClearAll,
            label = stringResource(R.string.clear_button)
        )
        ControlButton(
            iconRes = R.drawable.close_ic,
            backgroundColor = Color(0xFF9C27B0),
            onClick = onDeleteRandomLetters,
            label = stringResource(R.string.delete_two_letters_button),
            enabled = isDeleteRandomEnabled,
            coinCost = 100
        )
        ControlButton(
            iconRes = R.drawable.idea_ic,
            backgroundColor = Color(0xFFFFC107),
            onClick = onHintClick,
            label = stringResource(R.string.hint_button),
            coinCost = 50
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordGuessingGameScreen(
    navController: NavHostController,
    question: Question = Question(
        questionText = "What is the capital of Japan?",
        questionId = "1",
        imageUrl = "https://res.cloudinary.com/dnsgxkndl/image/upload/v1748606625/img4_l53ecx.jpg",
        answer = "Japan",
        lettersPool = listOf('J', 'a', 'p', 'a', 'n','m', 'o', 'n'),
        hint = listOf('J', 'a'),
        level = "l1",
        category = QuestionCategory.GENERAL
    ).initializeAvailableLetters()
) {
    var currentQuestion by remember { mutableStateOf(question) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }

    preferencesManager.saveLives(5)

    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var showSuccessAnimation by remember { mutableStateOf(false) }
    var showOutOfLivesDialog by remember { mutableStateOf(false) }
    var isDeleteRandomEnabled by remember { mutableStateOf(true) }

    var movingLetter by remember { mutableStateOf<LetterData?>(null) }
    var isLetterMoving by remember { mutableStateOf(false) }
    var movingLetterIndex by remember { mutableIntStateOf(-1) }
    var targetLetterIndex by remember { mutableIntStateOf(-1) }
    var pendingLetter by remember { mutableStateOf<Pair<Int, LetterData>?>(null) }

    val availableLettersPositions = remember { mutableStateMapOf<Int, LetterPosition>() }
    val answerPositions = remember { mutableStateMapOf<Int, LetterPosition>() }
    var animatedPosition by remember { mutableStateOf(Offset.Zero) }

    var selectedLettersCount by remember { mutableIntStateOf(currentQuestion.selectedLetters.size) }
    var currentLives by remember { mutableIntStateOf(preferencesManager.getLives()) }
    var isRecharging by remember { mutableStateOf(preferencesManager.getIsRecharging()) }
    var timeRemaining by remember { mutableIntStateOf(preferencesManager.getTimeRemaining()) }
    val maxLives = 5
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    val player = preferencesManager.getPlayer()

    val isArabic = currentLocale.language == "ar"

    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val backgroundAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }

    fun handleLetterClick(letter: LetterData, letterIndex: Int) {
        if (currentLives <= 0) {
            showOutOfLivesDialog = true
            return
        }

        if (!letter.isSelected && !letter.isDeleted) {
            currentQuestion = currentQuestion.selectLetter(letter)
            selectedLettersCount = currentQuestion.selectedLetters.size

            if (currentQuestion.isGameWon()) {
                showSuccess = true
                showError = false
            } else if (currentQuestion.selectedLetters.size == currentQuestion.answer.length) {
                showError = true
                showSuccess = false
            }
        }
    }

    fun continueToNextWord() {
        showSuccess = false
        showSuccessAnimation = false
        currentQuestion = question.initializeAvailableLetters()
        selectedLettersCount = 0
        isDeleteRandomEnabled = true
    }

    LaunchedEffect(Unit) {
        if (isRecharging) {
            val startTime = preferencesManager.getRechargeStartTime()
            val currentTime = System.currentTimeMillis()
            val elapsedSeconds = ((currentTime - startTime) / 1000).toInt()
            val remainingTime = 1800 - elapsedSeconds

            if (remainingTime <= 0) {
                currentLives = maxLives
                isRecharging = false
                timeRemaining = 1800
                preferencesManager.saveLives(maxLives)
                preferencesManager.saveIsRecharging(false)
                preferencesManager.saveTimeRemaining(1800)
            } else {
                timeRemaining = remainingTime
            }
        }
    }

    LaunchedEffect(isRecharging, timeRemaining) {
        if (isRecharging) {
            while (timeRemaining > 0 && isRecharging) {
                delay(1000)
                timeRemaining--
                preferencesManager.saveTimeRemaining(timeRemaining)

                if (timeRemaining <= 0) {
                    currentLives = maxLives
                    isRecharging = false
                    timeRemaining = 1800
                    preferencesManager.saveLives(maxLives)
                    preferencesManager.saveIsRecharging(false)
                    preferencesManager.saveTimeRemaining(1800)
                }
            }
        }
    }

    LaunchedEffect(showError) {
        if (showError) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            delay(1500)
            showError = false

            if (currentLives > 0) {
                currentLives--
                preferencesManager.saveLives(currentLives)

                if (currentLives <= 0) {
                    isRecharging = true
                    timeRemaining = 1800
                    preferencesManager.saveIsRecharging(true)
                    preferencesManager.saveRechargeStartTime(System.currentTimeMillis())
                    preferencesManager.saveTimeRemaining(1800)
                    showOutOfLivesDialog = true
                }
            }
        }
    }

    LaunchedEffect(selectedLettersCount) {
        for (i in currentQuestion.answer.indices) {
            val hasLetter = i < currentQuestion.selectedLetters.size
            if (answerPositions.containsKey(i)) {
                answerPositions[i] = answerPositions[i]!!.copy(
                    letter = if (hasLetter && i < currentQuestion.selectedLetters.size)
                        currentQuestion.selectedLetters[i].letter.toString() else ""
                )
            }
        }
    }

    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            delay(500)
            showSuccessAnimation = true
        }
    }
    CompositionLocalProvider(
        LocalContext provides LocalContext.current.createConfigurationContext(
            Configuration().apply { setLocale(currentLocale) }
        )
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0C1638))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = question.imageUrl
                            ),
                            contentDescription = stringResource(R.string.japanese_background_desc),
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(alpha = 0.4f),
                            contentScale = ContentScale.Crop
                        )
                        TopGameBar(
                            player = player,
                            onOpenStore = {},
                            onOpenProfile = {},
                            backgroundColor = Color.Transparent,
                            showDriver = false,
                            onBackClicked = { navController.popBackStack() },
                            fontFamily = FontFamily.Default
                        )

                        LivesProgressIndicator(
                            currentLives = currentLives,
                            maxLives = maxLives,
                            isRecharging = isRecharging,
                            timeRemainingSeconds = timeRemaining,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .graphicsLayer(alpha = backgroundAlpha)
                                .padding(top = 16.dp)
                        )
                    }

                    QuestionBar(question)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF13277A), Color(0xFF0E1E58))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            CompositionLocalProvider(
                                LocalLayoutDirection provides if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr
                            ) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(
                                        8.dp,
                                        Alignment.CenterHorizontally
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    val letterBoxes = updateAnswerBoxes(
                                        question = currentQuestion,
                                        answerPositions = answerPositions,
                                        targetLetterIndex = targetLetterIndex,
                                        isLetterMoving = isLetterMoving,
                                        pendingLetter = pendingLetter,
                                        onPositionCaptured = { index, position, size ->
                                            answerPositions[index] = LetterPosition(
                                                letter = if (index < currentQuestion.selectedLetters.size)
                                                    currentQuestion.selectedLetters[index].letter.toString() else "",
                                                startPosition = position,
                                                size = size
                                            )
                                        }
                                    )
                                    letterBoxes.forEach { letterBox -> letterBox() }
                                }
                            }

                            if (showSuccess) {
                                SuccessSection(
                                    level = currentQuestion.level,
                                    answer = currentQuestion.answer,
                                    onContinueClick = { continueToNextWord() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.CenterHorizontally
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        val firstRowIndices = (0 until minOf(
                                            5,
                                            currentQuestion.availableLetters.size
                                        )).toList().reversed()
                                        for (i in firstRowIndices) {
                                            val letter = currentQuestion.availableLetters[i]
                                            if (isLetterMoving && movingLetterIndex == i) {
                                                Box(modifier = Modifier.size(48.dp))
                                            } else {
                                                LetterBox(
                                                    letter = letter.letter.toString(),
                                                    onClick = { handleLetterClick(letter, i) },
                                                    isSelected = letter.isSelected,
                                                    isDeleted = letter.isDeleted,
                                                    onPositionCaptured = { position, size ->
                                                        availableLettersPositions[i] =
                                                            LetterPosition(
                                                                letter = letter.letter.toString(),
                                                                startPosition = position,
                                                                size = size
                                                            )
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    if (currentQuestion.availableLetters.size > 5) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(
                                                8.dp,
                                                Alignment.CenterHorizontally
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            val secondRowIndices =
                                                (5 until currentQuestion.availableLetters.size).toList()
                                                    .reversed()
                                            for (i in secondRowIndices) {
                                                val letter = currentQuestion.availableLetters[i]
                                                if (isLetterMoving && movingLetterIndex == i) {
                                                    Box(modifier = Modifier.size(48.dp))
                                                } else {
                                                    LetterBox(
                                                        letter = letter.letter.toString(),
                                                        onClick = { handleLetterClick(letter, i) },
                                                        isSelected = letter.isSelected,
                                                        isDeleted = letter.isDeleted,
                                                        onPositionCaptured = { position, size ->
                                                            availableLettersPositions[i] =
                                                                LetterPosition(
                                                                    letter = letter.letter.toString(),
                                                                    startPosition = position,
                                                                    size = size
                                                                )
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (isLetterMoving && movingLetter != null) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .offset(
                                        x = with(density) { (animatedPosition.x / density.density).dp },
                                        y = with(density) { (animatedPosition.y / density.density).dp }
                                    )
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .border(1.5.dp, Color(0xFF5270BF), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = movingLetter!!.letter.toString(),
                                    color = Color.Black,
                                    fontSize = 26.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    if (!showSuccess) {
                        ControlButtons(
                            onHintClick = {
                                if (currentLives <= 0) {
                                    showOutOfLivesDialog = true
                                    return@ControlButtons
                                }

                                currentQuestion = currentQuestion.useHint()
                                selectedLettersCount = currentQuestion.selectedLetters.size

                                if (currentQuestion.isGameWon()) {
                                    showSuccess = true
                                    showError = false
                                }
                            },
                            onDeleteLetter = {
                                currentQuestion = currentQuestion.deleteLetter()
                                pendingLetter = null
                                selectedLettersCount = currentQuestion.selectedLetters.size
                                showSuccess = false
                                showError = false
                            },
                            onClearAll = {
                                currentQuestion = currentQuestion.clearAllLetters()
                                pendingLetter = null
                                selectedLettersCount = 0
                                showSuccess = false
                                showError = false
                            },
                            onDeleteRandomLetters = {
                                if (currentLives <= 0) {
                                    showOutOfLivesDialog = true
                                    return@ControlButtons
                                }

                                if (isDeleteRandomEnabled) {
                                    currentQuestion = currentQuestion.deleteRandomLetters(2)
                                    isDeleteRandomEnabled = false
                                }
                            },
                            isDeleteRandomEnabled = isDeleteRandomEnabled,
                            selectedLettersCount = selectedLettersCount
                        )
                    }
                    BannerAdView(
                        modifier = Modifier.fillMaxWidth(),
                        onAdLoaded = {
                            println("Banner ad loaded successfully!")
                        },
                        onAdFailedToLoad = { error ->
                            println("Banner ad failed: $error")
                        }
                    )

                }
            }

            OutOfLivesDialog(
                onCloseClick = {
                    showOutOfLivesDialog = false
                    navController.popBackStack()
                },
                onBuyLivesClick = {
                    showOutOfLivesDialog = false
                },
                onWatchAdClick = {
                    currentLives = 1
                    preferencesManager.saveLives(currentLives)
                    isRecharging = false
                    preferencesManager.saveIsRecharging(false)
                    showOutOfLivesDialog = false
                },
                onBecomeVipClick = {
                    showOutOfLivesDialog = false
                },
                onJoinClick = {
                    showOutOfLivesDialog = false
                },
                showDialog = showOutOfLivesDialog,
                timeRemaining = formatTime(timeRemaining)
            )
        }
    }
}
@Composable
fun EnhancedLetterBox(
    letter: String,
    isCorrectPosition: Boolean = false,
    isHintLetter: Boolean = false,
    modifier: Modifier = Modifier
) {
    val (backgroundBrush, shadowColor, borderColors) = when {
        isCorrectPosition -> Triple(
            Brush.radialGradient(
                colors = listOf(Color(0xFF4CAF50), Color(0xFF2E7D32)),
                radius = 120f
            ),
            Color(0xFF1B5E20),
            listOf(Color(0xFF81C784), Color(0xFF2E7D32))
        )

        isHintLetter -> Triple(
            Brush.radialGradient(
                colors = listOf(Color(0xFFFFDD55), Color(0xFFFFC800)),
                radius = 120f
            ),
            Color(0xFF805E07),
            listOf(Color(0xFFFFEFB1), Color(0xFF8B6B00))
        )

        letter.isNotEmpty() -> Triple(
            Brush.radialGradient(
                colors = listOf(Color.White, Color(0xFFF0F0F0)),
                radius = 120f
            ),
            Color(0xFF9E9E9E),
            listOf(Color(0xFFE0E0E0), Color(0xFF9E9E9E))
        )

        else -> Triple(
            Brush.radialGradient(
                colors = listOf(Color(0xFF3A5498), Color(0xFF2C4085)),
                radius = 120f
            ),
            Color(0xFF1A237E),
            listOf(Color(0xFF7986CB), Color(0xFF2C4085))
        )
    }

    val textColor = when {
        isCorrectPosition -> Color.White
        isHintLetter -> Color.Black
        letter.isNotEmpty() -> Color.Black
        else -> Color.Transparent
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = shadowColor
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundBrush)
                .border(
                    width = 4.dp,
                    brush = Brush.verticalGradient(colors = borderColors),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(4.dp)
        ) {
            if (letter.isNotEmpty()) {
                Text(
                    text = letter,
                    color = textColor,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (isCorrectPosition) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .offset(x = 28.dp, y = (-4).dp)
                    .background(
                        Color(0xFF81C784),
                        shape = CircleShape
                    )
            )
        }

        if (isHintLetter) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .offset(x = 28.dp, y = (-4).dp)
                    .background(
                        Color(0xFFFFC107),
                        shape = CircleShape
                    )
            )
        }
    }
}

fun updateAnswerBoxes(
    question: Question,
    answerPositions: MutableMap<Int, LetterPosition>,
    targetLetterIndex: Int,
    isLetterMoving: Boolean,
    pendingLetter: Pair<Int, LetterData>?,
    onPositionCaptured: (Int, Offset, IntSize) -> Unit
): List<@Composable () -> Unit> {
    val reversedIndices = question.answer.indices.reversed().toList()

    return reversedIndices.mapNotNull { i ->
        if (question.answer[i] == ' ') {
            if (i < question.answer.length - 1 && question.answer[i + 1] == ' ') {
                return@mapNotNull {
                    Row {
                        createLetterBox(
                            i,
                            question,
                            answerPositions,
                            targetLetterIndex,
                            isLetterMoving,
                            pendingLetter,
                            onPositionCaptured
                        )
                        Box(modifier = Modifier.size(16.dp, 44.dp))
                    }
                }
            }
            null
        } else {
            return@mapNotNull {
                createLetterBox(
                    i,
                    question,
                    answerPositions,
                    targetLetterIndex,
                    isLetterMoving,
                    pendingLetter,
                    onPositionCaptured
                )
            }
        }
    }
}
@Composable
private fun createLetterBox(
    index: Int,
    question: Question,
    answerPositions: MutableMap<Int, LetterPosition>,
    targetLetterIndex: Int,
    isLetterMoving: Boolean,
    pendingLetter: Pair<Int, LetterData>?,
    onPositionCaptured: (Int, Offset, IntSize) -> Unit
) {
    val hasLetter = index < question.selectedLetters.size
    val isTargetForMovingLetter = isLetterMoving && targetLetterIndex == index
    val currentLetter = if (hasLetter) question.selectedLetters[index].letter.toString() else ""

    val isCorrectPosition = hasLetter &&
            index < question.answer.length &&
            question.selectedLetters[index].letter.lowercaseChar() == question.answer[index].lowercase().first()

    val isHintLetter = false

    val displayLetter = when {
        !isTargetForMovingLetter && hasLetter -> currentLetter
        pendingLetter != null && pendingLetter.first == index && !isLetterMoving -> pendingLetter.second.letter.toString()
        else -> ""
    }

    EnhancedLetterBox(
        letter = displayLetter,
        isCorrectPosition = isCorrectPosition,
        isHintLetter = isHintLetter,
        modifier = Modifier.onGloballyPositioned { coordinates ->
            onPositionCaptured(index, coordinates.positionInWindow(), coordinates.size)
            answerPositions[index] = LetterPosition(
                letter = displayLetter,
                startPosition = coordinates.positionInWindow(),
                size = coordinates.size
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun WordGuessingGamePreview() {
    MaterialTheme {
        Surface {
            WordGuessingGameApp()
        }
    }
}
