package com.example.newsapp.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.BuildConfig
import com.example.newsapp.core.status
import com.example.newsapp.koin.NewsRepository
import com.example.newsapp.model.News
import com.example.newsapp.network.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _newsState = MutableStateFlow<NewsState>(NewsState.Loading)
    private val newsState: StateFlow<NewsState> = _newsState

    var isLoading = mutableStateOf(false)
    var isError = mutableStateOf(false)
    var errorMessage = mutableStateOf("")
    var isSuccessful = mutableStateOf(false)
    var news = mutableStateOf<News?>(null)

    sealed class NewsState {
        data object Loading : NewsState()
        data class Success(val news: News?) : NewsState()
        data class Error(val message: String) : NewsState()
    }

    init {
        getNews()
    }

     fun getNews(source: String = "bbc-news") {
        viewModelScope.launch {
            if (status == ConnectivityObserver.Status.Unavailable) {
                _newsState.value = NewsState.Error("No internet connection")
                return@launch
            }
            _newsState.value = NewsState.Loading
            try {
                val sourceNews = if (source.isNotEmpty()) source.replace(" ", "-") else source
                val response = newsRepository.getNews(BuildConfig.API_KEY, sourceNews)
                if (response.isSuccessful) {
                    _newsState.value = NewsState.Success(response.body())
                } else {
                    _newsState.value = NewsState.Error(response.message())
                }
            } catch (e: Exception) {
                _newsState.value = NewsState.Error(e.message ?: "Unknown error")
            }
        }
        getNewsResponse()
    }

    private fun getNewsResponse() {
        viewModelScope.launch {
            newsState.collect {
                when(it) {
                    is NewsState.Error -> {
                        isLoading.value = false
                        isError.value = true
                        errorMessage.value = it.message
                        isSuccessful.value = false
                    }
                    NewsState.Loading -> {
                        isLoading.value = true
                        isError.value = false
                        isSuccessful.value = false
                        errorMessage.value = ""
                    }
                    is NewsState.Success -> {
                        isLoading.value = false
                        isError.value = false
                        isSuccessful.value = true
                        news.value = it.news
                    }
                }
            }
        }
    }

}