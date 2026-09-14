package com.example.mrd.domain

data class MenuCategory(
    val id: String,
    val name: String,
    val items: List<MenuItem>
)
