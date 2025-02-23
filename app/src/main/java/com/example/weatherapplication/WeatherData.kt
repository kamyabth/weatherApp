package com.example.weatherapplication



data class WeatherData(
                  val address: String,
                  val temperature: String,
                  val updatedAt: String,
                  val minTemperature: String,
                  val maxTemperature: String,
                  val pressure: String,
                  val humidity: String,
                  val sunrise: String,
                  val sunset: String,
                  val windSpeed: String,
                  val weatherDescription: String,
                  )
