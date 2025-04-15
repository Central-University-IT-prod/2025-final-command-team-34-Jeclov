package com.jeklov.superfinancer.data.models.news

data class NewsResponse(
    val copyright: String,
    val num_results: Int,
    val results: MutableList<News>,
    val status: String
)