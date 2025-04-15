package com.jeklov.superfinancer.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jeklov.superfinancer.data.models.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    // Transaction DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionItem(transactionItem: Transaction)

    @Update
    suspend fun updateTransactionItem(transactionItem: Transaction)

    @Delete
    suspend fun deleteTransactionItem(transactionItem: Transaction)

    @Query("SELECT * FROM transactionItems")
    fun getAllTransactionItems() : Flow<List<Transaction>>

    @Query("SELECT * FROM transactionItems WHERE aimId = :aimId")
    fun getTransactionsByAimId(aimId: Int) : Flow<List<Transaction>>
}