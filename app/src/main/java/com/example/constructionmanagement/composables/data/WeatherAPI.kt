package com.example.constructionmanagement.composables.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("v4/timelines")
    suspend fun getWeather(
        @Query("location") location: String,
        @Query("fields") fields: String = "temperature,weatherCode",
        @Query("units") units: String = "metric",
        @Query("apiKey") apiKey: String,
        ): Weather
}