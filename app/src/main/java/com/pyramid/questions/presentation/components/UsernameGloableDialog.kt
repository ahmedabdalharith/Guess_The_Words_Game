package com.pyramid.questions.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.ui.theme.GuessTheWordsGameTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameGloableDialog(
    onCloseClick: () -> Unit = {},
    onClickYes: (String) -> Unit = {},
    title : String = stringResource(R.string.choose_name)
) {

    var username by remember { mutableStateOf("") }
    val context = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    val arabicFont = FontFamily(
        Font(
            if (currentLocale == Locale("ar")) {
                R.font.arbic_font_bold_2
            } else {
                R.font.eng3
            }
        )
    )

    LaunchedEffect(Unit) {
        username = if (preferencesManager.getUsername().isNullOrEmpty()) {
            ""
        }else
            preferencesManager.getUsername().toString()
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
                    .aspectRatio(1.1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) { }
            ) {
                Box(
                    modifier = Modifier
                        .padding(24.dp)
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
                    Column {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .background(Color.Transparent)
                        )
                        Text(
                            text =title,
                            fontFamily = arabicFont,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.choose_name_description),
                            fontFamily = arabicFont,
                            fontSize = 10.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .padding(start = 36.dp, end = 36.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color(0xFF1E3244),
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            UsernameTextField(
                                value = username,
                                onValueChange = { username = it },
                                modifier = Modifier.fillMaxWidth(),
                                onRefreshClick = {
                                    username = if (username.isEmpty()) {
                                        "User${(1000..9999).random()}"
                                    } else {
                                        preferencesManager.getUsername().toString()
                                    }
                                }
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .background(Color.Transparent)
                        )
                        YellowButton(
                            onClick = {
                                if (username.isNotBlank()) {
                                    preferencesManager.setUsername(username.trim())
                                    onClickYes(username.trim())
                                }
                            },
                            text = stringResource(R.string.yes),
                            modifier = Modifier
                                .width(220.dp)
                                .padding(start = 16.dp, end = 16.dp)
                                .align(Alignment.CenterHorizontally),
                            fontFamily = arabicFont
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.name_change_note),
                            fontFamily = arabicFont,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                    }

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
}

@Composable
fun UsernameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onRefreshClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF02254B))
            .padding(vertical = 4.dp, horizontal = 12.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Right
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                )
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.enter_name_placeholder),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            IconButton(
                onClick = onRefreshClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.refresh_content_description),
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewUsernameGloableDialog() {
    GuessTheWordsGameTheme {
        UsernameGloableDialog()
    }
}