package com.example.weatherapplication.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class City (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "cityName") val cityName: String,
    @ColumnInfo(name = "temperature")val temperature: String,
    @ColumnInfo(name = "pressure")val pressure:String,
    @ColumnInfo(name = "sunset")val sunset:String,
    @ColumnInfo(name = "sunrise")val sunrise:String,
    @ColumnInfo(name = "windSpeed")val windSpeed:String,
    @ColumnInfo(name = "humidity")val humidity:String,
    @ColumnInfo(name = "maxTemperature")val maxTemperature:String,
    @ColumnInfo(name = "minTemperature")val minTemperature:String



)

