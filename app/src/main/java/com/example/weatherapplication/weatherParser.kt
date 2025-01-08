package com.example.weatherapplication

import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WeatherParser {
    fun parseWeatherData(response: String): WeatherData? {
        return try {
            val jsonObj = JSONObject(response)
            val main = jsonObj.getJSONObject("main")
            val sys = jsonObj.getJSONObject("sys")
            val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
            val wind = jsonObj.getJSONObject("wind")
            WeatherData(
                address = "${jsonObj.optString("name", "Unknown City")}, ${sys.optString("country", "Unknown Country")}",
                updatedAt = "Updated at: ${SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(jsonObj.getLong("dt") * 1000))}",
                temperature = main.getString("temp") + "°C",
                minTemperature = "Min temp: ${main.getString("temp_min")}°C",
                maxTemperature = "Max temp: ${main.getString("temp_max")}°C",
                pressure = main.getString("pressure"),
                humidity = main.getString("humidity"),
                sunrise = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sys.getLong("sunrise") * 1000)),
                sunset = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sys.getLong("sunset") * 1000)),
                windSpeed = wind.getString("speed"),
                weatherDescription = weather.getString("description")
            )
        } catch (e: Exception) {
            Log.e("WeatherParser", "Error parsing weather data: ${e.message}", e)
            null
        }
    }
}

