package com.example.newsapp.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.core.Black
import com.example.newsapp.core.White
import com.example.newsapp.core.mediaQueryWidth
import com.example.newsapp.core.normal
import com.example.newsapp.core.small

@Composable
fun DetailScreen(
    navController: NavHostController,
    image: String,
    title: String,
    description: String,
    content: String
) {
    Box(
        modifier = Modifier.padding(
            start = WindowInsets.navigationBars
                .asPaddingValues()
                .calculateStartPadding(LayoutDirection.Ltr),
            end = WindowInsets.navigationBars
                .asPaddingValues()
                .calculateEndPadding(LayoutDirection.Ltr)
        )
    ) {
        Scaffold(
            topBar = { TopBarDetail(navController) }
        ) { padding ->
            Content(image, title, description, content, padding)
        }
    }
}

@Composable
private fun TopBarDetail(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black.copy(alpha = 0.5f))
            .padding(start = 20.dp, top = 60.dp, end = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.back),
            colorFilter = ColorFilter.tint(White),
            contentDescription = null,
            modifier = Modifier
                .size(
                    if (mediaQueryWidth() < small) {
                        20.dp
                    } else if (mediaQueryWidth() < normal) {
                        30.dp
                    } else {
                        40.dp
                    }
                )
                .clickable {
                    navController.navigateUp()
                }
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            color = White,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
            fontSize =
            if (mediaQueryWidth() < small) {
                15.sp
            } else if (mediaQueryWidth() < normal) {
                20.sp
            } else {
                25.sp
            }
        )
    }
}

@Composable
private fun Content(
    image: String,
    title: String,
    description: String,
    content: String,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding(), start = 20.dp, end = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        if (image.isNotEmpty()) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(shape = MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.no_image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}