package com.example.newsapp.koin

import com.example.newsapp.model.News
import retrofit2.Call

interface NewsRepository {
    suspend fun getNews(apiKey: String): Call<News>
}