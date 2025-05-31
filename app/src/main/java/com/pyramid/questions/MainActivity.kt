package com.pyramid.questions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pyramid.questions.data.local.GameViewModel
import com.pyramid.questions.data.remote.AdMobManager
import com.pyramid.questions.data.remote.getAdMobManager
import com.pyramid.questions.navigation.AppNavGraph
import com.pyramid.questions.ui.theme.GuessTheWordsGameTheme


class MainActivity : ComponentActivity() {
    private lateinit var adMobManager: AdMobManager
    private val gameViewModel: GameViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adMobManager = this.getAdMobManager()
        adMobManager.preloadAllAds(object : AdMobManager.AdLoadListener {
            override fun onAdLoaded() {
                println("إعلان تم تحميله بنجاح")
            }
            override fun onAdFailedToLoad(error: String) {
                println("فشل في تحميل الإعلان: $error")
            }
        })
        setContent {
            GuessTheWordsGameTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color(0xFF0D47A1),
                        darkIcons = true
                    )
                    systemUiController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = true
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adMobManager.destroyAds()
    }
}

