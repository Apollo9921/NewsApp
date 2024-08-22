package com.example.newsapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.newsapp.R

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = { Text(text = stringResource(id = R.string.app_name)) }
    ) {
        Text(
            text = "Home Screen",
            modifier = Modifier.padding(it)
        )
    }
}