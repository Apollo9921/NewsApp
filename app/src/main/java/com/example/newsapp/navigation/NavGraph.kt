package com.example.newsapp.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.newsapp.ui.DetailScreen
import com.example.newsapp.ui.HomeScreen

@Composable
fun NavGraph(navController: NavHostController, startDestination: Destination = Destination.Home) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Destination.Home> {
            HomeScreen(navController)
        }
        composable<Destination.Detail>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val news = backStackEntry.toRoute<Destination.Detail>()
            DetailScreen(navController, news.image, news.title, news.description, news.content)
        }
    }
}