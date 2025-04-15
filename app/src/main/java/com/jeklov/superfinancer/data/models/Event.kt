package com.jeklov.superfinancer.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eventItems")
data class Event(
    @PrimaryKey
    val id: Int? = null, // DataBase generated

)