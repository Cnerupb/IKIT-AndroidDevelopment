package com.example.recipeapp.data

import com.example.recipeapp.data.remote.RetrofitInstance
import kotlin.math.roundToInt

class WeatherRepository {
    private val api = RetrofitInstance.api

    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        city: String
    ): WeatherData {
        val response = api.getForecast(
            latitude = latitude,
            longitude = longitude,
            current = "temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code"
        )
        return WeatherData(
            city = city,
            temperature = response.current.temperature2m.roundToInt(),
            condition = mapWeatherCode(response.current.weatherCode),
            humidity = response.current.relativeHumidity2m,
            windSpeed = response.current.windSpeed10m.roundToInt()
        )
    }

    private fun mapWeatherCode(code: Int): WeatherCondition = when (code) {
        0 -> WeatherCondition.CLEAR
        in 51..67, in 80..82, in 95..99 -> WeatherCondition.RAIN
        else -> WeatherCondition.CLOUDY
    }
}
