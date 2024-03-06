package com.example.dvtweatherapp.model

data class Weather(
    var cod: String?,
    var message: Int?,
    var cnt: Int?,
    var list: List<WeatherList>?,
    var main: Main?,
    var description: String,
    var icon: String,
    var coord: Coord,
    var city: City?
)
