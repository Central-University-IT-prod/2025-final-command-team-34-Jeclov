package com.jeklov.post.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jeklov.post.data.db.dao.PostDao
import com.jeklov.post.data.db.dao.TagDao
import com.jeklov.post.data.models.Post
import com.jeklov.post.data.models.Tag

@Database(
    entities = [Post::class, Tag::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class PostDB : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile
        private var INSTANCE: PostDB? = null

        fun getInstance(context: Context): PostDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostDB::class.java,
                    "post_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}