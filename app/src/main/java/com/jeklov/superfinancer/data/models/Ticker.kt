package com.jeklov.superfinancer.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickerItems")
data class Ticker(
    @PrimaryKey
    val id: Int? = null, // DataBase generated
    val currentPrice: Double,
    val change: Double,
    val percentChange: Double,
    val highPriceDay: Double,
    val lowPriceDay: Double,
    val openPriceDay: Double,
    val previousClosePrice: Double
)
