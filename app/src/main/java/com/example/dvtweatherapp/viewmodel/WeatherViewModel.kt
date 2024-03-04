package com.example.dvtweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dvtweatherapp.model.Weather
import com.example.dvtweatherapp.model.WeatherResults
import com.example.dvtweatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel (private val weatherRepository: WeatherRepository): ViewModel() {

    fun getForecast(place:String, unit : String) : MutableLiveData<WeatherResults<Weather>> {
        val weatherData: MutableLiveData<WeatherResults<Weather>> = MutableLiveData()
        viewModelScope.launch {
            val results = weatherRepository.getForecast(place, unit="metric")
            weatherData.value = results
        }
        return weatherData
    }

    fun getCurrentWeather(place:String) : MutableLiveData<WeatherResults<Weather>> {
        val weatherData: MutableLiveData<WeatherResults<Weather>> = MutableLiveData()
        viewModelScope.launch {
            val results = weatherRepository.getCurrentWeather(place)
            weatherData.value = results
        }
        return weatherData
    }
}