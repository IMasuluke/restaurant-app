package com.example.mrd.ui.detail

data class DetailUiState(
    val isLoading: Boolean = true,
    val restaurant: RestaurantDetailUi? = null,
    val errorMessage: String? = null
)
