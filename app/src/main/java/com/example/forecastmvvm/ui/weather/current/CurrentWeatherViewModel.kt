package com.example.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.forecastmvvm.data.provider.LanguageProvider
import com.example.forecastmvvm.data.provider.UnitProvider
import com.example.forecastmvvm.data.repository.ForecastRepository
import com.example.forecastmvvm.internal.LanguageSystem
import com.example.forecastmvvm.internal.UnitSystem
import com.example.forecastmvvm.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider, languageProvider: LanguageProvider) : ViewModel() {

    // Unit System
    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    // Language System
    private val languageSystem = languageProvider.getLanguageSystem()

    val isEnglish: Boolean
        get() = languageSystem == LanguageSystem.ENGLISH

    // Weather
    val weather by lazyDeferred {
        //forecastRepository.getCurrentWeather(isMetric)
        forecastRepository.getCurrentWeatherTest()
    }

    // Location
    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }

}
