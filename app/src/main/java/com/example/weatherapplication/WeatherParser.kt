package com.example.weatherapplication

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun WeatherResponse.toData(): WeatherData {
    val dateFormat = SimpleDateFormat("dd,MM,yyyy hh:mm a", Locale.ENGLISH)
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    val updatedAT = dt * 1000
    val formattedDate = dateFormat.format(Date(updatedAT))
    val sunriseFormatted = timeFormat.format(Date(sys.sunrise * 1000))
    val sunsetFormatted = timeFormat.format(Date(sys.sunset * 1000))
    return WeatherData(
        address = "${name},${sys.country}",
        updatedAt = "updated at: $formattedDate",
        temperature = "${main.temp}°C",
        minTemperature = "Min temp: ${main.tempMin}°C",
        maxTemperature = "Max temp: ${main.tempMax}°C",
        pressure = main.pressure.toString(),
        humidity = main.humidity.toString(),
        sunrise = sunriseFormatted,
        sunset = sunsetFormatted,
        windSpeed = wind.speed.toString(),
        weatherDescription = weather[0].description.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
    )
}

data class WeatherResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("main")
    val main: Main,
    @SerializedName("wind")
    val wind: Wind,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("dt")
    val dt: Long
)

data class Sys(
    @SerializedName("country")
    val country: String,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long
)

data class Main(
    @SerializedName("temp")
    val temp: Float,
    @SerializedName("temp_min")
    val tempMin: Float,
    @SerializedName("temp_max")
    val tempMax: Float,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("humidity")
    val humidity: Int
)

data class Wind(
    @SerializedName("speed")
    val speed: Float
)

data class Weather(
    @SerializedName("description")
    val description: String
)

