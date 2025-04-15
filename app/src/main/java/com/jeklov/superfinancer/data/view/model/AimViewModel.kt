package com.jeklov.superfinancer.data.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeklov.superfinancer.data.db.MainDB
import com.jeklov.superfinancer.data.models.Aim
import com.jeklov.superfinancer.data.models.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AimViewModel(private val aimDB: MainDB) : ViewModel() {

    // Flow all Aim
    val allAims: Flow<List<Aim>> = aimDB.aimDao().getAllAimItems()

    fun addAim(aim: Aim) {
        viewModelScope.launch {
            aimDB.aimDao().insertAimItem(aim)
        }
    }

    fun updateAim(aim: Aim) {
        viewModelScope.launch {
            aimDB.aimDao().updateAimItem(aim)
        }
    }

    fun deleteAim(aim: Aim) {
        viewModelScope.launch {
            val transaction = if (aim.accumulatedFunds > 0) {
                Transaction(
                    aimId = aim.id,
                    aimName = aim.name,
                    amount = aim.accumulatedFunds,
                    withdrawalOrReplenishment = false
                )
            } else {
                null
            }
            aimDB.aimDao().deleteAimItem(aim)
            transaction?.let { aimDB.transactionDao().insertTransactionItem(it) }
        }
    }

    fun calculateProgress(aim: Aim): Double {
        return if (aim.requiredFunds > 0) {
            (aim.accumulatedFunds.toDouble() / aim.requiredFunds) * 100
        } else {
            0.0
        }
    }
}