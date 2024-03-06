package com.example.dvtweatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dvtweatherapp.database.FavouriteTable
import com.example.dvtweatherapp.database.OfflineForecastTable
import com.example.dvtweatherapp.database.OfflineTable
import com.example.dvtweatherapp.repository.FavouriteRepository
import com.example.dvtweatherapp.repository.FavouriteRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application, private val repository: FavouriteRepository) :
    ViewModel() {

    fun addFavourite(favouriteTable: FavouriteTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavourite(favouriteTable)
        }
    }

    fun addOfflineWeather(offlineTable: OfflineTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOfflineWeather(offlineTable)
        }
    }

    fun getFavourites(): LiveData<List<FavouriteTable>>? {
        return repository.getFavourites()
    }

    fun getOfflineWeather(): LiveData<OfflineTable>? {
        return repository.getOfflineWeather()
    }

    fun getOfflineForecast(): LiveData<List<OfflineForecastTable>>? {
        return repository.getOfflineForecast()
    }

    fun addOfflineForecast(offlineForecastTable: OfflineForecastTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOfflineForecast(offlineForecastTable)
        }
    }
}