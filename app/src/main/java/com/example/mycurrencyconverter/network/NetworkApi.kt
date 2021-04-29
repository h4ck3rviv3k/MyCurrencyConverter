package com.example.mycurrencyconverter.network

import com.example.mycurrencyconverter.BuildConfig
import com.example.mycurrencyconverter.network.api.CurrencyRateDTO
import com.example.mycurrencyconverter.network.api.CurrencyTypeDTO
import retrofit2.http.GET

/**
 * @author: Vivek Singh
 * @property networkModule : This interface will be used by RetrofitClient to make network calls
 */
interface NetworkApi {
    @GET("/api/list?access_key=${BuildConfig.API_TOKEN}&format=1")
    suspend fun getCurrencyTypes(): CurrencyTypeDTO

    @GET("/api/live?access_key=${BuildConfig.API_TOKEN}&format=1")
    suspend fun getCurrencyRates(): CurrencyRateDTO
}