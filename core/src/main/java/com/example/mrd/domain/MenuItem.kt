package com.example.mrd.domain

data class MenuItem(
    val id: String,
    val name: String,
    val priceCents: Int,
    val available: Boolean,
    val description: String,
    val imageUrl: String?,
    val options: List<MenuOption>
)
