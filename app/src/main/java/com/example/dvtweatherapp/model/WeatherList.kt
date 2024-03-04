package com.example.dvtweatherapp.model

data class WeatherList( var dt: Int?,
                        var main: Main?,
                        var weather: List<WeatherProperties>?,
                        var clouds: Clouds?,
                        var wind: Wind?,
                        var visibility: Int?,
                        var pop: Double?,
                        var sys: Sys?,
                        var dt_txt: String?)
