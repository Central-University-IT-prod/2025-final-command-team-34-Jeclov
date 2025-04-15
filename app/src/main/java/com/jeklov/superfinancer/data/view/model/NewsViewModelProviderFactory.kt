package com.jeklov.superfinancer.data.view.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeklov.superfinancer.data.repository.NewsRepository

class NewsViewModelProviderFactory(val application: Application, val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(application, newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}