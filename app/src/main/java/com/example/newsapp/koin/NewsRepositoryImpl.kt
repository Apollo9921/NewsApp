package com.example.newsapp.koin

import com.example.newsapp.model.News
import com.example.newsapp.retrofit.RetrofitInterface
import retrofit2.Response

class NewsRepositoryImpl(
    private val retrofitInterface: RetrofitInterface
): NewsRepository  {
    override suspend fun getNews(apiKey: String, sources: String): Response<News> {
        return retrofitInterface.getNews(apiKey, sources)
    }
}