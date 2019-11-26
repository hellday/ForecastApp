package com.example.forecastmvvm.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.example.forecastmvvm.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(private val weatherApiService: WeatherApiService) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    // Fetch the Current Weather
    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather = weatherApiService
                .getCurrentWeather(location, languageCode).await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
            Log.e("Received Data", _downloadedCurrentWeather.toString())
        }catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.")
        }
    }
}