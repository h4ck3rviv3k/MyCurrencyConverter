package com.example.mycurrencyconverter.ui.model

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.mycurrencyconverter.domain.model.CurrencyData
import com.example.mycurrencyconverter.domain.model.CurrencyTransfer
import com.example.mycurrencyconverter.domain.repository.LocalDataRepository
import com.example.mycurrencyconverter.domain.repository.RemoteDataRepository
import com.example.mycurrencyconverter.network.api.CurrencyRateDTO
import com.example.mycurrencyconverter.network.api.CurrencyTypeDTO
import org.koin.dsl.module
import java.util.*

/**
 * @author: Vivek Singh
 * @property CurrencyViewModel : This class is responsible for interacting with View and Network Layer using Repository
 */
val currencyViewModule = module {
    factory { CurrencyViewModel(get(), get(), get()) }
}

class CurrencyViewModel(
    private val remoteDataRepository: RemoteDataRepository,
    private val localDataRepository: LocalDataRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val isCurrencyDataDirty = MutableLiveData<Boolean>()
    private val inputPrice = MutableLiveData<Double>()
    private val targetedCurrencyRate = MutableLiveData<Double>()

    companion object {
        const val KEY_LOG_TIME = "lastSyncTime"
        const val THRESHOLD_TIME=30 * 60 * 1000
    }

    private val currencyTransferData = isCurrencyDataDirty.switchMap {
        liveData {
            var returnData = CurrencyTransfer()
            try {
                val lastSaveTime = sharedPreferences.getLong(KEY_LOG_TIME, -1)
                val logTime = Calendar.getInstance().time.time
                if ((lastSaveTime + THRESHOLD_TIME) > logTime) {
                    returnData.data.addAll(localDataRepository.loadRateList()) // Get Data from Local
                } else {
                    val currencyTypes =
                        remoteDataRepository.getCurrencyTypes() // Get Data from Remote
                    val rateData = remoteDataRepository.getCurrencyRates()
                    returnData = getCurrencyData(currencyTypes, rateData)
                }
            } catch (exception: Throwable) {
                exception.message?.let { Log.i("error", it) }
            }
            emit(returnData)
        }
    }

    fun readyData(): LiveData<CurrencyTransfer> = currencyTransferData
    fun getTargetPrice(): LiveData<Double> = inputPrice
    fun getStandRate(): LiveData<Double> = targetedCurrencyRate

    fun fetchData() {
        isCurrencyDataDirty.value = true
    }

    fun updatePrice(price: Double) {
        inputPrice.value = price
    }

    fun updateRate(rate: Double) {
        targetedCurrencyRate.value = rate
    }

    private suspend fun getCurrencyData(
        currencyTypes: CurrencyTypeDTO,
        currencyRateData: CurrencyRateDTO
    ): CurrencyTransfer {
        val currencyTransferData = CurrencyTransfer()

        val currencyQuotes = currencyRateData.quotes
        val currencyMap = currencyTypes.currencies

        if (currencyQuotes != null) {
            val otherCurrencyCodeList: MutableList<String> = mutableListOf()
            if (currencyMap != null) {
                otherCurrencyCodeList.addAll(currencyMap.keys.toList())
            } else {
                for (key in currencyQuotes.keys.toList()) {
                    otherCurrencyCodeList.add(key.replaceFirst(currencyRateData.source, ""))
                }
            }
            val sourceCurrency = currencyRateData.source
            for (currencyCode in otherCurrencyCodeList) {
                validateCurrencyCodeAndGetCurrencyData(
                    currencyQuotes,
                    currencyMap,
                    currencyCode,
                    sourceCurrency
                )?.let {
                    currencyTransferData.data.add(it)
                }
            }
            //Save all of the Currency data in the Room DB
            localDataRepository.updateAllRate(currencyTransferData.data)

            // Log the time in SharedPreferences
            val logTime = Calendar.getInstance().time.time
            sharedPreferences.edit { putLong(KEY_LOG_TIME, logTime) }
        }

        return currencyTransferData
    }

    private fun validateCurrencyCodeAndGetCurrencyData(
        currencyQuotes: Map<String, Double>,
        currencyMap: Map<String, String>?,
        currencyCode: String,
        sourceCurrency: String
    ): CurrencyData? {
        val formattedKey = "${sourceCurrency}${currencyCode}" // code + target type code
        if (currencyQuotes.containsKey(formattedKey)) {
            var countryName = currencyCode
            currencyMap?.let { countryName = currencyMap[currencyCode] ?: "" }
            return CurrencyData(
                code = currencyCode,
                name = countryName,
                rate = currencyQuotes[formattedKey] ?: 1.0
            )
        }
        return null
    }

}
