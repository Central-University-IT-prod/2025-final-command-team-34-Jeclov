package com.jeklov.superfinancer.data.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeklov.superfinancer.data.db.MainDB
import com.jeklov.superfinancer.data.models.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionDB: MainDB) : ViewModel() {

    // Flow all Transaction
    val allTransactions: Flow<List<Transaction>> =
        transactionDB.transactionDao().getAllTransactionItems()

    fun insertTransaction(transaction: Transaction) {
        // Вставка транзакции
        viewModelScope.launch {
        transactionDB.transactionDao().insertTransactionItem(transaction)
        }

        // Обновление накопленных средств цели
        /*if (transaction.withdrawalOrReplenishment) { // Если это пополнение
            if (transaction.aimId != null) {
                transactionDB.aimDao().getAllAimItems().collect { it ->
                    if (it.first().id == 0) {
                        val aim = it.first()
                        aim.let {
                            it.accumulatedFunds += transaction.amount // Увеличиваем накопленные средства

                            transactionDB.aimDao()
                                .updateAimItem(it) // Обновляем цель в базе данных
                        }
                    }
                }
            }
        }*/
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDB.transactionDao().updateTransactionItem(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDB.transactionDao().deleteTransactionItem(transaction)
        }
    }

    fun getTransactionsByAimId(aimId: Int): Flow<List<Transaction>> {
        return transactionDB.transactionDao().getTransactionsByAimId(aimId)
    }
}