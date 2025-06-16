package com.example.constructionmanagement.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.constructionmanagement.data.RetrofitInstance
import com.example.constructionmanagement.data.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weather = mutableStateOf<WeatherResponse?>(null)
    val weather: State<WeatherResponse?> = _weather

    private val apiKey = ""

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                Log.d("WeatherAPI", "Requesting URL: https://api.openweathermap.org/data/3.0/onecall?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric")

                val response = RetrofitInstance.api.getWeather(
                    latitude = latitude,
                    longitude = longitude,
                    apiKey = apiKey,
                    units = "metric",
                    exclude = "current,minute,alerts"
                )
                _weather.value = response
                Log.d("WeatherAPI", "Hourly data size: ${response.hourly?.size}")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

