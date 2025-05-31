package com.pyramid.questions.navigation

import android.net.Uri
import com.pyramid.questions.presentation.wheel.SpinWheelScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.pyramid.questions.R
import com.pyramid.questions.core.Constants
import com.pyramid.questions.domain.model.Question
import com.pyramid.questions.presentation.game.WordGuessingGameScreen
import com.pyramid.questions.presentation.home.HomeScreen
import com.pyramid.questions.presentation.leaderboard.LeaderboardScreen
import com.pyramid.questions.presentation.levels.PuzzleGameScreen
import com.pyramid.questions.presentation.splash.SplashScreen
import com.pyramid.questions.presentation.store.GameStoreScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route.SPLASH
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Route.SPLASH) {
            SplashScreen(
                navController = navController,
                onSplashFinished = {
                    navController.navigate(Route.HOME) {
                        popUpTo(Route.SPLASH) {
                            inclusive = true
                        }
                    }
                }
            ) {
                navController.navigate(Route.HOME) {
                    popUpTo(Route.SPLASH) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Route.HOME) {
            HomeScreen(
                navController = navController,
                onStartGame = { levelId, category ->
                    navController.navigate(Route.Puzzle.createRoute(levelId, category.name))
                },
                onOpenStore = {
                  navController.navigate(Route.STORE)
                },

            )
        }

        composable(Route.Puzzle.ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString("levelId")?.toIntOrNull() ?: 1
            val categoryName = backStackEntry.arguments?.getString("category") ?: Constants.WordCategory.ANIMALS.name
            try {
                Constants.WordCategory.valueOf(categoryName)
            } catch (e: Exception) {
                Constants.WordCategory.ANIMALS
            }
            PuzzleGameScreen(
                navController = navController,
                onOpenStore = {
                    navController.navigate(Route.STORE)
                },
                onOpenProfile = {
                    navController.navigate(Route.PROFILE)
                },
                onImageClick = { question ->
                    val questionJson = Uri.encode(Gson().toJson(question))
                    navController.navigate(Route.WordGuessing.createRoute(questionJson))
                }
            )
        }

        composable(Route.WordGuessing.ROUTE,
            arguments = listOf(
                navArgument("question") { type = NavType.StringType }
            )) { backStackEntry ->
            val questionJson = backStackEntry.arguments?.getString("question")
            val question = Gson().fromJson(
                Uri.decode(questionJson),
                Question::class.java
            )
            WordGuessingGameScreen(
                navController = navController,
                question =question
            )
        }
        composable(Route.WHEEL) {
            SpinWheelScreen(
                onOpenStore = {},
                onOpenProfile = {},
                onImageClick = {},
                navController = navController
            )
        }
        composable(Route.PLAYERS) {
            LeaderboardScreen(
                navController = navController,
            )
        }
        composable(Route.STORE) {
            GameStoreScreen(navController = navController)
        }
    }
}

object Route {
    const val HOME = "home"
    const val SPLASH = "splash"
    const val STORE = "store"
    const val DAILY_CHALLENGE = "daily-challenge"
    const val PROFILE = "profile"
    const val WHEEL = "wheel"
    const val PLAYERS = "players"

    object Puzzle {
        const val ROUTE = "puzzle/{levelId}/{category}"

        fun createRoute(levelId: Int, category: String): String {
            return "puzzle/$levelId/$category"
        }
    }

    object WordGuessing {
        const val ROUTE = "word-guessing/{question}"

        fun createRoute(question: String): String {
            return "word-guessing/$question"
        }
    }
}