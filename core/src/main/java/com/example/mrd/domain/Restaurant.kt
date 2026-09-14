package com.example.mrd.domain

data class Restaurant(
    val id: String,
    val name: String,
    val cuisines: List<String>,
    val rating: Double,
    val deliveryFeeCents: Int,
    val etaMinutes: Int,
    val isOpen: Boolean,
    val imageUrl: String?,
    val menu: List<MenuCategory>
)
