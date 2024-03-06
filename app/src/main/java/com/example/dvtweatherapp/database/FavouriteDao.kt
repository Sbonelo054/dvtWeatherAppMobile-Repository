package com.example.dvtweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteDao {

    @Insert
    suspend fun addFavourite(table: FavouriteTable)

    @Query("SELECT * FROM favourite")
    fun getFavourites(): LiveData<List<FavouriteTable>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflineWeather(offlineTable: OfflineTable)

    @Query("SELECT * FROM offline_weather")
    fun getOfflineWeather(): LiveData<OfflineTable>?

    @Query("SELECT * FROM offline_forecast")
    fun getOfflineForecast(): LiveData<List<OfflineForecastTable>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOfflineForecast(offlineForecastTable: OfflineForecastTable)

    @Query("DELETE FROM offline_forecast")
    fun deleteOfflineForecast()

    @Query("DELETE FROM offline_weather")
    fun deleteOfflineWeather()
}