package com.example.mrd.ui.browse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mrd.ui.components.EmptyState
import com.example.mrd.ui.components.ErrorState
import com.example.mrd.ui.components.LoadingView
import com.example.mrd.ui.components.RestaurantImage
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun BrowseRoute(
    onSimulatorClicked: () -> Unit,
    onRestaurantSelected: (String) -> Unit,
    viewModel: BrowseViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BrowseScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onSimulatorClicked = onSimulatorClicked,
        onRestaurantSelected = onRestaurantSelected
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BrowseScreen(
    state: BrowseUiState,
    onIntent: (BrowseIntent) -> Unit,
    onSimulatorClicked: () -> Unit,
    onRestaurantSelected: (String) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = state.selectedPage.ordinal,
        pageCount = { BrowsePage.entries.size }
    )
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                onIntent(BrowseIntent.PageSelected(BrowsePage.entries[page]))
            }
    }

    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            pullToRefreshState.animateToHidden()
        }
    }

    Scaffold(
        topBar = {
            BrowseHeader(
                state = state,
                onIntent = onIntent,
                onSimulatorClicked = onSimulatorClicked,
                onPageSelected = { page ->
                    scope.launch {
                        pagerState.animateScrollToPage(page.ordinal)
                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { onIntent(BrowseIntent.RefreshRequested) },
            state = pullToRefreshState,
            indicator = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                val page = BrowsePage.entries[pageIndex]
                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        state.isLoading -> LoadingView()
                        state.errorMessage != null -> ErrorState(
                            message = state.errorMessage,
                            onRetry = { onIntent(BrowseIntent.RetryClicked) }
                        )
                        state.isEmpty -> EmptyState(
                            title = emptyTitle(page),
                            message = emptyMessage(page)
                        )
                        else -> RestaurantList(
                            restaurants = state.restaurants,
                            onRestaurantSelected = onRestaurantSelected,
                            onFavoriteClicked = { restaurantId ->
                                onIntent(BrowseIntent.FavoriteClicked(restaurantId))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BrowseHeader(
    state: BrowseUiState,
    onIntent: (BrowseIntent) -> Unit,
    onSimulatorClicked: () -> Unit,
    onPageSelected: (BrowsePage) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Restaurants",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                modifier = Modifier.semantics {
                    contentDescription = "Open issue simulator"
                },
                onClick = onSimulatorClicked
            ) {
                Text(
                    text = "!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = headerSummary(state),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TabRow(
            modifier = Modifier.padding(top = 16.dp),
            selectedTabIndex = state.selectedPage.ordinal
        ) {
            BrowsePage.entries.forEach { page ->
                Tab(
                    selected = state.selectedPage == page,
                    onClick = { onPageSelected(page) },
                    text = {
                        Text(
                            text = if (page == BrowsePage.Saved) {
                                "${page.label} (${state.savedCount})"
                            } else {
                                page.label
                            }
                        )
                    }
                )
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = state.query,
            onValueChange = { onIntent(BrowseIntent.SearchChanged(it)) },
            singleLine = true,
            label = { Text("Search by restaurant or cuisine") }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Open now",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Switch(
                checked = state.openOnly,
                onCheckedChange = { onIntent(BrowseIntent.OpenOnlyChanged(it)) }
            )
        }
    }
}

private fun headerSummary(state: BrowseUiState): String {
    return when (state.selectedPage) {
        BrowsePage.All -> "${state.restaurants.size} shown from ${state.totalCount} nearby"
        BrowsePage.Saved -> "${state.restaurants.size} shown from ${state.savedCount} saved"
    }
}

private fun emptyTitle(page: BrowsePage): String {
    return when (page) {
        BrowsePage.All -> "No restaurants found"
        BrowsePage.Saved -> "No saved restaurants"
    }
}

private fun emptyMessage(page: BrowsePage): String {
    return when (page) {
        BrowsePage.All -> "Try a different search or include closed restaurants."
        BrowsePage.Saved -> "Save restaurants from the All tab to see them here."
    }
}

@Composable
private fun RestaurantList(
    restaurants: List<RestaurantCardUi>,
    onRestaurantSelected: (String) -> Unit,
    onFavoriteClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Spacer(modifier = Modifier.height(2.dp)) }
        items(
            items = restaurants,
            key = { it.id }
        ) { restaurant ->
            RestaurantCard(
                restaurant = restaurant,
                onClick = { onRestaurantSelected(restaurant.id) },
                onFavoriteClicked = { onFavoriteClicked(restaurant.id) }
            )
        }
        item { Spacer(modifier = Modifier.height(18.dp)) }
    }
}

@Composable
private fun RestaurantCard(
    restaurant: RestaurantCardUi,
    onClick: () -> Unit,
    onFavoriteClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RestaurantImage(
                name = restaurant.name,
                seed = restaurant.imageSeed
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = restaurant.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    TextButton(onClick = onFavoriteClicked) {
                        Text(if (restaurant.isFavorite) "Saved" else "Save")
                    }
                }
                Text(
                    text = restaurant.cuisines,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(top = 6.dp),
                    text = "★ ${restaurant.rating}  •  ${restaurant.eta}  •  ${restaurant.deliveryFee}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = if (restaurant.isOpen) "Open now" else "Closed",
                    color = if (restaurant.isOpen) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
