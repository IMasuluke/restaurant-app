package com.example.mrd.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mrd.ui.components.ErrorState
import com.example.mrd.ui.components.LoadingView
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailRoute(
    restaurantId: String,
    onBackClicked: () -> Unit,
    viewModel: DetailViewModel = koinViewModel(
        key = "detail-$restaurantId",
        parameters = { parametersOf(restaurantId) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailScreen(
        state = state,
        onBackClicked = onBackClicked,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun DetailScreen(
    state: DetailUiState,
    onBackClicked: () -> Unit,
    onIntent: (DetailIntent) -> Unit
) {
    Scaffold(
        topBar = {
            DetailHeader(
                restaurant = state.restaurant,
                onBackClicked = onBackClicked,
                onFavoriteClicked = { onIntent(DetailIntent.FavoriteClicked) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> LoadingView()
                state.errorMessage != null -> ErrorState(
                    message = state.errorMessage,
                    onRetry = { onIntent(DetailIntent.RetryClicked) }
                )
                state.restaurant != null -> RestaurantDetail(restaurant = state.restaurant)
            }
        }
    }
}

@Composable
private fun DetailHeader(
    restaurant: RestaurantDetailUi?,
    onBackClicked: () -> Unit,
    onFavoriteClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBackClicked) {
                Text("Back")
            }
            Spacer(modifier = Modifier.weight(1f))
            if (restaurant != null) {
                TextButton(onClick = onFavoriteClicked) {
                    Text(if (restaurant.isFavorite) "Saved" else "Save")
                }
            }
        }
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = restaurant?.name ?: "Restaurant",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        restaurant?.let {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = it.cuisines,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "★ ${it.rating}  •  ${it.eta}  •  ${it.deliveryFee}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = if (it.isOpen) "Open now" else "Closed",
                color = if (it.isOpen) {
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

@Composable
private fun RestaurantDetail(restaurant: RestaurantDetailUi) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "Menu",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        restaurant.menu.forEach { category ->
            item(key = category.id) {
                MenuCategoryHeader(category = category)
            }
            items(
                items = category.items,
                key = { it.id }
            ) { item ->
                MenuItemCard(item = item)
            }
        }
        item { Spacer(modifier = Modifier.height(18.dp)) }
    }
}

@Composable
private fun MenuCategoryHeader(category: MenuCategoryUi) {
    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = category.name,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun MenuItemCard(item: MenuItemUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = item.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = item.price,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = if (item.available) "Available" else "Unavailable",
                color = if (item.available) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = item.options,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
