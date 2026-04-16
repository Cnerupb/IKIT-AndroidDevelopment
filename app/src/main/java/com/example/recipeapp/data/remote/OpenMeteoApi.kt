package com.example.recipeapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String,
        @Query("wind_speed_unit") windSpeedUnit: String = "kmh"
    ): WeatherResponse
}
