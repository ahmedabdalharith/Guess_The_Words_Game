package com.pyramid.questions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
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

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLanguageDialog(
    onCloseClick: () -> Unit = {},
    onLanguageSelected: (Language) -> Unit = {},
    selectedLanguage: String = "ar",
    availableLanguages: List<Language> = defaultLanguages(),
) {
    val arabicFont = FontFamily.Default
    val context = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context.applicationContext) }

    BasicAlertDialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .aspectRatio(1f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { }
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
                SelectLanguageDialogContent(
                    arabicFont = arabicFont,
                    onLanguageSelected = { language ->
                        preferencesManager.setLanguage(language.code)
                        onLanguageSelected(language)
                    },
                    selectedLanguage = selectedLanguage,
                    availableLanguages = availableLanguages
                )
            }
            RedCloseButton(
                icon = R.drawable.close_ic,
                onClick = onCloseClick,
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-32).dp, y = (2).dp)
            )
        }
    }
}

@Composable
private fun SelectLanguageDialogContent(
    arabicFont: FontFamily,
    onLanguageSelected: (Language) -> Unit,
    selectedLanguage: String,
    availableLanguages: List<Language>
) {
    Card(
        modifier = Modifier
            .padding(top = 32.dp, start = 8.dp, end = 8.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D47A1)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "اختر اللغة",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = arabicFont,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(3f, 3f),
                        blurRadius = 5f
                    )
                ),
                modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
            )

            // Language Options
            availableLanguages.forEachIndexed { index, language ->
                LanguageOption(
                    language = language,
                    isSelected = language.code == selectedLanguage,
                    onLanguageSelected = onLanguageSelected
                )

                if (index < availableLanguages.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LanguageOption(
    language: Language,
    isSelected: Boolean,
    onLanguageSelected: (Language) -> Unit
) {
    CustomButton(
        onClick = { onLanguageSelected(language) },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
        content = {
            Text(
                text = language.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = if (language.code == "ar") {
                    FontFamily(Font(R.font.arbic_font_bold_2))
                } else {
                    FontFamily(Font(R.font.font_bold))
                },
                color = Color(0xFF4E3F07)
            )
        }
    )
}

// Default language options
private fun defaultLanguages(): List<Language> {
    return listOf(
        Language(
            code = "ar",
            name = "العربية",
            nativeName = "العربية"
        ),
        Language(
            code = "en",
            name = "English",
            nativeName = "English"
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun SelectLanguageDialogPreview() {
    GuessTheWordsGameTheme {
        SelectLanguageDialog(
            onLanguageSelected = { language ->
                println("Language selected: ${language.name}")
            },
            selectedLanguage = "ar"
        )
    }
}