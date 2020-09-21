package com.example.calculator

import android.app.Application
import com.example.calculator.di.calculatorUIModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            logger(PrintLogger(Level.ERROR))
            androidContext(this@App)
            modules(calculatorUIModule)
        }
    }
}