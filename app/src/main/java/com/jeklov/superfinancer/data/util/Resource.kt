package com.jeklov.superfinancer.data.util

import com.jeklov.superfinancer.data.models.news.NewsResponse

sealed class Resource<T>(
    val data: NewsResponse? = null,
    val message: String? = null
) {
    class Success<T>(data: NewsResponse?) : Resource<T>(data)
    class Error<T>(message: String, data: NewsResponse? = null): Resource<T>(data, message)
    class Loading<T>: Resource<T>()
}