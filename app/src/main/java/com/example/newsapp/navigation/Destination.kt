package com.example.newsapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    data object Home: Destination()
    @Serializable
    data class Detail(
        val image: String,
        val title: String,
        val description: String,
        val content: String
    ): Destination()
}