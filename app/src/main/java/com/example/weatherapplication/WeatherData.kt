package com.example.weatherapplication

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class WeatherData(
                  val address: String,
                  val updatedAt: String,
                  val temperature: String,
                  val minTemperature: String,
                  val maxTemperature: String,
                  val pressure: String,
                  val humidity: String,
                  val sunrise: String,
                  val sunset: String,
                  val windSpeed: String,
                  val weatherDescription: String,
                  )
