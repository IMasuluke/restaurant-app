package com.example.mrd.ui.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mrd.domain.Restaurant
import com.example.mrd.domain.RestaurantRepository
import com.example.mrd.utils.asPrice
import com.example.mrd.utils.asRating
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BrowseViewModel(
    private val repository: RestaurantRepository
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val openOnly = MutableStateFlow(false)
    private val selectedPage = MutableStateFlow(BrowsePage.All)
    private val loading = MutableStateFlow(true)
    private val error = MutableStateFlow<String?>(null)

    private val filters = combine(
        repository.observeRestaurants(),
        repository.favoriteIds,
        query,
        openOnly,
        selectedPage
    ) { restaurants, favoriteIds, searchText, openOnlyEnabled, selectedPage ->
        BrowseFilters(
            restaurants = restaurants,
            favoriteIds = favoriteIds,
            searchText = searchText,
            openOnlyEnabled = openOnlyEnabled,
            selectedPage = selectedPage
        )
    }

    val state: StateFlow<BrowseUiState> = combine(
        filters,
        loading,
        error
    ) { filters, isLoading, errorMessage ->
        val filtered = filters.restaurants
            .asSequence()
            .filter { restaurant ->
                val matchesSearch = filters.searchText.isBlank() ||
                    restaurant.name.contains(filters.searchText, ignoreCase = true) ||
                    restaurant.cuisines.any { it.contains(filters.searchText, ignoreCase = true) }
                val matchesOpen = !filters.openOnlyEnabled || restaurant.isOpen
                val matchesPage = filters.selectedPage == BrowsePage.All ||
                    restaurant.id in filters.favoriteIds
                matchesSearch && matchesOpen && matchesPage
            }
            .sortedWith(compareByDescending<Restaurant> { it.isOpen }.thenByDescending { it.rating })
            .map { restaurant -> restaurant.toCardUi(isFavorite = restaurant.id in filters.favoriteIds) }
            .toList()

        BrowseUiState(
            isLoading = isLoading,
            query = filters.searchText,
            openOnly = filters.openOnlyEnabled,
            selectedPage = filters.selectedPage,
            restaurants = filtered,
            totalCount = filters.restaurants.size,
            savedCount = filters.favoriteIds.size,
            errorMessage = errorMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BrowseUiState()
    )

    init {
        load()
    }

    fun onIntent(intent: BrowseIntent) {
        when (intent) {
            is BrowseIntent.SearchChanged -> query.value = intent.query
            is BrowseIntent.OpenOnlyChanged -> openOnly.value = intent.enabled
            is BrowseIntent.PageSelected -> selectedPage.value = intent.page
            is BrowseIntent.FavoriteClicked -> toggleFavorite(intent.restaurantId)
            BrowseIntent.RefreshRequested -> load()
            BrowseIntent.RetryClicked -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            loading.value = true
            error.value = null

            try {
                runCatching { repository.refresh() }
                    .onFailure { throwable ->
                        error.value = throwable.message ?: "Restaurants could not be loaded."
                    }
            } finally {
                loading.value = false
            }
        }
    }

    private fun toggleFavorite(restaurantId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(restaurantId)
        }
    }

    private fun Restaurant.toCardUi(isFavorite: Boolean): RestaurantCardUi {
        return RestaurantCardUi(
            id = id,
            name = name,
            cuisines = cuisines.joinToString(" • "),
            rating = rating.asRating(),
            deliveryFee = deliveryFeeCents.asPrice(),
            eta = "$etaMinutes min",
            isOpen = isOpen,
            isFavorite = isFavorite,
            imageSeed = id.hashCode()
        )
    }
}
