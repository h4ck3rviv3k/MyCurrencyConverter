package com.example.mycurrencyconverter.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: Vivek Singh
 */
@Entity(tableName = "collect_table")
data class CurrencyData(
    @PrimaryKey
    @ColumnInfo(name = "code")
    var code: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "rate")
    var rate: Double = 1.0
)