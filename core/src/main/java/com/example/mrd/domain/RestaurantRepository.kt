package com.example.mrd.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RestaurantRepository {
    val favoriteIds: StateFlow<Set<String>>

    suspend fun refresh()
    fun observeRestaurants(): Flow<List<Restaurant>>
    suspend fun restaurantById(id: String): Restaurant?
    suspend fun toggleFavorite(id: String)
}
