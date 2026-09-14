package com.example.mrd.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_restaurants")
data class FavoriteRestaurantEntity(
    @PrimaryKey val restaurantId: String
)
