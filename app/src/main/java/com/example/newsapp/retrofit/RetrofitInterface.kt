package com.example.newsapp.retrofit

import com.example.newsapp.model.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("top-headlines?sources=bbc-news")
    suspend fun getNews(@Query("apiKey") apiKey: String): Response<News>
}