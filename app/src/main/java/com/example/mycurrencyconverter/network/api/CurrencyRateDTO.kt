package com.example.mycurrencyconverter.network.api

import androidx.annotation.Keep

/**
 * @author: Vivek Singh
 */
@Keep
data class CurrencyRateDTO(val source: String, val quotes: Map<String, Double>?)