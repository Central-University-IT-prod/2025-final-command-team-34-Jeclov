package com.jeklov.post.data.view.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeklov.post.data.db.PostDB
import com.jeklov.post.data.models.Post
import kotlinx.coroutines.launch

class PostViewModel(val postDB: PostDB) : ViewModel() {

    var posts: MutableLiveData<List<Post>> = MutableLiveData()
    
    init {
        getPosts()
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            postDB.postDao().insertPost(post)
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            postDB.postDao().updatePost(post)
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postDB.postDao().deletePost(post)
        }
    }

    fun toggleFavorite(post: Post) {
        post.favorite = true
        updatePost(post)
    }

    fun getPosts() {
        posts = postDB.postDao().getAllPosts()
    }
}