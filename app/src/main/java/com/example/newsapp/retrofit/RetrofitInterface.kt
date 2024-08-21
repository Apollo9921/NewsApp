package com.example.newsapp.retrofit

import com.example.newsapp.model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("top-headlines")
    suspend fun getNews(@Query("apiKey") apiKey: String): Call<News>
}