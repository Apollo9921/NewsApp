package com.example.newsapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    data object Home: Destination()
}