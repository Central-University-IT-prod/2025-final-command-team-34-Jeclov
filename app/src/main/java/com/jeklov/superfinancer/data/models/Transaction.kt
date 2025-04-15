package com.jeklov.superfinancer.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactionItems")
data class Transaction(
    @PrimaryKey
    val id: Int? = null, // DataBase generated
    //@ColumnInfo(name = "aimId")
    var aimId: Int? = null, // Null if aim deleted
    var aimName: String, // We use it if aim was deleted, var because we can rename aim
    var amount: Int,
    var withdrawalOrReplenishment: Boolean
)