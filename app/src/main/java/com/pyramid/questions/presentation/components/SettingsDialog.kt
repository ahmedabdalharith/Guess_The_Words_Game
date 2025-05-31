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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.ui.theme.GuessTheWordsGameTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    onCloseClick: () -> Unit = {},
    onSelectLanguageClick: () -> Unit = {},
    onStatisticsClick: () -> Unit = {},
    onResetGameClick: () -> Unit = {},
    onRateUsClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsOfUseClick: () -> Unit = {},
    onContactUsClick: () -> Unit = {},
    onMusicToggle: (Boolean) -> Unit = {},
    onSoundToggle: (Boolean) -> Unit = {},
    onVibrationToggle: (Boolean) -> Unit = {},
    isMusicEnabled: Boolean = true,
    isSoundEnabled: Boolean = true,
    isVibrationEnabled: Boolean = false,
    appVersion: String = "V1.0.0.0",
    navController:NavController = rememberNavController()
) {
    val preferencesManager = remember { GamePreferencesManager(navController.context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    var currentLanguage by remember { mutableStateOf(currentLocale) }
    val arabicFont = FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.eng3
    }))

    // استخدام state محلية لتتبع حالة الإعدادات مع القيم الأولية من PreferencesManager
    var isMusicEnabledState by remember { mutableStateOf(preferencesManager.isMusicEnabled()) }
    var isSoundEnabledState by remember { mutableStateOf(preferencesManager.isSoundEnabled()) }
    var isVibrationEnabledState by remember { mutableStateOf(preferencesManager.isVibrationEnabled()) }

    // State to manage dialog visibility
    var showRatingDialog by remember { mutableStateOf(false) }
    var showStatisticsDialog by remember { mutableStateOf(false) }
    var showGameResetDialog by remember { mutableStateOf(false) }
    var showSelectLanguageDialog by remember { mutableStateOf(false) }

    // Functions to handle settings changes and save them
    val onMusicToggleInternal: (Boolean) -> Unit = { enabled ->
        isMusicEnabledState = enabled
        preferencesManager.setMusicEnabled(enabled)
        onMusicToggle(enabled)
    }

    val onSoundToggleInternal: (Boolean) -> Unit = { enabled ->
        isSoundEnabledState = enabled
        preferencesManager.setSoundEnabled(enabled)
        onSoundToggle(enabled)
    }

    val onVibrationToggleInternal: (Boolean) -> Unit = { enabled ->
        isVibrationEnabledState = enabled
        preferencesManager.setVibrationEnabled(enabled)
        onVibrationToggle(enabled)
    }

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
                    SettingsDialogContent(
                        arabicFont = arabicFont,
                        appVersion = appVersion,
                        onCloseClick = onCloseClick,
                        onSelectLanguageClick = {
                            showSelectLanguageDialog = true
                        },
                        onStatisticsClick = {
                            showStatisticsDialog = true
                        },
                        onResetGameClick = {
                            showGameResetDialog = true
                        },
                        onRateUsClick = {
                            showRatingDialog = true
                        },
                        onPrivacyPolicyClick = onPrivacyPolicyClick,
                        onTermsOfUseClick = onTermsOfUseClick,
                        onContactUsClick = onContactUsClick,
                        onMusicToggle = onMusicToggleInternal,
                        onSoundToggle = onSoundToggleInternal,
                        onVibrationToggle = onVibrationToggleInternal,
                        isMusicEnabled = isMusicEnabledState,
                        isSoundEnabled = isSoundEnabledState,
                        isVibrationEnabled = isVibrationEnabledState
                    )
                }

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

    if (showRatingDialog) {
        RatingDialog(
            onCloseClick = {
                showRatingDialog = false
                onCloseClick()
            },
            onRatingSubmit = { rating ->
                // Handle rating submission
                onRateUsClick()
                showRatingDialog = false
                onCloseClick()
            }
        )
        return
    }

    if (showStatisticsDialog) {
        StatisticsDialog(
            onCloseClick = {
                onStatisticsClick()
                showStatisticsDialog = false
                onCloseClick()
            }
        )
        return
    }
    if (showGameResetDialog) {
        GameResetDialog(
            onResetClick = {
                onResetGameClick()
                showGameResetDialog = false
            },
            onCancelClick = {
                showGameResetDialog = false
            },
            showDialog = true
        )
        return
    }

    if (showSelectLanguageDialog) {
        SelectLanguageDialog(
            onCloseClick = {
                showSelectLanguageDialog = false
                onCloseClick()
            },
            onLanguageSelected = { language ->
                // Update language when selected
                currentLanguage = Locale(language.code)
                preferencesManager.setLanguage(language.code)
                onSelectLanguageClick()
                showSelectLanguageDialog = false
                onCloseClick()
            }
        )
        return
    }
}

@Composable
private fun SettingsDialogContent(
    arabicFont: FontFamily,
    appVersion: String,
    onCloseClick: () -> Unit,
    onSelectLanguageClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onResetGameClick: () -> Unit,
    onRateUsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfUseClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onMusicToggle: (Boolean) -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onVibrationToggle: (Boolean) -> Unit,
    isMusicEnabled: Boolean,
    isSoundEnabled: Boolean,
    isVibrationEnabled: Boolean
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
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.settings),
                fontSize = 28.sp,
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
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )

            ToggleOptionsRow(
                isMusicEnabled = isMusicEnabled,
                isSoundEnabled = isSoundEnabled,
                isVibrationEnabled = isVibrationEnabled,
                onMusicToggle = onMusicToggle,
                onSoundToggle = onSoundToggle,
                onVibrationToggle = onVibrationToggle
            )

            SettingsMenuOptions(
                arabicFont = arabicFont,
                onSelectLanguageClick = onSelectLanguageClick,
                onStatisticsClick = onStatisticsClick,
                onResetGameClick = onResetGameClick,
                onRateUsClick = onRateUsClick,
                onContactUsClick = onContactUsClick
            )

            Spacer(modifier = Modifier.height(40.dp))

            SettingsFooter(
                arabicFont = arabicFont,
                appVersion = appVersion,
                onPrivacyPolicyClick = onPrivacyPolicyClick,
                onTermsOfUseClick = onTermsOfUseClick
            )
        }
    }
}

