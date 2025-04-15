package com.jeklov.post.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeklov.post.data.models.Tag

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<Tag>
}