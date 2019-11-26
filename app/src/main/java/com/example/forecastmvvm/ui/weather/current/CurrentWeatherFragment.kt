package com.example.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.lifecycle.Observer

import com.example.forecastmvvm.R
import com.example.forecastmvvm.internal.glide.GlideApp
import com.example.forecastmvvm.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        // Binding the UI to the ViewModel
        bindUI()
    }

    private fun bindUI() = launch{
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        // Current Weather Observer
        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if(it == null) return@Observer
            // Hide the Loading Data Group
            group_loading.visibility = View.GONE

            Log.d("API Callback", it.toString())

            updateDateToTodayAndTime()
            updateTemperature(it.temperature, it.feelslike)
            updateCondition(it.weatherDescriptions)
            updatePrecipitation(it.precip)
            updateWind(it.windDir, it.windSpeed)
            updateVisibility(it.visibility)

            GlideApp.with(this@CurrentWeatherFragment)
                .load(it.weatherIcons[0])
                .into(imageView_condition_icon)
        })

        // Location Weather Observer
        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if(location == null) return@Observer

            updateLocation(location.name)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String) : String {
        return if(viewModel.isMetric) metric else imperial
    }

    private fun chooseLanguage(english: String, french: String) : String {
        return if(viewModel.isEnglish) english else french
    }

    // Update WeatherLocation on the Action bar title of the Activity
    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    // Update Date on the Action bar subtitle of the Activity
    private fun updateDateToTodayAndTime() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperature(temperature: Double, feelsLike: Double){
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private fun updateCondition(condition: List<String>){
        textView_condition.text = condition[0]
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = buildSpannedString {
            bold { append("Precipitation: ") }
            append("$precipitationVolume $unitAbbreviation")
        }
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text = buildSpannedString {
            bold { append("Wind: ") }
            append("$windDirection, $windSpeed $unitAbbreviation")
        }
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        textView_visibility.text = buildSpannedString {
            bold { append("Visibility: ") }
            append("$visibilityDistance $unitAbbreviation")
        }
    }

}
