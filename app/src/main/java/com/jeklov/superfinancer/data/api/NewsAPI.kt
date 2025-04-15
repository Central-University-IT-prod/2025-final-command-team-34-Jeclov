package com.jeklov.superfinancer.data.api

import com.jeklov.superfinancer.data.models.news.NewsResponse
import com.jeklov.superfinancer.data.util.Constants.Companion.API_KEY_NEWS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("svc/news/v3/content/all/home.json")
    suspend fun getNews(
        @Query("limit")
        limitNumber: Int,
        @Query("offset")
        offsetNumber: Int,
        @Query("api-key")
        apiKey: String = API_KEY_NEWS
    ): Response<NewsResponse>
    @GET("svc/search/v2/articlesearch.json")
    suspend fun searchForNews(
        @Query("page")
        pageNumber: Int = 1,
        @Query("q")
        q: String,
        @Query("api-key")
        apiKey: String = API_KEY_NEWS
    ): Response<NewsResponse>
}