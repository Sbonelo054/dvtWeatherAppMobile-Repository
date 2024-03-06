package com.example.dvtweatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_forecast")
class OfflineForecastTable(var date: String?, var tempMax: String?, var tempMin: String?) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}