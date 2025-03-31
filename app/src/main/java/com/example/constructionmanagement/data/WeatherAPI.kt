package com.example.constructionmanagement.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "current,minutely,alerts",
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String,
    ): WeatherResponse
}

