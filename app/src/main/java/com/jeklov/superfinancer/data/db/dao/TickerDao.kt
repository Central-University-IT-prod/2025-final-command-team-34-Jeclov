package com.jeklov.superfinancer.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jeklov.superfinancer.data.models.Ticker
import kotlinx.coroutines.flow.Flow

@Dao
interface TickerDao {
    // Ticker DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTickerItem(tickerItem: Ticker)

    @Update
    suspend fun updateTickerItem(tickerItem: Ticker)

    @Delete
    suspend fun deleteTickerItem(tickerItem: Ticker)

    @Query("SELECT * FROM tickerItems")
    fun getAllTickerItems() : Flow<List<Ticker>>
}