package com.example.dvtweatherapp.model.currentWeather

import com.example.dvtweatherapp.model.Clouds
import com.example.dvtweatherapp.model.Coord
import com.example.dvtweatherapp.model.Main

data class CurrentWeatherModel(
    var coord: Coord,
    var weather: List<Weather>,
    var base: String,
    var main: Main?,
    var rain: Rain?,
    var clouds: Clouds,
    var dt: Int?,
    var sys: Sys?,
    var timeZone: Int?,
    var id: Int?,
    var name: String?
) {
}





