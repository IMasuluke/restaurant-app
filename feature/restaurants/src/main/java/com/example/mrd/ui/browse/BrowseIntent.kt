package com.example.mrd.ui.browse

sealed interface BrowseIntent {
    data class SearchChanged(val query: String) : BrowseIntent
    data class OpenOnlyChanged(val enabled: Boolean) : BrowseIntent
    data class PageSelected(val page: BrowsePage) : BrowseIntent
    data class FavoriteClicked(val restaurantId: String) : BrowseIntent
    data object RefreshRequested : BrowseIntent
    data object RetryClicked : BrowseIntent
}
