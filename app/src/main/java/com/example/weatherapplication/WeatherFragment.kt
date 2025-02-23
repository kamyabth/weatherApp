package com.example.weatherapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapplication.FirstPage.SelectWeatherOptionDialogFragment
import com.example.weatherapplication.roomdb.City
import com.example.weatherapplication.roomdb.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale


@Suppress("DEPRECATION")
class WeatherFragment : Fragment() {
    private lateinit var dayAnimationView: LottieAnimationView
    private lateinit var nightAnimationView: LottieAnimationView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val API = "338b38e3e8755a8de39629feb35cc270"
    private lateinit var userViewModel: UserViewModel
    val LOCATION_PERMISSION_REQUEST_CODE = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayAnimationView = view.findViewById(R.id.day_animation)
        nightAnimationView = view.findViewById(R.id.night_animation)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        // Setup Swipe to Refresh
        swipeRefreshLayout.setOnRefreshListener {
            checkLocationPermission()
        }

        checkLocationPermission()
        setBackgroundAnimation()
    }


    private fun setBackgroundAnimation() {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        if (currentHour in 6..18) {
            dayAnimationView.setAnimation(R.raw.day)
            nightAnimationView.visibility = View.GONE
            dayAnimationView.visibility = View.VISIBLE
            if (!dayAnimationView.isAnimating) dayAnimationView.playAnimation()
        } else {
            nightAnimationView.setAnimation(R.raw.night)
            dayAnimationView.visibility = View.GONE
            nightAnimationView.visibility = View.VISIBLE
            if (!nightAnimationView.isAnimating) nightAnimationView.playAnimation()
        }

    }

    @SuppressLint("MissingPermission")
    private fun fetchWeather() {
        val cityName = requireArguments().getString("city")

        val sharedPreferences =
            requireContext().getSharedPreferences("dictionary", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("default_city", cityName)
        editor.apply()

        swipeRefreshLayout.isRefreshing = true

        if (isInternetAvailable()) {
            if (cityName?.isNotEmpty() == true) {

            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lifecycleScope.launch {
                        val weatherData = WeatherRepository.fetchWeatherData(
                            location.latitude,
                            location.longitude,
                            API,
                            cityName
                        )

                        updateUI(weatherData)

                        if (weatherData != null && cityName != null) {
                            val city = City(
                                temperature = weatherData.temperature,
                                cityName = cityName,
                                id = id,
                                pressure = weatherData.pressure,
                                sunset = weatherData.sunset,
                                sunrise = weatherData.sunrise,
                                windSpeed = weatherData.windSpeed,
                                humidity = weatherData.humidity,
                                maxTemperature = weatherData.maxTemperature,
                                minTemperature = weatherData.minTemperature
                            )


                            userViewModel.insert(
                                user = city
                            )
                        }

                    }
                } else {
                    Log.e("WeatherApp", "Location not found")
                }
                swipeRefreshLayout.isRefreshing = false
            }.addOnFailureListener { e ->
                Log.e("WeatherApp", "Failed to get location: ${e.message}", e)
                swipeRefreshLayout.isRefreshing = false

                cityName?.let {
                    userViewModel.getCityByName(it) { city ->
                        if (city != null) {
                            updateUI(city.toWeatherData())

                        } else {
                            Toast.makeText(
                                activity,
                                "internet disconnected and no information in database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        } else {
            cityName?.let {
                userViewModel.getCityByName(it) { city ->
                    if (city != null) {
                        updateUI(city.toWeatherData())
                        swipeRefreshLayout.isRefreshing = false
                    } else {
                        Toast.makeText(
                            activity,
                            "internet disconnected and no information in database ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
            }
        }
    }

    private fun updateUI(weatherData: WeatherData?) {
        if (weatherData != null) {
            view?.findViewById<TextView>(R.id.address)?.text = weatherData.address
            view?.findViewById<TextView>(R.id.updated_at)?.text = weatherData.updatedAt
            view?.findViewById<TextView>(R.id.status)?.text =
                weatherData.weatherDescription.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
            view?.findViewById<TextView>(R.id.temp)?.text = weatherData.temperature
            view?.findViewById<TextView>(R.id.temp_min)?.text = weatherData.minTemperature
            view?.findViewById<TextView>(R.id.temp_max)?.text = weatherData.maxTemperature
            view?.findViewById<TextView>(R.id.pressure)?.text = weatherData.pressure
            view?.findViewById<TextView>(R.id.humidity)?.text = weatherData.humidity
            view?.findViewById<TextView>(R.id.wind)?.text = weatherData.windSpeed
            view?.findViewById<TextView>(R.id.sunrise)?.text = weatherData.sunrise
            view?.findViewById<TextView>(R.id.sunset)?.text = weatherData.sunset

            view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.GONE
            view?.findViewById<RelativeLayout>(R.id.mainContainer)?.visibility = View.VISIBLE
        } else {
            view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.errorTxt)?.visibility = View.VISIBLE
        }
    }


    @SuppressLint("ServiceCast")
    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fetchWeather()
        }
    }
}

fun City.toWeatherData() = WeatherData(
    address = cityName,
    temperature = temperature,
    updatedAt = "",
    minTemperature = minTemperature,
    maxTemperature = maxTemperature,
    pressure = pressure,
    humidity = humidity,
    windSpeed = windSpeed,
    sunrise = sunrise,
    sunset = sunset,
    weatherDescription = ""
)



