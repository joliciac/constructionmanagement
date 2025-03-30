package com.example.constructionmanagement.data


data class WeatherResponse(
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>
)

data class DailyWeather(
    val dt: Long, // timestamp for the day
    val temp: Temperature,
    val weather: List<WeatherCondition>
)

data class Temperature(
    val day: Double,
    val night: Double
)

data class WeatherCondition(
    val description: String,
    val icon: String
)

data class HourlyWeather(
    val dt: Long,
    val temp: Double,
    val weather: List<WeatherCondition>
)