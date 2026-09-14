package com.example.mrd.ui.detail

sealed interface DetailIntent {
    data object FavoriteClicked : DetailIntent
    data object RetryClicked : DetailIntent
}
