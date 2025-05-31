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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
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
fun GameResetDialog(
    onCloseClick: () -> Unit = {},
    onResetClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    showDialog: Boolean = true
) {
    val context = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    FontFamily(
        Font(
            if (currentLocale == Locale("ar")) {
                R.font.arbic_font_bold_2
            } else {
                R.font.eng3
            }
        )
    )
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
                        .aspectRatio(1.3f)
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
                                width = 6.dp,
                                color = Color(0xFF3D9CFF),
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        GameResetDialogContent(
                            onResetClick = onResetClick,
                            onCancelClick = onCancelClick
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
private fun GameResetDialogContent(
    onResetClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "أعد إعداد اللعبة",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.4f),
                    offset = Offset(2f, 3f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Message
        Text(
            text = "هل أنت متأكد من أنك تريد إعادة ضبط اللعبة؟ ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
            textAlign = TextAlign.Center,
            lineHeight = 28.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.3f),
                    offset = Offset(1f, 2f),
                    blurRadius = 4f
                )
            ),
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cancel Button (لا شكراً)
            CustomButton(
                onClick = onCancelClick,
                content = {
                    Text(
                        text = "لا شكراً",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                backgroundColor = Color(0xFFFFB000),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            )

            // Reset Button (إعادة ضبط)
            CustomButton(
                onClick = onResetClick,
                content = {
                    Text(
                        text = "إعادة ضبط",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                backgroundColor = Color(0xFF6B7280),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameResetDialogPreview() {
    GuessTheWordsGameTheme {
        GameResetDialog(
            onCloseClick = { println("Close clicked") },
            onResetClick = { println("Reset clicked") },
            onCancelClick = { println("Cancel clicked") }
        )
    }
}