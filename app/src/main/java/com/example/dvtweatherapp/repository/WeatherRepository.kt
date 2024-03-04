package com.example.dvtweatherapp.repository

import com.example.dvtweatherapp.model.Weather
import com.example.dvtweatherapp.model.WeatherResults

interface WeatherRepository {
    suspend fun getForecast(place : String, unit : String) : WeatherResults<Weather>

    suspend fun getCurrentWeather(place: String) : WeatherResults<Weather>
}