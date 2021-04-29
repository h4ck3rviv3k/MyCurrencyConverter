package com.example.mycurrencyconverter.domain.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mycurrencyconverter.domain.model.CurrencyData

/**
 * @author: Vivek Singh
 * @property RateDao : This class is responsible for doing CURD operation in local database
 */
@Dao
interface RateDao {
    @Query("SELECT * from collect_table")
    suspend fun getAllList(): List<CurrencyData>

    @Query("SELECT * from collect_table WHERE code = :typeCode")
    suspend fun findRate(typeCode: String): CurrencyData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRate(rateData: CurrencyData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAllRate(rateList: List<CurrencyData>)
}