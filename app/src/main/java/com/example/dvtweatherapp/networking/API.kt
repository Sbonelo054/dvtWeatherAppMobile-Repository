package com.example.dvtweatherapp.networking

import com.example.dvtweatherapp.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("forecast")
    suspend fun getForecast(@Query("q") place: String?, @Query("units")units:String, @Query("appid") key: String): Weather

    @GET("weather")
    suspend fun getCurrentWeather(@Query("q") place: String?, @Query("appid") key: String): Weather
}