package com.jeklov.post.data.db.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jeklov.post.data.models.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)

    @Query("SELECT * FROM posts")
    fun getAllPosts(): MutableLiveData<List<Post>>

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: Long)
}