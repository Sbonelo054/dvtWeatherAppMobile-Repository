package com.example.dvtweatherapp

import android.app.Application
import com.example.dvtweatherapp.di.repoModule
import com.example.dvtweatherapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class DVTApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DVTApplication)
            modules(listOf(repoModule, viewModelModule))
        }
    }
}