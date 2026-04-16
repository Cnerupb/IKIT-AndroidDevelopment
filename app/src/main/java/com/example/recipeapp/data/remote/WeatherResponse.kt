package com.example.recipeapp.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current") val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m") val temperature2m: Double,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: Int,
    @SerializedName("wind_speed_10m") val windSpeed10m: Double,
    @SerializedName("weather_code") val weatherCode: Int
)
