package com.example.weatherapplication

import android.annotation.SuppressLint
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val API: String = "338b38e3e8755a8de39629feb35cc270"

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocationAndFetchWeather()
            } else {
                Log.e("WeatherApp", "Location permission denied")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            refreshWeatherData()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            getLocationAndFetchWeather()
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }
        setupBackgroundVideo()
    }
    override fun onResume() {
        super.onResume()
        setupBackgroundVideo()
    }
    private fun refreshWeatherData(){
        swipeRefreshLayout.isRefreshing=true
        getLocationAndFetchWeather()
    }



    @SuppressLint("MissingPermission")
        private fun getLocationAndFetchWeather() {

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {

                    CoroutineScope(Dispatchers.Main).launch {
                        val weatherData = WeatherRepository.fetchWeatherData(
                            location.latitude,
                            location.longitude,
                            API
                        )
                        withContext(Dispatchers.Main) {
                            updateUI(weatherData)
                        }
                    }
                } else {
                    Log.e("WeatherApp", "location not found")
                }
            }.addOnFailureListener { e ->
                Log.e("WeatherApp", "Failed to get location: ${e.message}", e)
            }
        }


        private fun updateUI(weatherData: WeatherData?) {
            if (weatherData != null) {
                findViewById<TextView>(R.id.address).text = weatherData.address
                findViewById<TextView>(R.id.updated_at).text = weatherData.updatedAt
                findViewById<TextView>(R.id.status).text =
                    weatherData.weatherDescription.capitalize(Locale.ROOT)
                findViewById<TextView>(R.id.temp).text = weatherData.temperature
                findViewById<TextView>(R.id.temp_min).text = weatherData.minTemperature
                findViewById<TextView>(R.id.temp_max).text = weatherData.maxTemperature
                findViewById<TextView>(R.id.pressure).text = weatherData.pressure
                findViewById<TextView>(R.id.sunrise).text = weatherData.sunrise
                findViewById<TextView>(R.id.sunset).text = weatherData.sunset
                findViewById<TextView>(R.id.wind).text = weatherData.windSpeed
                findViewById<TextView>(R.id.humidity).text = weatherData.humidity




                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            } else {
                Log.e("WeatherApp", "Error data UI")
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorTxt).visibility = View.VISIBLE


            }
            swipeRefreshLayout.isRefreshing = false
        }



    private fun setupBackgroundVideo() {
        val videoView = findViewById<VideoView>(R.id.videoBackground)
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val videoUri = if (currentHour in 6..18) {
            Uri.parse("android.resource://" + packageName + "/" + R.raw.sun_clip)
        } else {
            Uri.parse("android.resource://" + packageName + "/" + R.raw.moon_clip)
        }
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
        videoView.start()
    }
}
