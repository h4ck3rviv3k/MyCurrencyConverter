package com.example.mycurrencyconverter.domain.model

/**
 * @author: Vivek Singh
 */
class CurrencyTransfer(var data: MutableList<CurrencyData> = mutableListOf()) {
    fun getFormattedCountryWithCurrencyName(): MutableList<String> {
        val countryNameList: MutableList<String> = mutableListOf()
        for (info in data) {
            val nameKey = "[${info.code}] - ${info.name}"
            countryNameList.add(nameKey)
        }
        return countryNameList
    }
}