package com.jeklov.post.data.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeklov.post.data.db.PostDB

class PostViewModelProviderFactory(val database: PostDB) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}