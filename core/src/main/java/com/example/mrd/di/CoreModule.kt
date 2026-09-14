package com.example.mrd.di

import androidx.room.Room
import com.example.mrd.data.AssetRestaurantSeedDataSource
import com.example.mrd.data.FavoriteLocalDataSource
import com.example.mrd.data.LocalRestaurantRepository
import com.example.mrd.data.RestaurantLocalDataSource
import com.example.mrd.data.RestaurantSeedDataSource
import com.example.mrd.database.MrDDatabase
import com.example.mrd.database.RoomFavoriteLocalDataSource
import com.example.mrd.database.RoomRestaurantLocalDataSource
import com.example.mrd.debug.IssueSimulationController
import com.example.mrd.domain.RestaurantRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single {
        IssueSimulationController()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            MrDDatabase::class.java,
            "mrd.db"
        ).build()
    }

    single {
        get<MrDDatabase>().restaurantDao()
    }

    single {
        get<MrDDatabase>().favoriteRestaurantDao()
    }

    single<RestaurantLocalDataSource> {
        RoomRestaurantLocalDataSource(restaurantDao = get())
    }

    single<FavoriteLocalDataSource> {
        RoomFavoriteLocalDataSource(favoriteRestaurantDao = get())
    }

    single<RestaurantSeedDataSource> {
        AssetRestaurantSeedDataSource(context = androidContext())
    }

    single<RestaurantRepository> {
        LocalRestaurantRepository(
            restaurantLocalDataSource = get(),
            favoriteLocalDataSource = get(),
            restaurantSeedDataSource = get(),
            issueSimulationController = get()
        )
    }
}
