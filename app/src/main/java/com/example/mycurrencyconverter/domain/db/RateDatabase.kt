package com.example.mycurrencyconverter.domain.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mycurrencyconverter.domain.model.CurrencyData
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * @author: Vivek Singh
 * @property dbSetupModule() : This Koin Module is responsible for creating Tabels in local db
 */
fun dbSetupModule() = module {
    single {
        Room.databaseBuilder(androidContext(), RateDatabase::class.java, "CurrencyTable.db").build()
    }
}

@Database(entities = [CurrencyData::class], version = 1)
abstract class RateDatabase : RoomDatabase() {
    abstract fun rateDao(): RateDao
}