package com.example.mrd

import android.app.Application
import com.example.mrd.di.coreModule
import com.example.mrd.di.restaurantsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MrDApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MrDApplication)
            modules(coreModule, restaurantsModule)
        }
    }
}
