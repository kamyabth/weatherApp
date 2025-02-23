package com.example.weatherapplication.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update


@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City)
    @Query("SELECT * FROM Users WHERE cityName = :cityName LIMIT 1")
    suspend fun getCityByName(cityName: String): City?
    @Query("SELECT * FROM users GROUP BY cityName ORDER BY id DESC LIMIT 10")
    fun getLast10Cities(): LiveData<List<City>>
    @Query("DELETE FROM users WHERE id = (SELECT ID FROM users ORDER BY id ASC LIMIT 1)")
    suspend fun deleteOldCity()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    suspend fun insertOrUpdateCity(city: City){
        val existingCity = getCityByName(city.cityName)
        if (existingCity != null){
            insert(city)
        }else{
            if ((getLast10Cities().value?.size ?: 0) >= 10){
                deleteOldCity()
            }
            insert(city)
        }


    }
    @Query("SELECT COUNT(*) FROM Users")
    suspend fun getCityCount(): Int



}