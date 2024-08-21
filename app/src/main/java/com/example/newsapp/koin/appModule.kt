package com.example.newsapp.koin

import com.example.newsapp.retrofit.NetworkModule
import org.koin.dsl.module

val appModule = module {
    single {
        NetworkModule().retrofitInterface()
    }
    single<NewsRepository> {
        NewsRepositoryImpl(get())
    }
}