package com.example.mrd.ui.browse

data class RestaurantCardUi(
    val id: String,
    val name: String,
    val cuisines: String,
    val rating: String,
    val deliveryFee: String,
    val eta: String,
    val isOpen: Boolean,
    val isFavorite: Boolean,
    val imageSeed: Int
)
