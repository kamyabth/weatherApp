package com.example.weatherapplication.roomdb

import androidx.lifecycle.LiveData

class UserRepository(private val cityDao: CityDao) {

    val last10City: LiveData<List<City>> = cityDao.getLast10Cities()

    suspend fun insertORUpdateCity(city: City) {
        cityDao.insertOrUpdateCity(city)
    }
    suspend fun getCityByName(cityName: String): City? {
        return cityDao.getCityByName(cityName)

    }
}