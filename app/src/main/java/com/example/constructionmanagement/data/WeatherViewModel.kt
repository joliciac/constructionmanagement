package com.example.constructionmanagement.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weather = mutableStateOf<Weather?>(null)
    val weather: State<Weather?> = _weather

    private val apiKey = "Hy8QwInA4sdVTCziwW2jSCpcInCRW4Sz" // Replace with your Tomorrow.io API key

    fun fetchWeather(location: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeather(
                    location = location,
                    apiKey = apiKey
                )
                _weather.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}