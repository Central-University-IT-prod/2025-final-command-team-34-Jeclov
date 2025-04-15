package com.jeklov.superfinancer.data.repository

import com.jeklov.superfinancer.data.api.RetrofitInstance
import com.jeklov.superfinancer.data.db.MainDB
import com.jeklov.superfinancer.data.models.news.News

class NewsRepository(val db: MainDB) {
    suspend fun getNews(limitNumber: Int, offsetNumber: Int) =
        RetrofitInstance.newsAPI.getNews(
            limitNumber = limitNumber,
            offsetNumber = offsetNumber
        )
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.newsAPI.searchForNews(
            pageNumber = pageNumber,
            q = searchQuery
        )
    suspend fun upset(news: News) = db.newsDao().upsertNewsItem(news)

    fun getFavoriteNews() = db.newsDao().getAllNewsItems()

    suspend fun deleteNews(news: News) = db.newsDao().deleteNewsItem(news)
}