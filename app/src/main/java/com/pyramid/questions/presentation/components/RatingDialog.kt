package com.pyramid.questions.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
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
fun RatingDialog(
    onCloseClick: () -> Unit = {},
    onRatingSubmit: (Int) -> Unit = {},
    onNoThanksClick: () -> Unit = {},
    showDialog: Boolean = true
) {
    val context  = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.eng3
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
                    .aspectRatio(0.85f)
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
                    RatingDialogContent(
                        onRatingSubmit = onRatingSubmit,
                        onNoThanksClick = onNoThanksClick
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
    }
}

@Composable
private fun RatingDialogContent(
    onRatingSubmit: (Int) -> Unit,
    onNoThanksClick: () -> Unit
) {
    var selectedRating by remember { mutableIntStateOf(0) }
    var isSubmitting by remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = stringResource(id = R.string.rating_dialog_title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(3f, 3f),
                    blurRadius = 5f
                )
            ),
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )

        // Subtitle
        Text(
            text = stringResource(id = R.string.rating_dialog_subtitle),
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.rating_dialog_game_name),
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Yellow,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Star Rating Row
        Box {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                repeat(5) { index ->
                    StarButton(
                        isSelected = index < selectedRating,
                        starIndex = index,
                        onClick = {
                            selectedRating = index + 1
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )
                }
            }
            if (selectedRating == 0) {
                Image(
                    painter = painterResource(id = R.drawable.hand_pointing_ic),
                    contentDescription = stringResource(id = R.string.hand_pointing_content_description),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(80.dp)
                        .align(Alignment.CenterEnd)
                        .padding(start = 16.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        val isEnabled = selectedRating > 0 && !isSubmitting
        CustomButton(
            onClick = {
                if (selectedRating > 0 && !isSubmitting) {
                    isSubmitting = true
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onRatingSubmit(selectedRating)
                }
            },
            content = {
                Text(
                    text = stringResource(id = R.string.rating_dialog_submit_button),
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            },
            backgroundColor = if (isEnabled) Color(0xFF4CAF50) else Color(0xFF2E7D32).copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxWidth()
                .width(100.dp).height(60.dp)
                .padding(horizontal = 32.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.rating_dialog_no_thanks),
            fontSize = 18.sp,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
            modifier = Modifier
                .clickable { onNoThanksClick() }
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Composable
private fun StarButton(
    isSelected: Boolean,
    starIndex: Int,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(
            id = if (isSelected) R.drawable.star else R.drawable.star_empty_ic
        ),
        contentDescription = stringResource(id = R.string.star_content_description, starIndex + 1),
        modifier = Modifier
            .size(52.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentScale = ContentScale.Crop,
    )
}

@Preview(showBackground = true)
@Composable
fun RatingDialogPreview() {
    GuessTheWordsGameTheme {
        RatingDialog(
            onRatingSubmit = { rating ->
                println("Rating submitted: $rating")
            },
            onNoThanksClick = {
                println("No thanks clicked")
            },
            onCloseClick = {
                println("Close clicked")
            }
        )
    }
}