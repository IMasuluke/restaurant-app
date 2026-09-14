package com.example.mrd.data

import com.example.mrd.domain.Restaurant
import com.example.mrd.domain.RestaurantRepository
import com.example.mrd.debug.IssueSimulationController
import com.example.mrd.debug.SimulatedApiException

class LocalRestaurantRepository(
    private val restaurantLocalDataSource: RestaurantLocalDataSource,
    private val favoriteLocalDataSource: FavoriteLocalDataSource,
    private val restaurantSeedDataSource: RestaurantSeedDataSource,
    private val issueSimulationController: IssueSimulationController
) : RestaurantRepository {
    override val favoriteIds = favoriteLocalDataSource.favoriteIds

    override suspend fun refresh() {
        issueSimulationController.consumeNextRefreshIssue()?.let { issue ->
            throw SimulatedApiException(issue)
        }

        if (!restaurantLocalDataSource.isEmpty()) return

        restaurantLocalDataSource.upsertRestaurants(
            restaurantSeedDataSource.restaurants()
        )
    }

    override fun observeRestaurants() = restaurantLocalDataSource.observeRestaurants()

    override suspend fun restaurantById(id: String): Restaurant? {
        refresh()
        return restaurantLocalDataSource.restaurantById(id)
    }

    override suspend fun toggleFavorite(id: String) {
        favoriteLocalDataSource.toggle(id)
    }
}
