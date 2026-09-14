package com.example.mrd.data

import com.example.mrd.domain.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantLocalDataSource {
    fun observeRestaurants(): Flow<List<Restaurant>>
    suspend fun restaurantById(id: String): Restaurant?
    suspend fun isEmpty(): Boolean
    suspend fun upsertRestaurants(restaurants: List<Restaurant>)
}
