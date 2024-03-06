package com.example.dvtweatherapp.di

import com.example.dvtweatherapp.viewmodel.FavouriteViewModel
import com.example.dvtweatherapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        WeatherViewModel(get())
    }

    viewModel {
        FavouriteViewModel(get(), get())
    }
}