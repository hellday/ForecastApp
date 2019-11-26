package com.example.forecastmvvm

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.example.forecastmvvm.data.db.ForecastDatabase
import com.example.forecastmvvm.data.network.*
import com.example.forecastmvvm.data.provider.*
import com.example.forecastmvvm.data.repository.ForecastRepository
import com.example.forecastmvvm.data.repository.ForecastRepositoryImpl
import com.example.forecastmvvm.ui.weather.current.CurrentWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication() : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        // Database
        bind() from singleton { ForecastDatabase(instance()) }
        // DAO
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        // Interceptor
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        // API Service
        bind() from singleton { WeatherApiService(instance()) }
        // DataSource
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        // Repository
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance()) }
        // Factory
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance(), instance()) }
        // Providers
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind<LanguageProvider>() with singleton { LanguageProviderImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        // Set Default Values for the Preferences like in the xml
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}