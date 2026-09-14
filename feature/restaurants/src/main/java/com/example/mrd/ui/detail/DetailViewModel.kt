package com.example.mrd.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mrd.domain.MenuCategory
import com.example.mrd.domain.MenuItem
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

class DetailViewModel(
    private val restaurantId: String,
    private val repository: RestaurantRepository
) : ViewModel() {
    private val loading = MutableStateFlow(true)
    private val restaurant = MutableStateFlow<Restaurant?>(null)
    private val error = MutableStateFlow<String?>(null)

    val state: StateFlow<DetailUiState> = combine(
        restaurant,
        repository.favoriteIds,
        loading,
        error
    ) { restaurant, favoriteIds, isLoading, errorMessage ->
        DetailUiState(
            isLoading = isLoading,
            restaurant = restaurant?.toDetailUi(isFavorite = restaurant.id in favoriteIds),
            errorMessage = errorMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailUiState()
    )

    init {
        load()
    }

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            DetailIntent.FavoriteClicked -> toggleFavorite()
            DetailIntent.RetryClicked -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            loading.value = true
            error.value = null

            val loadedRestaurant = runCatching { repository.restaurantById(restaurantId) }
                .onFailure { throwable ->
                    error.value = throwable.message ?: "Restaurant details could not be loaded."
                }
                .getOrNull()

            restaurant.value = loadedRestaurant
            if (loadedRestaurant == null && error.value == null) {
                error.value = "We could not find that restaurant."
            }

            loading.value = false
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            restaurant.value?.let { repository.toggleFavorite(it.id) }
        }
    }

    private fun Restaurant.toDetailUi(isFavorite: Boolean): RestaurantDetailUi {
        return RestaurantDetailUi(
            id = id,
            name = name,
            cuisines = cuisines.joinToString(" • "),
            rating = rating.asRating(),
            deliveryFee = deliveryFeeCents.asPrice(),
            eta = "$etaMinutes min",
            isOpen = isOpen,
            isFavorite = isFavorite,
            menu = menu.map { it.toUi() }
        )
    }

    private fun MenuCategory.toUi(): MenuCategoryUi {
        return MenuCategoryUi(
            id = id,
            name = name,
            items = items.map { it.toUi() }
        )
    }

    private fun MenuItem.toUi(): MenuItemUi {
        return MenuItemUi(
            id = id,
            name = name,
            price = priceCents.asPrice(),
            available = available,
            description = description,
            options = options.joinToString { "${it.name} +${it.priceCents.asPrice()}" }
        )
    }
}
