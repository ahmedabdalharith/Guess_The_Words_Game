package com.pyramid.questions.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pyramid.questions.AppColors
import com.pyramid.questions.R
import com.pyramid.questions.domain.model.Player
import com.pyramid.questions.navigation.Route

@Composable
fun TopHomeBar(
    navController: NavHostController,
    player: Player,
    onOpenStore: () -> Unit = {},
    fontFamily: FontFamily,
    onOpenSettings: () -> Unit = {},
    onVideoClick: () -> Unit = {},
    onWorldClick : () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0D47A1)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LevelProgressUI()
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
                showAddButton = true,
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
                onClick = onOpenSettings,
                size = 32.dp,
                icon = R.drawable.settings_ic
            )
        }
        UnderTopBarRow(
            onSpinClick = {navController.navigate(Route.WHEEL)
            },
            onVideoClick = onVideoClick,
            onGlobleClick = onWorldClick

        )

    }
}
@Composable
fun UnderTopBarRow(
    onVideoClick : () -> Unit = {},
    onSpinClick : () -> Unit = {},
    onGlobleClick : () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(AppColors.BackgroundStart, Color(0xFF0D47A1))
                )
            )
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable(onClick = onGlobleClick)
        ){
            Image(
                painter = painterResource(id = R.drawable.globle),
                contentDescription = stringResource(R.string.globe_content_description),
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            GreenTextButton(
                onClick = onGlobleClick,
                text = stringResource(R.string.play_button),
                modifier = Modifier.width(70.dp).height(
                    25.dp
                ).align(
                    Alignment.BottomCenter
                ),
                fontSize = 12.sp
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable(onClick = onSpinClick)
        ){
            Image(
                painter = painterResource(id = R.drawable.spin_wheel),
                contentDescription = stringResource(R.string.spin_wheel_content_description),
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            GreenTextButton(
                onClick = onSpinClick,
                text = stringResource(R.string.spin_button),
                modifier = Modifier.width(70.dp).height(
                    25.dp
                ).align(
                    Alignment.BottomCenter
                ),
                fontSize = 12.sp
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable(
                onClick = onVideoClick
            )
        ){
            Image(
                painter = painterResource(id = R.drawable.which_video),
                contentDescription = stringResource(R.string.video_content_description),
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            ProgressIndicator(
                progress = stringResource(R.string.progress_indicator),
                modifier = Modifier.width(70.dp).height(
                    25.dp
                ).align(
                    Alignment.BottomCenter
                )
            )
        }

    }
}