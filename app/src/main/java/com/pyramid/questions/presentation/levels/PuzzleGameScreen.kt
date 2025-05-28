package com.pyramid.questions.presentation.levels

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pyramid.questions.AppColors
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.presentation.components.TopGameBar
import java.util.Locale


@Composable
fun PuzzleGameScreen(
    onOpenStore: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    onImageClick: (Int) -> Unit = {},
    navController: NavHostController= rememberNavController()
) {
    val images = listOf(R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4)
    val preferencesManager = remember { GamePreferencesManager(navController.context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }
    FontFamily(Font(if (currentLocale == Locale("ar")) {
        R.font.arbic_font_bold_2
    } else {
        R.font.en_font
    }))
    val playerStats =preferencesManager.getPlayerStats()
    CompositionLocalProvider(
        LocalContext provides LocalContext.current.createConfigurationContext(
            Configuration().apply { setLocale(currentLocale) }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppColors.BackgroundStart,
                            AppColors.BackgroundEnd
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top bar with player stats
                TopGameBar(
                    playerStats = playerStats,
                    onOpenStore = onOpenStore,
                    onOpenProfile = onOpenProfile,
                    onBackClicked = {
                        navController.popBackStack()
                    },
                    fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Puzzle images grid
                PuzzleImagesGrid(
                    images = images,
                    onImageClick = onImageClick
                )
            }
        }
    }
}

@Composable
fun PuzzleImagesGrid(
    images: List<Int>,
    onImageClick: (Int) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(images.size) { index ->
            PuzzleImageItem(
                imageId = images[index],
                onClick = {
                    onImageClick(images[index])

                }
            )
        }
    }
}

@Composable
fun PuzzleImageItem(
    imageId: Int,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = "Puzzle image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
@Composable
fun Divider3D(
    modifier: Modifier = Modifier,
    topColor: Color = Color(0xFFB0C4DE),
    bottomColor: Color = Color(0xFF4C70A1),
    thickness: Dp = 2.dp
) {
    Column(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(thickness)
                .background(topColor)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(thickness)
                .background(bottomColor)
        )
    }
}

@Preview
@Composable
fun PuzzleGameScreenPreview() {
    PuzzleGameScreen(navController = rememberNavController())
}

