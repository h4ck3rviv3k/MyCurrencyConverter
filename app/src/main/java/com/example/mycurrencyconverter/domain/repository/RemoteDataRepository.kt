package com.example.mycurrencyconverter.domain.repository

import com.example.mycurrencyconverter.network.NetworkApi
import org.koin.dsl.module

/**
 * @author: Vivek Singh
 */
val remoteDataRepoModule = module {
    factory { RemoteDataRepository(get()) }
}

class RemoteDataRepository(private val networkApi: NetworkApi) {
    suspend fun getCurrencyTypes() = networkApi.getCurrencyTypes()
    suspend fun getCurrencyRates() = networkApi.getCurrencyRates()
}