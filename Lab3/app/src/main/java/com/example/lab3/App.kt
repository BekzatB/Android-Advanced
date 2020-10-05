package com.example.lab3

import android.app.Application
import com.example.lab3.di.moviesDataModule
import com.example.lab3.di.moviesUIModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger(PrintLogger(Level.ERROR))
            androidContext(this@App)
            modules(moviesDataModule, moviesUIModule)
        }
    }
}