@Composable
private fun ToggleOptionsRow(
    isMusicEnabled: Boolean,
    isSoundEnabled: Boolean,
    isVibrationEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onVibrationToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Music Toggle Button
        YellowButtonWithIcon(
            onClick = { onMusicToggle(!isMusicEnabled) },
            width = 50.dp,
            height = 50.dp,
            content = {
                Icon(
                    painter = painterResource(
                        id = if (isMusicEnabled) R.drawable.music_iic else R.drawable.music_off_ic
                    ),
                    contentDescription = stringResource(
                        if (isMusicEnabled) R.string.music_off else R.string.music_on
                    ),
                    tint = Color(0xFF4E3F07),
                    modifier = Modifier
                        .size(38.dp)
                        .padding(4.dp)
                )
            }
        )

        // Sound Toggle Button - تم تصحيح الأيقونة
        YellowButtonWithIcon(
            onClick = { onSoundToggle(!isSoundEnabled) },
            width = 50.dp,
            height = 50.dp,
            content = {
                Icon(
                    painter = painterResource(
                        id = if (isSoundEnabled) R.drawable.sound_ic else R.drawable.sound_off_ic
                    ),
                    contentDescription = stringResource(
                        if (isSoundEnabled) R.string.sound_off else R.string.sound_on
                    ),
                    tint = Color(0xFF4E3F07),
                    modifier = Modifier
                        .size(38.dp)
                        .padding(4.dp)
                )
            }
        )

        // Vibration Toggle Button - تم إضافة أيقونة مختلفة للحالة المغلقة
        YellowButtonWithIcon(
            onClick = { onVibrationToggle(!isVibrationEnabled) },
            width = 50.dp,
            height = 50.dp,
            content = {
                Icon(
                    painter = painterResource(
                        id = if (isVibrationEnabled) R.drawable.vibration_ic else R.drawable.vibration_ic
                    ),
                    contentDescription = stringResource(
                        if (isVibrationEnabled) R.string.vibration_off else R.string.vibration_on
                    ),
                    tint = Color(0xFF4E3F07),
                    modifier = Modifier
                        .size(38.dp)
                        .padding(4.dp)
                )
            }
        )
    }
}

@Composable
private fun SettingsMenuOptions(
    arabicFont: FontFamily,
    onSelectLanguageClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onResetGameClick: () -> Unit,
    onRateUsClick: () -> Unit,
    onContactUsClick: () -> Unit
) {
    val menuOptions = listOf(
        MenuItem(stringResource(R.string.select_language), onSelectLanguageClick),
        MenuItem(stringResource(R.string.statistics), onStatisticsClick),
        MenuItem(stringResource(R.string.reset_game), onResetGameClick),
        MenuItem(stringResource(R.string.rate_us), onRateUsClick),
        MenuItem(stringResource(R.string.contact_us), onContactUsClick)
    )

    menuOptions.forEachIndexed { index, menuItem ->
        YellowButton(
            onClick = menuItem.onClick,
            text = menuItem.text,
            fontFamily = arabicFont,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        if (index < menuOptions.size - 1) {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private data class MenuItem(
    val text: String,
    val onClick: () -> Unit
)

@Composable
private fun SettingsFooter(
    arabicFont: FontFamily,
    appVersion: String,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfUseClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stringResource(R.string.privacy_policy),
            fontSize = 14.sp,
            color = Color.White,
            fontFamily = arabicFont, // إضافة الخط العربي
            modifier = Modifier.clickable { onPrivacyPolicyClick() },
            textDecoration = TextDecoration.Underline
        )
        Text(
            text = stringResource(R.string.terms_of_use),
            fontSize = 14.sp,
            color = Color.White,
            fontFamily = arabicFont, // إضافة الخط العربي
            modifier = Modifier.clickable { onTermsOfUseClick() },
            textDecoration = TextDecoration.Underline
        )
    }
    Text(
        text = appVersion,
        fontSize = 14.sp,
        color = Color.White,
        fontFamily = arabicFont, // إضافة الخط العربي
        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    GuessTheWordsGameTheme {
        SettingsDialog(
            onMusicToggle = { enabled ->
                println("Music toggled: $enabled")
            },
            onSoundToggle = { enabled ->
                println("Sound toggled: $enabled")
            },
            onVibrationToggle = { enabled ->
                println("Vibration toggled: $enabled")
            },
            onContactUsClick = {
                println("Contact us clicked")
            },
            onSelectLanguageClick = {
                println("Select language clicked")
            },
            onStatisticsClick = {
                println("Statistics clicked")
            },
            onResetGameClick = {
                println("Reset game clicked")
            },
            onRateUsClick = {
                println("Rate us clicked")
            },
            onPrivacyPolicyClick = {
                println("Privacy policy clicked")
            },
            onTermsOfUseClick = {
                println("Terms of use clicked")
            },
        )
    }
}