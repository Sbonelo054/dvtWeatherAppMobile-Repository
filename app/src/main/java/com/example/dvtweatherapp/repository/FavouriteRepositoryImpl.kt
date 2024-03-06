package com.example.dvtweatherapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dvtweatherapp.database.FavouriteDao
import com.example.dvtweatherapp.database.FavouriteDatabase
import com.example.dvtweatherapp.database.FavouriteTable
import com.example.dvtweatherapp.database.OfflineForecastTable
import com.example.dvtweatherapp.database.OfflineTable

class FavouriteRepositoryImpl(application: Application): FavouriteRepository {
    private lateinit var dao : FavouriteDao

    init {
        val database = FavouriteDatabase.getInstance(application)
        if (database != null){
            dao = database.favouriteDao()
        }
    }

    override suspend fun addFavourite(favouriteTable: FavouriteTable) {
        dao.addFavourite(favouriteTable)
    }

    override fun getFavourites(): LiveData<List<FavouriteTable>>? {
        return dao.getFavourites()
    }

    override suspend fun addOfflineWeather(offlineTable: OfflineTable) {
        dao.insertOfflineWeather(offlineTable)
    }

    override fun getOfflineWeather(): LiveData<OfflineTable>? {
        return dao.getOfflineWeather()
    }

    override fun getOfflineForecast(): LiveData<List<OfflineForecastTable>>? {
       return dao.getOfflineForecast()
    }

    override suspend fun addOfflineForecast(offlineForecastTable: OfflineForecastTable) {
        dao.addOfflineForecast(offlineForecastTable)
    }

    override suspend fun deleteOfflineWeather() {
        dao.deleteOfflineWeather()
    }

    override suspend fun deleteOfflineForecast() {
       dao.deleteOfflineForecast()
    }
}