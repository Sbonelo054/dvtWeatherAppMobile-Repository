package com.example.dvtweatherapp.di

import com.example.dvtweatherapp.repository.WeatherRepository
import com.example.dvtweatherapp.repository.WeatherRepositoryImpl
import org.koin.dsl.module

val repoModule = module {
    single<WeatherRepository> {
        WeatherRepositoryImpl()
    }
}
