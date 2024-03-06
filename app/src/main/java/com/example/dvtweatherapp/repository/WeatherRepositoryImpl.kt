package com.example.dvtweatherapp.repository

import com.example.dvtweatherapp.model.Weather
import com.example.dvtweatherapp.model.WeatherResults
import com.example.dvtweatherapp.model.currentWeather.CurrentWeatherModel
import com.example.dvtweatherapp.networking.API
import com.example.dvtweatherapp.networking.Client
import com.example.dvtweatherapp.utils.Constants

class WeatherRepositoryImpl: WeatherRepository {
    private lateinit var api: API

    override suspend fun getForecast(place : String?, unit : String) : WeatherResults<Weather> {
        api = Client.instance.create(API::class.java)
        return try {
            val results = api.getForecast(place,unit, Constants.APP_ID)
            WeatherResults.SuccessResults(results)
        } catch (t : Throwable) {
            WeatherResults.Error(t)
        }
    }

    override suspend fun getCurrentWeather(place: String?): WeatherResults<CurrentWeatherModel> {
        api = Client.instance.create(API::class.java)
        return try {
            val results = api.getCurrentWeather(place, Constants.APP_ID)
            WeatherResults.SuccessResults(results)
        } catch (t : Throwable) {
            WeatherResults.Error(t)
        }
    }
}