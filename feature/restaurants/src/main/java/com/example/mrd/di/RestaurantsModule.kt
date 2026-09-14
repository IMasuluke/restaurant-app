package com.example.mrd.di

import com.example.mrd.ui.browse.BrowseViewModel
import com.example.mrd.ui.detail.DetailViewModel
import com.example.mrd.ui.simulation.SimulationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val restaurantsModule = module {
    viewModel {
        BrowseViewModel(repository = get())
    }

    viewModel { parameters ->
        DetailViewModel(
            restaurantId = parameters.get(),
            repository = get()
        )
    }

    viewModel {
        SimulationViewModel(issueSimulationController = get())
    }
}
