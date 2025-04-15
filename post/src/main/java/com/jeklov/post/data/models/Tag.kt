package com.jeklov.post.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)
