package com.pyramid.questions.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TopBarComponents(
    modifier: Modifier = Modifier,
    icon: Int,
    value: Int? = null,
    gradientColors: List<Color>,
    borderColor: Color,
    fontFamily: FontFamily,
    showAddButton: Boolean = false,
    onAddClick: () -> Unit = {},
    isBtnBack: Boolean = false,
    onBtnClick: () -> Unit ={},
    isBtnSettings : Boolean = false,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center

    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.5.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                .height(24.dp)
                .wrapContentWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (
                    isBtnBack || isBtnSettings
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(onClick = onBtnClick)
                            .size(28.dp)
                    )
                }
                if (!isBtnBack && !isBtnSettings) {
                    Spacer(
                        modifier = Modifier
                            .width(if (isBtnBack) 4.dp else 42.dp)
                            .height(36.dp)
                    )
                }
                if (value != null) {
                    Text(
                        text = value.toString(),
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                    if (showAddButton) {
                        GreenAddButton(
                            onClick = onAddClick,
                            size = 28.dp
                        )
                    }else
                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                }
            }
        }
        if (!isBtnBack && !isBtnSettings) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(48.dp)
                    .offset(
                        x = (-20).dp,
                        y = (2).dp
                    ),
                contentScale = ContentScale.Crop
            )
        }
    }
}