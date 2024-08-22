package com.example.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    }
}