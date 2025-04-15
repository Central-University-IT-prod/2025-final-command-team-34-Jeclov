package com.jeklov.superfinancer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jeklov.superfinancer.data.db.dao.AimDao
import com.jeklov.superfinancer.data.db.dao.NewsDao
import com.jeklov.superfinancer.data.db.dao.TickerDao
import com.jeklov.superfinancer.data.db.dao.TransactionDao
import com.jeklov.superfinancer.data.models.Aim
import com.jeklov.superfinancer.data.models.Ticker
import com.jeklov.superfinancer.data.models.Transaction
import com.jeklov.superfinancer.data.models.news.News

@Database(
    entities = [Aim::class, News::class, Ticker::class, Transaction::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MainDB : RoomDatabase() {
    abstract fun aimDao(): AimDao
    abstract fun newsDao(): NewsDao
    abstract fun tickerDao(): TickerDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: MainDB? = null

        fun getInstance(context: Context): MainDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDB::class.java,
                    "main_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}