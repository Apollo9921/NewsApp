package com.example.newsapp.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.R
import com.example.newsapp.network.ConnectivityObserver

val small = 600.dp
val normal = 840.dp

lateinit var status: ConnectivityObserver.Status

@Composable
fun mediaQueryWidth(): Dp {
    return LocalContext.current.resources.displayMetrics.widthPixels.dp / LocalDensity.current.density
}

@Composable
fun NoInternetConnection(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.calculateTopPadding()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_wifi),
                contentDescription = "No Wifi Connection",
                modifier = Modifier
                    .size(
                        if (mediaQueryWidth() < small) {
                            100.dp
                        } else if (mediaQueryWidth() < normal) {
                            200.dp
                        } else {
                            300.dp
                        }
                    )
            )
            Text(
                text = stringResource(id = R.string.noInternetConnection),
                color = Black,
                fontWeight = FontWeight.Bold,
                fontSize =
                if (mediaQueryWidth() < small) {
                    15.sp
                } else if (mediaQueryWidth() < normal) {
                    20.sp
                } else {
                    25.sp
                },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Black,
            modifier = Modifier
                .size(
                    if (mediaQueryWidth() < small) {
                        100.dp
                    } else if (mediaQueryWidth() < normal) {
                        200.dp
                    } else {
                        300.dp
                    }
                )
        )
    }
}

@Composable
fun NoResults(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.calculateTopPadding()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_image),
                contentDescription = "No Results",
                modifier = Modifier
                    .size(
                        if (mediaQueryWidth() < small) {
                            100.dp
                        } else if (mediaQueryWidth() < normal) {
                            200.dp
                        } else {
                            300.dp
                        }
                    )
            )
            Text(
                text = stringResource(id = R.string.noResults),
                color = Black,
                fontWeight = FontWeight.Bold,
                fontSize =
                if (mediaQueryWidth() < small) {
                    15.sp
                } else if (mediaQueryWidth() < normal) {
                    20.sp
                } else {
                    25.sp
                },
                textAlign = TextAlign.Center
            )
        }
    }
}