package com.example.mycurrencyconverter.domain.pref

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * @author: Vivek Singh
 */
val sharedPrefModule = module {
    single { setupSharedPreferences(androidApplication()) }
}

private const val PREFERENCE_KEY = "com.example.mycurrencyconverter_preferences"

private fun setupSharedPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)