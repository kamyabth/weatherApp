package com.example.weatherapplication.FirstPage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.roomdb.City
import com.example.weatherapplication.roomdb.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationSelectionFragment : Fragment() {

    private lateinit var citySearchEditText: EditText
    private lateinit var searchCityButton: Button
    private lateinit var gpsLocationButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var cityRecyclerView: RecyclerView
    private lateinit var cityAdapter: CityAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val view = inflater.inflate(R.layout.fragment_location_selection, container, false)

        citySearchEditText = view.findViewById(R.id.citySearchEditText)
        searchCityButton = view.findViewById(R.id.searchCityButton)
        cityRecyclerView = view.findViewById(R.id.cityRecyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        cityAdapter = CityAdapter { city -> showWeatherSelectionDialog(city) }
        cityRecyclerView.adapter = cityAdapter
        cityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cityRecyclerView.isNestedScrollingEnabled = false
        layoutManager.stackFromEnd = true
        layoutManager.isSmoothScrollbarEnabled = true

        gpsLocationButton = view.findViewById(R.id.gpsLocationButton)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        searchCityButton.setOnClickListener {
            val cityName = citySearchEditText.text.toString().trim()

            val sharedPreferences =
                requireContext().getSharedPreferences("dictionary", Context.MODE_PRIVATE)
            val defaultCityName = sharedPreferences.getString("default_city", null)

            val name = cityName.ifEmpty { defaultCityName }

            name?.let {
                val city = City(
                    cityName = cityName,
                    temperature = "",
                    pressure = "",
                    sunset ="",
                    sunrise = "",
                    windSpeed = "",
                    humidity ="" ,
                    maxTemperature = "",
                    minTemperature = ""
                )
                userViewModel.insert(city)
                val action =
                    LocationSelectionFragmentDirections.actionLocationSelectionFragmentToWeatherFragment(
                        city = it
                    )

                findNavController().navigate(action)
            } ?: let {
                Toast.makeText(activity, "Pleas enter a city name", Toast.LENGTH_SHORT).show()
            }

        }


        gpsLocationButton.setOnClickListener {
            val action =
                LocationSelectionFragmentDirections.actionLocationSelectionFragmentToWeatherFragment(
                    city = ""
                )
            findNavController().navigate(action)
        }
        userViewModel.allUsers.observe(viewLifecycleOwner, Observer { cities ->
            cities?.let {
                Log.d("asdfghjkl", "onCreateView: $cities")

                cityAdapter.submitList(it)
            }
        })
        return view
    }

    private fun showWeatherSelectionDialog(city: City) {

        val dialog = SelectWeatherOptionDialogFragment(
            onOnlineClick = { fetchOnlineWeatherData(city) },
            onStoredClick = { showStoredWeatherData(city) }
        )
        dialog.show(parentFragmentManager, "SelectWeatherOptionDialog")
    }

    private fun showStoredWeatherData(city: City) {
        val action =
            LocationSelectionFragmentDirections.actionLocationSelectionFragmentToWeatherFragment(
                city.cityName
            )
        findNavController().navigate(action)

    }

    private fun fetchOnlineWeatherData(city: City) {
        val action =
            LocationSelectionFragmentDirections.actionLocationSelectionFragmentToWeatherFragment(
                city.cityName
            )
        findNavController().navigate(action)
    }

}


