package com.example.mycurrencyconverter

import android.app.Application
import com.example.mycurrencyconverter.domain.db.dbSetupModule
import com.example.mycurrencyconverter.domain.pref.sharedPrefModule
import com.example.mycurrencyconverter.domain.repository.localDataRepoModule
import com.example.mycurrencyconverter.domain.repository.remoteDataRepoModule
import com.example.mycurrencyconverter.network.networkModule
import com.example.mycurrencyconverter.ui.model.currencyViewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * @author: Vivek Singh
 * @property MyCurrencyConverterApplication : This class is the main application class which initialize DI using Koin Library
 */
class MyCurrencyConverterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    remoteDataRepoModule,
                    localDataRepoModule,
                    networkModule,
                    currencyViewModule,
                    dbSetupModule(),
                    sharedPrefModule
                )
            )
        }
    }
}