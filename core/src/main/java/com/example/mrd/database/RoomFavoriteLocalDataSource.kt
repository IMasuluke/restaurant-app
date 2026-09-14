package com.example.mrd.database

import com.example.mrd.data.FavoriteLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RoomFavoriteLocalDataSource(
    private val favoriteRestaurantDao: FavoriteRestaurantDao
) : FavoriteLocalDataSource {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override val favoriteIds: StateFlow<Set<String>> = favoriteRestaurantDao.observeFavoriteIds()
        .map { ids -> ids.toSet() }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = emptySet()
        )

    override suspend fun toggle(id: String) {
        if (favoriteRestaurantDao.isFavorite(id)) {
            favoriteRestaurantDao.delete(FavoriteRestaurantEntity(restaurantId = id))
        } else {
            favoriteRestaurantDao.insert(FavoriteRestaurantEntity(restaurantId = id))
        }
    }
}
