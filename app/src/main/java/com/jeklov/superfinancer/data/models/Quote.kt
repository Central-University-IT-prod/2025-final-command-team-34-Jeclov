package com.jeklov.superfinancer.data.models

import androidx.room.PrimaryKey

data class Quote(
    @PrimaryKey
    val id: Int? = null, // DataBase generated
    val author: String,
    val quote: String
)
