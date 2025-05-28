package com.pyramid.questions.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.ui.theme.GuessTheWordsGameTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsDialog(
    onCloseClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    showDialog: Boolean = true,
    totalQuestions: Int = 2001,
    solvedRecently: Int = 2,
    notSolvedYet: Int = 1999,
    gameProgress: Float = 0.1f,
    totalHintsUsed: Int = 5,
    failedAttempts: Int = 8,
    membershipLevel: Int = 0
) {
    val context  = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.en_font
    }))
    if (showDialog) {
        BasicAlertDialog(
            onDismissRequest = onCloseClick,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            CompositionLocalProvider(
                LocalContext provides LocalContext.current.createConfigurationContext(
                    Configuration().apply { setLocale(currentLocale) }
                )
            ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .aspectRatio(0.6f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { /* Prevent clicks from propagating */ }
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF4C7ED3), Color(0xFF1E4998)),
                                center = Offset.Infinite,
                                radius = 900f
                            )
                        )
                        .border(
                            width = 8.dp,
                            color = Color(0xFF3D9CFF),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    StatisticsDialogContent(
                        totalQuestions = totalQuestions,
                        solvedRecently = solvedRecently,
                        notSolvedYet = notSolvedYet,
                        gameProgress = gameProgress,
                        totalHintsUsed = totalHintsUsed,
                        failedAttempts = failedAttempts,
                        membershipLevel = membershipLevel,
                        onBackClick = onBackClick
                    )
                }

                // Close button
                RedCloseButton(
                    icon = R.drawable.close_ic,
                    onClick = onCloseClick,
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-32).dp, y = 2.dp)
                )
            }
        }
    }
    }
}

@Composable
private fun StatisticsDialogContent(
    totalQuestions: Int,
    solvedRecently: Int,
    notSolvedYet: Int,
    gameProgress: Float,
    totalHintsUsed: Int,
    failedAttempts: Int,
    membershipLevel: Int,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = stringResource(R.string.statistics_title),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.3f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Questions Statistics Card
        StatisticsCard(
            title = stringResource(R.string.statistics_total_questions),
            mainValue = totalQuestions.toString(),
            items = listOf(
                StatItem(stringResource(R.string.statistics_solved_recently), solvedRecently.toString()),
                StatItem(stringResource(R.string.statistics_not_solved_yet), notSolvedYet.toString())
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Card
        StatisticsCard(
            title = stringResource(R.string.statistics_game_progress),
            mainValue = stringResource(R.string.statistics_progress_percentage, gameProgress),
            items = emptyList(),
            showProgressBar = true,
            progress = gameProgress / 100f
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Usage Statistics Card
        StatisticsCard(
            title = "",
            mainValue = "",
            items = listOf(
                StatItem(stringResource(R.string.statistics_total_hints_used), totalHintsUsed.toString()),
                StatItem(stringResource(R.string.statistics_failed_attempts), failedAttempts.toString())
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Membership Level Card
        StatisticsCard(
            title = stringResource(R.string.statistics_membership_level),
            mainValue = membershipLevel.toString(),
            items = emptyList(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Back Button
        CustomButton(
            onClick = onBackClick,
            content = {
                Text(
                    text = stringResource(R.string.statistics_back_button),
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            },
            backgroundColor = Color(0xFFFFB000),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun StatisticsCard(
    title: String,
    mainValue: String,
    items: List<StatItem>,
    showProgressBar: Boolean = false,
    progress: Float = 0f,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF020A24).copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title and main value row
            if (title.isNotEmpty() && mainValue.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = mainValue,
                        fontSize = 18.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                        fontWeight = FontWeight.Bold
                    )
                }

                if (items.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Progress bar for progress card
            if (showProgressBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .background(
                                color = Color(0xFF4CAF50),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            // Items list
            items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontFamily = FontFamily(Font(R.font.arbic_font_bold_2))
                    )
                    Text(
                        text = item.value,
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                        fontWeight = FontWeight.Bold
                    )
                }
                if (item != items.last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

data class StatItem(
    val label: String,
    val value: String
)

@Preview(showBackground = true)
@Composable
fun StatisticsDialogPreview() {
    GuessTheWordsGameTheme {
        StatisticsDialog(
            onCloseClick = { println("Close clicked") },
            onBackClick = { println("Back clicked") }
        )
    }
}