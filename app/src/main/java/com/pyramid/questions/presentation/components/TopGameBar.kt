package com.pyramid.questions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.pyramid.questions.AppColors
import com.pyramid.questions.R
import com.pyramid.questions.domain.model.Player
import com.pyramid.questions.presentation.levels.Divider3D


@Composable
fun TopGameBar(
    player: Player,
    onOpenStore: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    fontFamily: FontFamily,
    backgroundColor: Color = Color(0xFF0D47A1),
    showDriver : Boolean = false,
    showButtonAdd : Boolean = true,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopBarComponents(
                icon = R.drawable.coin1,
                value = player.coins,
                gradientColors = listOf(AppColors.CoinsGradientStart, AppColors.CoinsGradientEnd),
                borderColor = Color(0xFF4C7CE4),
                fontFamily = fontFamily,
                showAddButton = showButtonAdd,
                onAddClick = onOpenStore,
            )
            TopBarComponents(
                icon = R.drawable.star,
                value = player.stars,
                gradientColors = listOf(AppColors.StarsGradientStart, AppColors.StarsGradientEnd),
                borderColor = Color(0xFFE4AF4C),
                fontFamily = fontFamily,
            )
            BlueButton(
                onClick = onBackClicked,
                size = 32.dp,
                icon = R.drawable.arrow_forward_ic
            )
        }
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        if (showDriver){
            Divider3D()

        }
    }
}