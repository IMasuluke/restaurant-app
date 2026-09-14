package com.example.mrd.ui.browse

import com.example.mrd.domain.Restaurant

data class BrowseFilters(
    val restaurants: List<Restaurant>,
    val favoriteIds: Set<String>,
    val searchText: String,
    val openOnlyEnabled: Boolean,
    val selectedPage: BrowsePage
)
