package com.example.weatherapplication

import android.util.Log
import com.example.weatherapplication.Retrofit.RetrofitClient


object WeatherRepository {
    suspend fun fetchWeatherData(lat: Double, lon: Double, apiKey: String,cityName:String?): WeatherData? {
        return try {
                val response = if (!cityName.isNullOrEmpty()) {
                    RetrofitClient.instance.getWeatherByCity(cityName, apiKey = apiKey)
                }else{
                    RetrofitClient.instance.getWeatherByLocation(lat,lon, apiKey = apiKey)

                }
            Log.d("WeatherRepository", "API Response: $response")

            val weatherData = response.toData()
            Log.d("WeatherRepository", "after mapping $weatherData")

            return weatherData
        } catch (e: Exception) {
            Log.e("WeatherApp", "Error fetching weather data: ${e.message}", e)
            null

        }
    }
}
