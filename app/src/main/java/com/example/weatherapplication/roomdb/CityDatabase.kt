package com.example.weatherapplication.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [City::class], version = 2, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun cityDao():CityDao

    companion object{
        @Volatile
        private var INSTANCE:AppDatabase? = null


        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context):AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

    }
}