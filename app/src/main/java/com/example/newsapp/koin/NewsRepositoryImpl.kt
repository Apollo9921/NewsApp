package com.example.newsapp.koin

import com.example.newsapp.model.News
import com.example.newsapp.retrofit.RetrofitInterface
import retrofit2.Call

class NewsRepositoryImpl(
    private val retrofitInterface: RetrofitInterface
): NewsRepository  {
    override suspend fun getNews(apiKey: String): Call<News> {
        return retrofitInterface.getNews(apiKey)
    }
}