package com.example.mrd.ui.browse

data class BrowseUiState(
    val isLoading: Boolean = true,
    val query: String = "",
    val openOnly: Boolean = false,
    val selectedPage: BrowsePage = BrowsePage.All,
    val restaurants: List<RestaurantCardUi> = emptyList(),
    val totalCount: Int = 0,
    val savedCount: Int = 0,
    val errorMessage: String? = null
) {
    val isEmpty: Boolean = !isLoading && errorMessage == null && restaurants.isEmpty()
}
