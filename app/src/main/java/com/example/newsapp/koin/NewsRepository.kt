package com.example.newsapp.koin

import com.example.newsapp.model.News
import retrofit2.Response

interface NewsRepository {
    suspend fun getNews(apiKey: String, sources: String = "bbc-news"): Response<News>
}