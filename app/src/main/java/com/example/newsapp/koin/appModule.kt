package com.example.newsapp.koin

import com.example.newsapp.retrofit.NetworkModule
import com.example.newsapp.ui.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        NetworkModule().retrofitInterface()
    }
    single<NewsRepository> {
        NewsRepositoryImpl(get())
    }
    viewModel { HomeScreenViewModel(get()) }
}