package com.example.dvtweatherapp.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dvtweatherapp.model.Weather
import com.example.dvtweatherapp.model.currentWeather.CurrentWeatherModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "offline_weather")
data class OfflineTable(
    var temp: String,
    var minTemp: String,
    var maxTemp: String,
    var description: String,
    var name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}