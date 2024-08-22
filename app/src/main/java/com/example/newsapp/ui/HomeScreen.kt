package com.example.newsapp.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.newsapp.R
import com.example.newsapp.core.Black
import com.example.newsapp.core.Loading
import com.example.newsapp.core.NoInternetConnection
import com.example.newsapp.core.White
import com.example.newsapp.core.mediaQueryWidth
import com.example.newsapp.core.normal
import com.example.newsapp.core.small
import com.example.newsapp.core.status
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import com.example.newsapp.network.ConnectivityObserver
import com.example.newsapp.network.NetworkConnectivityObserver
import org.koin.androidx.compose.koinViewModel

private lateinit var connectivityObserver: ConnectivityObserver
private var applicationContext: Context? = null
private lateinit var viewModel: HomeScreenViewModel

@Composable
fun HomeScreen(navController: NavHostController) {
    val configuration = LocalConfiguration.current
    applicationContext = LocalContext.current.applicationContext
    connectivityObserver = NetworkConnectivityObserver(applicationContext ?: return)
    status = connectivityObserver.observe().collectAsState(
        initial = ConnectivityObserver.Status.Unavailable
    ).value
    viewModel = koinViewModel<HomeScreenViewModel>()
    if (status == ConnectivityObserver.Status.Available && !viewModel.isSuccessful.value) {
        viewModel.getNews()
    }
    Scaffold(
        topBar = { TopBar(configuration) }
    ) { padding ->
        GetNewsResponse(padding)
    }
}

@Composable
private fun TopBar(configuration: Configuration) {
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Black.copy(alpha = 0.5f))
                    .padding(start = 60.dp, top = 60.dp, end = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopBarContent()
            }
        }

        Configuration.ORIENTATION_PORTRAIT -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Black.copy(alpha = 0.5f))
                    .padding(start = 20.dp, top = 60.dp, end = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopBarContent()
            }
        }

        else -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Black.copy(alpha = 0.5f))
                    .padding(start = 20.dp, top = 60.dp, end = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopBarContent()
            }
        }
    }
}

@Composable
private fun TopBarContent() {
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

@Composable
private fun GetNewsResponse(padding: PaddingValues) {
    when {
        viewModel.isLoading.value -> {
            Loading()
        }

        viewModel.isError.value -> {
            val message = viewModel.errorMessage.value
            if (message == "No internet connection") {
                NoInternetConnection()
            }
        }

        viewModel.isSuccessful.value -> {
            SaveDataAndDisplayNews(padding)
        }
    }
}

@Composable
private fun SaveDataAndDisplayNews(padding: PaddingValues) {
    val myDataSaver = Saver<News, Map<String, Any?>>(
        save = { myData ->
            mapOf(
                "articles" to myData.articles,
                "status" to myData.status,
                "totalResults" to myData.totalResults
            )
        },
        restore = { savedState ->
            News(
                articles = savedState.getOrElse("articles") { emptyList<Article>() } as List<Article>,
                status = savedState["status"] as String,
                totalResults = savedState["totalResults"] as Int
            )
        }
    )
    var newsList by rememberSaveable(stateSaver = myDataSaver)
    {
        mutableStateOf(News(emptyList(), "", 0))
    }
    newsList = viewModel.news.value ?: News(emptyList(), "", 0)
    NewsList(newsList, padding)
}

@Composable
private fun NewsList(newsList: News?, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.calculateTopPadding()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(newsList?.articles?.size ?: 0) { index ->
            Text(text = newsList?.articles?.get(index)?.title ?: "")
            Text(text = newsList?.articles?.get(index)?.description ?: "")
            HorizontalDivider(
                color = Black,
                thickness = 1.dp
            )
        }
    }
}