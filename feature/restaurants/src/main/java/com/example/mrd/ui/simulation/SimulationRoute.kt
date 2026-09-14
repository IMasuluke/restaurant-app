package com.example.mrd.ui.simulation

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun SimulationRoute(
    onBackClicked: () -> Unit,
    viewModel: SimulationViewModel = koinViewModel()
) {
    var selectedScenario by rememberSaveable {
        mutableStateOf(SimulationScenario.ApiTimeout)
    }
    val armedIssue by viewModel.armedIssue.collectAsState()

    Scaffold(
        topBar = {
            SimulationHeader(onBackClicked = onBackClicked)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ScenarioPicker(
                selectedScenario = selectedScenario,
                onScenarioSelected = { scenario ->
                    selectedScenario = scenario
                }
            )
            ScenarioPreview(
                modifier = Modifier.weight(1f),
                scenario = selectedScenario,
                armed = selectedScenario.apiIssue == armedIssue,
                onArmClicked = {
                    viewModel.armNextRefresh(selectedScenario.apiIssue)
                },
                onClearClicked = viewModel::clear
            )
        }
    }
}

@Composable
private fun SimulationHeader(
    onBackClicked: () -> Unit
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
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Issue simulator",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Arm an API issue, go back, then pull to refresh to exercise the real error path once.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ScenarioPicker(
    selectedScenario: SimulationScenario,
    onScenarioSelected: (SimulationScenario) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Spacer(modifier = Modifier.height(2.dp)) }
        items(
            items = SimulationScenario.entries,
            key = { it.name }
        ) { scenario ->
            ScenarioCard(
                scenario = scenario,
                selected = scenario == selectedScenario,
                onClick = { onScenarioSelected(scenario) }
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun ScenarioCard(
    scenario: SimulationScenario,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 2.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = scenario.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = scenario.message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ScenarioPreview(
    scenario: SimulationScenario,
    armed: Boolean,
    onArmClicked: () -> Unit,
    onClearClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = if (armed) {
                    "Armed for next refresh"
                } else {
                    "Choose an issue to arm"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (armed) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontWeight = FontWeight.SemiBold
            )
            TextButton(onClick = onClearClicked) {
                Text("Clear")
            }
            Button(onClick = onArmClicked) {
                Text("Arm")
            }
        }

        Text(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            text = "After arming, return to the restaurants screen and pull to refresh. The selected issue is consumed once and then cleared.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
