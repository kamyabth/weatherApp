package com.example.weatherapplication.Retrofit

import com.example.weatherapplication.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q")cityName:String,
        @Query("units")units:String="metric",
        @Query("appid")apiKey:String
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherByLocation(
        @Query("lat")lat:Double,
        @Query("lon")lon:Double,
        @Query("units")units: String="metric",
        @Query("appid")apiKey: String
    ): WeatherResponse
}