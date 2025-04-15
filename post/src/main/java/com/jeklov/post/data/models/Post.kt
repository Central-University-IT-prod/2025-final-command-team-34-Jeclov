package com.jeklov.post.data.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val tags: List<String>,
    val photos: List<Uri>,
    val url: String? = null,
    var favorite: Boolean = false
)
