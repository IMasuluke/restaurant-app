package com.example.mrd.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRestaurantDao {
    @Query("SELECT restaurantId FROM favorite_restaurants ORDER BY restaurantId ASC")
    fun observeFavoriteIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_restaurants WHERE restaurantId = :restaurantId)")
    suspend fun isFavorite(restaurantId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: FavoriteRestaurantEntity)

    @Delete
    suspend fun delete(favorite: FavoriteRestaurantEntity)
}
