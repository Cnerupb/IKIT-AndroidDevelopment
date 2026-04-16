package com.example.recipeapp.data

data class WeatherData(
    val city: String,
    val temperature: Int,
    val condition: WeatherCondition,
    val humidity: Int,
    val windSpeed: Int
)

enum class WeatherCondition(val label: String) {
    CLEAR("Ясно"),
    CLOUDY("Облачно"),
    RAIN("Дождь")
}

val sampleWeather = WeatherData(
    city = "Минск",
    temperature = 18,
    condition = WeatherCondition.CLOUDY,
    humidity = 72,
    windSpeed = 14
)
