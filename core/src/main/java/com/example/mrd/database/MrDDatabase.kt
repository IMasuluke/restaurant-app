package com.example.mrd.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        RestaurantEntity::class,
        FavoriteRestaurantEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class MrDDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
    abstract fun favoriteRestaurantDao(): FavoriteRestaurantDao
}
