package com.example.mrd.ui.detail

data class RestaurantDetailUi(
    val id: String,
    val name: String,
    val cuisines: String,
    val rating: String,
    val deliveryFee: String,
    val eta: String,
    val isOpen: Boolean,
    val isFavorite: Boolean,
    val menu: List<MenuCategoryUi>
)
