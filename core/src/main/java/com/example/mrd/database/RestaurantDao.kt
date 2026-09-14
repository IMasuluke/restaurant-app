package com.example.mrd.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Query("SELECT COUNT(*) FROM restaurants")
    suspend fun countRestaurants(): Int

    @Query("SELECT * FROM restaurants ORDER BY name ASC")
    fun observeRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE id = :id LIMIT 1")
    suspend fun restaurantById(id: String): RestaurantEntity?

    @Upsert
    suspend fun upsertRestaurants(restaurants: List<RestaurantEntity>)
}
