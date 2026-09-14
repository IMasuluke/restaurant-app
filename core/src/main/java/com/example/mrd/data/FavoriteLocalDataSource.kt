package com.example.mrd.data

import kotlinx.coroutines.flow.StateFlow

interface FavoriteLocalDataSource {
    val favoriteIds: StateFlow<Set<String>>
    suspend fun toggle(id: String)
}
