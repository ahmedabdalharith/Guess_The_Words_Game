package com.pyramid.questions.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyramid.questions.R


@Composable
fun SuccessSection(
    level: String = "6",
    answer: String = "التالي",
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            GameProgressBarSpacial(
                current = 1,
                total = 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            CustomButton(
                onClick = onContinueClick,
                content = {
                    Text(
                        text = stringResource(R.string.next),
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                backgroundColor = Color(0xFF2ae272),
                modifier = Modifier
                    .width(180.dp)
                    .height(60.dp)
            )
        }
    }
}

@Preview
@Composable
fun SuccessScreenPreview(){
    SuccessSection(
        onContinueClick = { /* Handle continue click */ },
        modifier = Modifier.fillMaxSize()
    )
}