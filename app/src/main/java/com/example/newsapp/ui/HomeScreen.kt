package com.example.newsapp.ui

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.newsapp.R
import com.example.newsapp.core.NoInternetConnection
import com.example.newsapp.core.status
import com.example.newsapp.network.ConnectivityObserver
import com.example.newsapp.network.NetworkConnectivityObserver

private lateinit var connectivityObserver: ConnectivityObserver
private var applicationContext: Context? = null

@Composable
fun HomeScreen(navController: NavHostController) {
    applicationContext = LocalContext.current.applicationContext
    connectivityObserver = NetworkConnectivityObserver(applicationContext ?: return)
    status = connectivityObserver.observe().collectAsState(
        initial = ConnectivityObserver.Status.Unavailable
    ).value
    when (status) {
        ConnectivityObserver.Status.Unavailable -> {
            NoInternetConnection()
        }

        ConnectivityObserver.Status.Available -> {
            Scaffold(
                topBar = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                    )
                }
            ) { padding ->
                Text(
                    text = "Home Screen",
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}