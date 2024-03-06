package com.example.dvtweatherapp.repository

import androidx.lifecycle.LiveData
import com.example.dvtweatherapp.database.FavouriteTable
import com.example.dvtweatherapp.database.OfflineForecastTable
import com.example.dvtweatherapp.database.OfflineTable

interface FavouriteRepository {

    suspend fun addFavourite(favouriteTable: FavouriteTable)

    fun getFavourites(): LiveData<List<FavouriteTable>>?

    suspend fun addOfflineWeather(offlineTable: OfflineTable)

    fun getOfflineWeather(): LiveData<OfflineTable>?

    suspend fun addOfflineForecast(offlineForecastTable: OfflineForecastTable)

    fun getOfflineForecast(): LiveData<List<OfflineForecastTable>>?

    suspend fun deleteOfflineForecast()

    suspend fun deleteOfflineWeather()
}