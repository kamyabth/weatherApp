package com.example.weatherapplication.roomdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    val allUsers: LiveData<List<City>>

    init {
        val userDao = AppDatabase.getInstance(application).cityDao()
        repository = UserRepository(userDao)
        allUsers = userDao.getLast10Cities()
    }

    fun insert(user: City) = viewModelScope.launch {
        repository.insertORUpdateCity(user)
    }
    fun getCityByName(cityName: String, callback: (City?) -> Unit) {
        viewModelScope.launch {
            val city = repository.getCityByName(cityName)
            callback(city)
        }
    }
}