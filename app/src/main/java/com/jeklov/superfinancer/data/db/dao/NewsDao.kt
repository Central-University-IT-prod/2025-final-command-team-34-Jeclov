package com.jeklov.superfinancer.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeklov.superfinancer.data.models.news.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    // News DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNewsItem(newsItem: News)

    @Delete
    suspend fun deleteNewsItem(newsItem: News)

    @Query("SELECT * FROM newsItems")
    fun getAllNewsItems() : Flow<List<News>>
}