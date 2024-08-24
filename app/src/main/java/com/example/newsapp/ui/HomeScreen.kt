package com.example.newsapp.ui

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsapp.R
import com.example.newsapp.core.Black
import com.example.newsapp.core.Loading
import com.example.newsapp.core.NoInternetConnection
import com.example.newsapp.core.NoResults
import com.example.newsapp.core.White
import com.example.newsapp.core.mediaQueryWidth
import com.example.newsapp.core.normal
import com.example.newsapp.core.small
import com.example.newsapp.core.status
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import com.example.newsapp.navigation.Destination
import com.example.newsapp.network.ConnectivityObserver
import com.example.newsapp.network.NetworkConnectivityObserver
import org.koin.androidx.compose.koinViewModel

private lateinit var connectivityObserver: ConnectivityObserver
private var applicationContext: Context? = null
private lateinit var viewModel: HomeScreenViewModel

@Composable
fun HomeScreen(navController: NavHostController) {
    applicationContext = LocalContext.current.applicationContext
    connectivityObserver = NetworkConnectivityObserver(applicationContext ?: return)
    status = connectivityObserver.observe().collectAsState(
        initial = ConnectivityObserver.Status.Unavailable
    ).value
    viewModel = koinViewModel<HomeScreenViewModel>()
    val isConnected = rememberSaveable { mutableStateOf(false) }
    if (status == ConnectivityObserver.Status.Available && !isConnected.value) {
        viewModel.getNews()
        isConnected.value = true
    }
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
            topBar = { TopBar() }
        ) { padding ->
            GetNewsResponse(padding, navController)
        }
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black.copy(alpha = 0.5f))
            .padding(start = 20.dp, top = 60.dp, end = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TopBarContent()
    }
}

@Composable
private fun TopBarContent() {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val emptyField = stringResource(id = R.string.searchEmpty)
    val noInternetConnection = stringResource(id = R.string.noInternetConnection)
    var search by rememberSaveable { mutableStateOf("") }
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
    Spacer(modifier = Modifier.padding(10.dp))
    TextField(
        value = search,
        onValueChange = {
            search = it
        },
        maxLines = 1,
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search),
                color = Black,
                fontWeight = FontWeight.W400,
                fontSize =
                if (mediaQueryWidth() < small) {
                    12.sp
                } else if (mediaQueryWidth() < normal) {
                    17.sp
                } else {
                    22.sp
                }
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                when {
                    search.isEmpty() -> {
                        Toast.makeText(context, emptyField, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        if (status == ConnectivityObserver.Status.Unavailable) {
                            Toast.makeText(context, noInternetConnection, Toast.LENGTH_SHORT)
                                .show()
                            return@IconButton
                        }
                        viewModel.getNews(search)
                        search = ""
                        focusManager.clearFocus()
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Black
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (search.isNotEmpty()) {
                    if (status == ConnectivityObserver.Status.Unavailable) {
                        Toast.makeText(context, noInternetConnection, Toast.LENGTH_SHORT)
                            .show()
                        return@KeyboardActions
                    }
                    viewModel.getNews(search)
                    search = ""
                    focusManager.clearFocus()
                } else {
                    Toast.makeText(context, emptyField, Toast.LENGTH_SHORT).show()
                }
            },
        )
    )
}

@Composable
private fun GetNewsResponse(padding: PaddingValues, navController: NavHostController) {
    when {
        viewModel.isLoading.value -> {
            Loading()
        }

        viewModel.isError.value -> {
            val message = viewModel.errorMessage.value
            if (message == "No internet connection") {
                NoInternetConnection(padding)
            }
        }

        viewModel.isSuccessful.value -> {
            SaveDataAndDisplayNews(padding, navController)
        }
    }
}

@Composable
private fun SaveDataAndDisplayNews(padding: PaddingValues, navController: NavHostController) {
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
    NewsList(newsList, padding, navController)
}

@Composable
private fun NewsList(newsList: News?, padding: PaddingValues, navController: NavHostController) {
    val news = newsList?.articles?.sortedBy { it.publishedAt }
    if (news?.isEmpty() == true) {
        NoResults(padding)
        return
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
        horizontalAlignment = Alignment.Start
    ) {
        items(news?.size ?: 0) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clickable {
                        navController.navigate(
                            Destination.Detail(
                                news?.get(index)?.urlToImage ?: "",
                                news?.get(index)?.title ?: "",
                                news?.get(index)?.description ?: "",
                                news?.get(index)?.content ?: ""
                            )
                        )
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    if (news?.get(index)?.urlToImage != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(news[index].urlToImage)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            placeholder = painterResource(R.drawable.no_image),
                            error = painterResource(R.drawable.no_image),
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
                            text = news[index].source.name,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            fontSize =
                            if (mediaQueryWidth() < small) {
                                12.sp
                            } else if (mediaQueryWidth() < normal) {
                                17.sp
                            } else {
                                20.sp
                            }
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.no_image),
                            contentDescription = null,
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
                Spacer(modifier = Modifier.padding(10.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = news?.get(index)?.title ?: "",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W600,
                        fontSize =
                        if (mediaQueryWidth() < small) {
                            18.sp
                        } else if (mediaQueryWidth() < normal) {
                            23.sp
                        } else {
                            28.sp
                        }
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = news?.get(index)?.description ?: "",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W400,
                        fontSize =
                        if (mediaQueryWidth() < small) {
                            13.sp
                        } else if (mediaQueryWidth() < normal) {
                            18.sp
                        } else {
                            23.sp
                        }
                    )
                }
            }
            HorizontalDivider(
                color = Black,
                thickness = 1.dp
            )
        }
    }
}