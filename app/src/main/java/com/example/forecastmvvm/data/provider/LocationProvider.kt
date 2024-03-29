package com.example.forecastmvvm.data.provider

import com.example.forecastmvvm.data.db.entity.WeatherLocation

// Interface for Location
interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean
    suspend fun getPreferredLocationString(): String
}