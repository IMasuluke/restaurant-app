package com.example.mrd.data

import com.example.mrd.domain.Restaurant

interface RestaurantSeedDataSource {
    suspend fun restaurants(): List<Restaurant>
}
