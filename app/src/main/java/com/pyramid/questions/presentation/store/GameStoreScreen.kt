package com.pyramid.questions.presentation.store

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pyramid.questions.AppColors
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.data.remote.AdMobManager
import com.pyramid.questions.data.remote.rememberAdMobManager
import com.pyramid.questions.presentation.components.CustomButton
import com.pyramid.questions.presentation.components.TopGameBar
import java.util.Locale

@Composable
fun GameStoreScreen(
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val preferencesManager = remember { GamePreferencesManager(navController.context) }
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
    val context = LocalContext.current

    var Player by remember { mutableStateOf(preferencesManager.getPlayer()) }
    val adMobManager = rememberAdMobManager()

    val updateCoins = { amount: Int ->
        preferencesManager.addCoins(amount)
        Player = preferencesManager.getPlayer()
    }

    CompositionLocalProvider(
        LocalContext provides LocalContext.current.createConfigurationContext(
            Configuration().apply { setLocale(currentLocale) }
        )
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

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
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    TopGameBar(
                        player = Player,
                        onOpenStore = { },
                        onOpenProfile = { },
                        showDriver = false,
                        showButtonAdd = false,
                        onBackClicked = {
                            navController.popBackStack()
                        },
                        fontFamily = arabicFont,
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            PremiumVipButton(
                                onClick = { },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        SectionTitle(
                            title = stringResource(R.string.free_coins),
                            fontFamily = arabicFont
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)

                        ) {
                            FreeCoinsOption(
                                coinAmount = 50,
                                action = stringResource(R.string.watch_video),
                                icon = R.drawable.video_ic,
                                onClick = {
                                    adMobManager.showVideoAd(
                                        activity = navController.context as Activity,
                                        preferInterstitial = false,
                                        rewardListener = object : AdMobManager.RewardedAdListener {
                                            override fun onUserEarnedReward(amount: Int, type: String) {
                                                updateCoins(50)
                                                Toast.makeText(
                                                    context,
                                                    "You earned 50 coins!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            override fun onAdClosed() {
                                            }

                                            override fun onAdFailedToShow(error: String) {
                                                Toast.makeText(
                                                    context,
                                                    "Failed to show ad: $error",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    )
                                },
                            )

                            FreeCoinsOption(
                                coinAmount = 200,
                                action = stringResource(R.string.share),
                                icon = R.drawable.whatsapp_icon,
                                onClick = {
                                    val shareText = "تطبيق أسئلة الهرم - اختبر معلوماتك واستمتع بالألعاب التعليمية!\n\nحمل التطبيق الآن:\nhttps://play.google.com/store/apps/details?id=com.pyramid.questions"

                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                        setPackage("com.whatsapp")
                                    }

                                    if (intent.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(intent)
                                    } else {
                                        val webIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, shareText)
                                        }
                                        val chooser = Intent.createChooser(webIntent, "مشاركة التطبيق")
                                        context.startActivity(chooser)
                                    }
                                },
                                isWhatsapp = true,
                            )

                            FreeCoinsOption(
                                coinAmount = 100,
                                action = stringResource(R.string.like),
                                icon = R.drawable.facebook_icon,
                                onClick = {
                                    val url = "https://www.facebook.com/PyramidQuestions"
                                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                    context.startActivity(intent)
                                    updateCoins(100)
                                    Toast.makeText(
                                        context,
                                        "تم منحك 100 عملة!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                isFacebook = true,
                            )

                            FreeCoinsOption(
                                coinAmount = 100,
                                action = stringResource(R.string.follow),
                                icon = R.drawable.instagram_icon,
                                onClick = {
                                    val url = "https://www.instagram.com/pyramid_questions/"
                                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                    context.startActivity(intent)
                                    updateCoins(100)
                                    Toast.makeText(
                                        context,
                                        "تم منحك 100 عملة!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                isInstagram = true,
                            )
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumVipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFF6E58D), Color(0xFFDEC057))
                )
            )
            .border(2.dp, Color(0xFFBC7F04), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFD700))
                    .border(2.dp, Color(0xFFFFFFFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.vip),
                    color = Color(0xFF333333),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.vip_icon),
                    contentDescription = "VIP Crown",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.vip_membership),
                color = Color(0xFF333333),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomButton(
                onClick = onClick,
                backgroundColor = Color(0xFF4CAF50),
                content = {
                    Text(
                        text = stringResource(R.string.join),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Default
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    fontFamily: FontFamily
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF3A5CB7))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(1f, 1f),
                    blurRadius = 2f
                )
            )
        )
    }
}

@Composable
fun FreeCoinsOption(
    coinAmount: Int,
    action: String,
    icon: Int,
    onClick: () -> Unit,
    isWhatsapp: Boolean = false,
    isFacebook: Boolean = false,
    isInstagram: Boolean = false,
    isGoogle: Boolean = false,

    ) {
    val buttonColor = when {
        isWhatsapp -> Color(0xFF4CAF50)
        isFacebook -> Color(0xFF1976D2)
        isInstagram -> Color(0xFFC13584)
        isGoogle -> Color(0xFF4285F4)
        else -> Color(0xFFFFB300)
    }
    CustomButton(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5EFD6))
            .border(2.dp, Color(0xFFDFD4A8), RoundedCornerShape(12.dp)),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painterResource(R.drawable.coin1),
                    modifier = Modifier.size(50.dp),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = coinAmount.toString(),
                    color = Color(0xFFFCFCFC),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    fontFamily = FontFamily.Default
                )

                CustomButton(
                    onClick = onClick,
                    backgroundColor = buttonColor,
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = action,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 8.dp)
                            )

                            Spacer(modifier = Modifier
                                .width(8.dp)
                                .weight(1f))

                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = action,
                                modifier = Modifier.size(32.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    },
                    modifier = Modifier
                        .width(140.dp)
                        .height(48.dp)
                )
            }
        }

    )
}

@Preview
@Composable
fun GameStoreScreenPreview() {
    GameStoreScreen(navController = rememberNavController())
}