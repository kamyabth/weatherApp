package com.example.weatherapplication

import android.util.Log
import com.example.weatherapplication.WeatherParser.parseWeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object WeatherRepository {
    suspend fun fetchWeatherData(lat: Double, lon: Double, apiKey: String): WeatherData? {
        return try {
            val url =
                "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=$apiKey"
            val response = withContext(Dispatchers.IO) { URL(url).readText(Charsets.UTF_8) }
            parseWeatherData(response)
        } catch (e: Exception) {
            Log.e("WeatherApp", "Error fetching weather data: ${e.message}", e)
            null
        }
    }
}
