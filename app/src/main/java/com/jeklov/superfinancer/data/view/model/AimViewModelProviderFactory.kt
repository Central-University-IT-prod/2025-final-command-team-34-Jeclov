package com.jeklov.superfinancer.data.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeklov.superfinancer.data.db.MainDB

class AimViewModelProviderFactory(val db: MainDB) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AimViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AimViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}