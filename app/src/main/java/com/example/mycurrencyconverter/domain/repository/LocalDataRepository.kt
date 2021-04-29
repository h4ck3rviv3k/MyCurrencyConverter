package com.example.mycurrencyconverter.domain.repository

import com.example.mycurrencyconverter.domain.db.RateDao
import com.example.mycurrencyconverter.domain.db.RateDatabase
import com.example.mycurrencyconverter.domain.model.CurrencyData
import org.koin.dsl.module

/**
 * @author: Vivek Singh
 */
val localDataRepoModule = module {
    factory { LocalDataRepository(get()) }
    factory { get<RateDatabase>().rateDao() }
}

class LocalDataRepository(private val rateDao: RateDao) {
    suspend fun loadRateList(): List<CurrencyData> = rateDao.getAllList()
    suspend fun updateAllRate(saveList: List<CurrencyData>) = rateDao.updateAllRate(saveList)
}