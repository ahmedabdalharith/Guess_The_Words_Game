package com.pyramid.questions.presentation.game

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.text.font.Font
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
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.presentation.components.ControlButton
import com.pyramid.questions.presentation.components.LivesProgressIndicator
import com.pyramid.questions.presentation.components.TopGameBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

suspend fun animatePosition(
    startPosition: Offset,
    endPosition: Offset,
    updatePosition: (Offset) -> Unit
) {
    val animationSpec = tween<Float>(
        durationMillis = 250,
        easing = FastOutSlowInEasing
    )

    animate(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = animationSpec
    ) { value, _ ->
        val x = startPosition.x + (endPosition.x - startPosition.x) * value
        val y = startPosition.y + (endPosition.y - startPosition.y) * value
        updatePosition(Offset(x, y))
    }
}

@Composable
fun ArabicLetterButtonWithPosition(
    letter: String,
    onClick: () -> Unit,
    isSelected: Boolean = false,
    isDeleted: Boolean = false,
    onPositionCaptured: (Offset, IntSize) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (!isSelected && !isDeleted) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .scale(scale)
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
            .border(1.dp, if (!isSelected && !isDeleted) Color(0xFF5270BF) else Color.Transparent, RoundedCornerShape(8.dp))
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
            fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun QuestionBar() {
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
                text = stringResource(R.string.guess_country_question),
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
    onSkipClick: () -> Unit,
    onDeleteLetter: () -> Unit,
    onClearAll: () -> Unit,
    onDeleteRandomLetters: () -> Unit,
    isDeleteRandomEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3766E0), Color(0xFF2E59BC))
                )
            ).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlButton(
            iconRes = R.drawable.close_ic,
            backgroundColor = Color(0xFFE74C3C),
            onClick = onDeleteLetter,
            label = stringResource(R.string.delete_button),
            enabled = isDeleteRandomEnabled,
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
        ControlButton(
            iconRes = R.drawable.arrow_forward_ic,
            backgroundColor = Color(0xFF55ACEE),
            onClick = onSkipClick,
            label = stringResource(R.string.skip_button)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordGuessingGameScreen(
    imageId: Int = R.drawable.japanese_background,
    navController: NavHostController
) {
    val gameDataManager = remember { GameDataManager() }
    val gameData = gameDataManager.getGameData()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current

    var showSuccess by remember { mutableStateOf(gameDataManager.getShowSuccess()) }
    var showError by remember { mutableStateOf(gameDataManager.getShowError()) }
    var isDeleteRandomEnabled by remember { mutableStateOf(true) }

    var movingLetter by remember { mutableStateOf<LetterData?>(null) }
    val availableLettersPositions = remember { mutableStateMapOf<Int, LetterPosition>() }
    val answerPositions = remember { mutableStateMapOf<Int, LetterPosition>() }

    var animatedPosition by remember { mutableStateOf(Offset.Zero) }
    var letterSourcePosition by remember { mutableStateOf(Offset.Zero) }
    var letterTargetPosition by remember { mutableStateOf(Offset.Zero) }

    var isLetterMoving by remember { mutableStateOf(false) }
    var movingLetterIndex by remember { mutableIntStateOf(-1) }
    var targetLetterIndex by remember { mutableIntStateOf(-1) }

    var pendingLetter by remember { mutableStateOf<Pair<Int, LetterData>?>(null) }

    rememberUpdatedState(gameData.selectedLetters)
    var selectedLettersCount by remember { mutableIntStateOf(gameData.selectedLetters.size) }
    var currentLives by remember { mutableIntStateOf(5) }
    val maxLives = 5

    var isRecharging by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(1800) }

    LaunchedEffect(isRecharging) {
        if (isRecharging) {
            while (timeRemaining > 0) {
                delay(1000)
                timeRemaining--

                if (timeRemaining <= 0) {
                    currentLives = maxLives
                    isRecharging = false
                    timeRemaining = 1800
                }
            }
        }
    }

    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            delay(2000)
            gameDataManager.setShowSuccess(false)
            showSuccess = false
        }
    }

    LaunchedEffect(showError) {
        if (showError) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            delay(1500)
            gameDataManager.setShowError(false)
            showError = false
            if (currentLives > 0) {
                currentLives--
                if (currentLives <= 0) {
                    isRecharging = true
                }
            }
        }
    }

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
    val context  = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.en_font
    }))
    val playerStats = preferencesManager.getPlayerStats()
    CompositionLocalProvider(
        LocalContext provides LocalContext.current.createConfigurationContext(
            Configuration().apply { setLocale(currentLocale) }
        )
    ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C1638))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img2),
                    contentDescription = stringResource(R.string.japanese_background_desc),
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(alpha = 0.4f),
                    contentScale = ContentScale.Crop
                )
                TopGameBar(
                    playerStats = playerStats,
                    onOpenStore = {},
                    onOpenProfile = {},
                    backgroundColor = Color.Transparent,
                    showDriver = false,
                    onBackClicked = {
                        navController.popBackStack()
                    },
                    fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
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

            QuestionBar()

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
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        val letterBoxes = updateAnswerBoxes(
                            gameData = gameData,
                            answerPositions = answerPositions,
                            targetLetterIndex = targetLetterIndex,
                            isLetterMoving = isLetterMoving,
                            pendingLetter = pendingLetter,
                            onPositionCaptured = { index, position, size ->
                                answerPositions[index] = LetterPosition(
                                    letter = if (index < gameData.selectedLetters.size)
                                        gameData.selectedLetters[index].letter.toString() else "",
                                    startPosition = position,
                                    size = size
                                )
                            }
                        )

                        letterBoxes.forEach { letterBox ->
                            letterBox()
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val firstRowIndices = (0 until 5).toList().reversed()
                            for (i in firstRowIndices) {
                                if (i < gameData.availableLetters.size) {
                                    val letter = gameData.availableLetters[i]
                                    if (isLetterMoving && movingLetterIndex == i) {
                                        Box(
                                            modifier = Modifier.size(48.dp)
                                        )
                                    } else {
                                        ArabicLetterButtonWithPosition(
                                            letter = letter.letter.toString(),
                                            onClick = {
                                                if (!letter.isSelected && !letter.isDeleted) {
                                                    movingLetterIndex = i
                                                    targetLetterIndex = gameData.selectedLetters.size

                                                    letterSourcePosition = availableLettersPositions[i]?.startPosition ?: Offset.Zero
                                                    letterTargetPosition = answerPositions[targetLetterIndex]?.startPosition ?: Offset.Zero

                                                    isLetterMoving = true
                                                    movingLetter = letter

                                                    pendingLetter = Pair(targetLetterIndex, letter)

                                                    coroutineScope.launch {
                                                        animatePosition(
                                                            letterSourcePosition,
                                                            letterTargetPosition
                                                        ) { currentPosition ->
                                                            animatedPosition = currentPosition
                                                        }

                                                        isLetterMoving = false
                                                        movingLetter = null

                                                        gameDataManager.selectLetter(letter)
                                                        selectedLettersCount = gameData.selectedLetters.size

                                                        delay(100)
                                                        pendingLetter = null

                                                        showSuccess = gameDataManager.getShowSuccess()
                                                        showError = gameDataManager.getShowError()
                                                    }
                                                }
                                            },
                                            isSelected = letter.isSelected,
                                            isDeleted = letter.isDeleted,
                                            onPositionCaptured = { position, size ->
                                                availableLettersPositions[i] = LetterPosition(
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

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val secondRowIndices = (5 until gameData.availableLetters.size).toList().reversed()
                            for (i in secondRowIndices) {
                                val letter = gameData.availableLetters[i]
                                if (isLetterMoving && movingLetterIndex == i) {
                                    Box(
                                        modifier = Modifier.size(48.dp)
                                    )
                                } else {
                                    ArabicLetterButtonWithPosition(
                                        letter = letter.letter.toString(),
                                        onClick = {
                                            if (!letter.isSelected && !letter.isDeleted) {
                                                movingLetterIndex = i
                                                targetLetterIndex = gameData.selectedLetters.size

                                                letterSourcePosition = availableLettersPositions[i]?.startPosition ?: Offset.Zero
                                                letterTargetPosition = answerPositions[targetLetterIndex]?.startPosition ?: Offset.Zero

                                                isLetterMoving = true
                                                movingLetter = letter

                                                pendingLetter = Pair(targetLetterIndex, letter)

                                                coroutineScope.launch {
                                                    animatePosition(
                                                        letterSourcePosition,
                                                        letterTargetPosition
                                                    ) { currentPosition ->
                                                        animatedPosition = currentPosition
                                                    }

                                                    isLetterMoving = false
                                                    movingLetter = null

                                                    gameDataManager.selectLetter(letter)
                                                    selectedLettersCount = gameData.selectedLetters.size

                                                    delay(100)
                                                    pendingLetter = null

                                                    showSuccess = gameDataManager.getShowSuccess()
                                                    showError = gameDataManager.getShowError()
                                                }
                                            }
                                        },
                                        isSelected = letter.isSelected,
                                        isDeleted = letter.isDeleted,
                                        onPositionCaptured = { position, size ->
                                            availableLettersPositions[i] = LetterPosition(
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
                            fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            ControlButtons(
                onHintClick = {
                    val correctLetters = gameData.targetWord.toCharArray().distinct()
                    val availableCorrectLetters = gameData.availableLetters.filter {
                        it.letter in correctLetters && !it.isSelected && !it.isDeleted
                    }

                    if (availableCorrectLetters.isNotEmpty()) {
                        val randomCorrectLetter = availableCorrectLetters.random()
                        val emptyPosition = gameData.selectedLetters.size

                        if (emptyPosition < gameData.targetWord.length) {
                            gameDataManager.selectLetter(randomCorrectLetter)
                            selectedLettersCount = gameData.selectedLetters.size
                        }
                    }
                },
                onSkipClick = {
                    gameDataManager.skipWord()
                    selectedLettersCount = 0
                },
                onDeleteLetter = {
                    gameDataManager.deleteLetter()
                    pendingLetter = null
                    selectedLettersCount = gameData.selectedLetters.size
                    showSuccess = gameDataManager.getShowSuccess()
                    showError = gameDataManager.getShowError()
                },
                onClearAll = {
                    gameDataManager.clearAllLetters()
                    pendingLetter = null
                    selectedLettersCount = 0
                    showSuccess = gameDataManager.getShowSuccess()
                    showError = gameDataManager.getShowError()
                },
                onDeleteRandomLetters = {
                    if (isDeleteRandomEnabled) {
                        gameDataManager.deleteRandomLetters(2)
                        isDeleteRandomEnabled = false
                    }
                },
                isDeleteRandomEnabled = isDeleteRandomEnabled
            )
        }
    }

    LaunchedEffect(selectedLettersCount) {
        for (i in gameData.targetWord.indices) {
            val hasLetter = i < gameData.selectedLetters.size
            if (answerPositions.containsKey(i)) {
                answerPositions[i] = answerPositions[i]!!.copy(
                    letter = if (hasLetter && i < gameData.selectedLetters.size)
                        gameData.selectedLetters[i].letter.toString() else ""
                )
            }
        }
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
                    fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
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
                        shape = androidx.compose.foundation.shape.CircleShape
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
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}

fun updateAnswerBoxes(
    gameData: GameData,
    answerPositions: MutableMap<Int, LetterPosition>,
    targetLetterIndex: Int,
    isLetterMoving: Boolean,
    pendingLetter: Pair<Int, LetterData>?,
    onPositionCaptured: (Int, Offset, IntSize) -> Unit
): List<@Composable () -> Unit> {
    val reversedIndices = gameData.targetWord.indices.reversed().toList()

    return reversedIndices.mapNotNull { i ->
        if (gameData.targetWord[i] == ' ') {
            if (i < gameData.targetWord.length - 1 && gameData.targetWord[i + 1] == ' ') {
                return@mapNotNull {
                    Row {
                        createLetterBox(i, gameData, answerPositions, targetLetterIndex, isLetterMoving, pendingLetter, onPositionCaptured)
                        Box(modifier = Modifier.size(16.dp, 44.dp))
                    }
                }
            }
            null
        } else {
            return@mapNotNull {
                createLetterBox(i, gameData, answerPositions, targetLetterIndex, isLetterMoving, pendingLetter, onPositionCaptured)
            }
        }
    }
}

@Composable
private fun createLetterBox(
    index: Int,
    gameData: GameData,
    answerPositions: MutableMap<Int, LetterPosition>,
    targetLetterIndex: Int,
    isLetterMoving: Boolean,
    pendingLetter: Pair<Int, LetterData>?,
    onPositionCaptured: (Int, Offset, IntSize) -> Unit
) {
    val hasLetter = index < gameData.selectedLetters.size
    val isTargetForMovingLetter = isLetterMoving && targetLetterIndex == index
    val currentLetter = if (hasLetter) gameData.selectedLetters[index].letter.toString() else ""

    val isCorrectPosition = hasLetter &&
            index < gameData.targetWord.length &&
            gameData.selectedLetters[index].letter.toLowerCase() == gameData.targetWord[index].toLowerCase()

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