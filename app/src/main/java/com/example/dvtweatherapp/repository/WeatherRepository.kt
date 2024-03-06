package com.example.dvtweatherapp.repository

import com.example.dvtweatherapp.model.Weather
import com.example.dvtweatherapp.model.WeatherResults
import com.example.dvtweatherapp.model.currentWeather.CurrentWeatherModel

interface WeatherRepository {
    suspend fun getForecast(place : String?, unit : String) : WeatherResults<Weather>

    suspend fun getCurrentWeather(place: String?) : WeatherResults<CurrentWeatherModel>
}