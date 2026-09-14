package com.example.mrd.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.mrd.ui.browse.BrowseRoute
import com.example.mrd.ui.detail.DetailRoute
import com.example.mrd.ui.simulation.SimulationRoute
import com.example.mrd.ui.theme.AppTheme

@Composable
fun RestaurantApp(initialRestaurantId: String?) {
    var selectedRestaurantId by rememberSaveable { mutableStateOf(initialRestaurantId) }
    var showSimulation by rememberSaveable { mutableStateOf(false) }

    AppTheme {
        if (showSimulation) {
            BackHandler {
                showSimulation = false
            }
            SimulationRoute(
                onBackClicked = {
                    showSimulation = false
                }
            )
        } else if (selectedRestaurantId == null) {
            BrowseRoute(
                onSimulatorClicked = {
                    showSimulation = true
                },
                onRestaurantSelected = { restaurantId ->
                    selectedRestaurantId = restaurantId
                }
            )
        } else {
            BackHandler {
                selectedRestaurantId = null
            }
            DetailRoute(
                restaurantId = selectedRestaurantId.orEmpty(),
                onBackClicked = {
                    selectedRestaurantId = null
                }
            )
        }
    }
}
