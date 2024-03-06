package com.example.dvtweatherapp.model.currentWeather

data class Sys(
    var type: Int?,
    var id: Int?,
    var country: String?,
    var sunrise: Int?,
    var sunset: Int?
) {
}