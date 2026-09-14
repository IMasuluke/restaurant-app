package com.example.mrd.database

import com.example.mrd.data.RestaurantLocalDataSource
import com.example.mrd.domain.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRestaurantLocalDataSource(
    private val restaurantDao: RestaurantDao
) : RestaurantLocalDataSource {
    override fun observeRestaurants(): Flow<List<Restaurant>> {
        return restaurantDao.observeRestaurants()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun restaurantById(id: String): Restaurant? {
        return restaurantDao.restaurantById(id)?.toDomain()
    }

    override suspend fun isEmpty(): Boolean {
        return restaurantDao.countRestaurants() == 0
    }

    override suspend fun upsertRestaurants(restaurants: List<Restaurant>) {
        restaurantDao.upsertRestaurants(
            restaurants.map { restaurant -> restaurant.toEntity() }
        )
    }
}
