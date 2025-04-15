package com.jeklov.superfinancer.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jeklov.superfinancer.data.models.Aim
import com.jeklov.superfinancer.data.models.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface AimDao {
    // Aim DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAimItem(aimItem: Aim)

    @Update
    suspend fun updateAimItem(aimItem: Aim)

    @Delete
    suspend fun deleteAimItem(aimItem: Aim)

    @Query("SELECT * FROM aimItems")
    fun getAllAimItems() : Flow<List<Aim>>

    @Query("SELECT * FROM aimItems WHERE Id = :aimId")
    fun getAimById(aimId: Int) : List<Aim?>
}