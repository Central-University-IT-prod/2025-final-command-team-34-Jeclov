package com.jeklov.superfinancer.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "aimItems")
data class Aim(
    @PrimaryKey
    val id: Int? = null, // DataBase generated
    var name: String,
    var requiredFunds: Int,
    var accumulatedFunds: Int,
    var date: Date
)