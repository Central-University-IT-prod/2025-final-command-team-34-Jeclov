package com.jeklov.superfinancer.data.api

import com.jeklov.superfinancer.data.models.Ticker
import com.jeklov.superfinancer.data.util.Constants.Companion.API_KEY_TICKERS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TickerAPI {
    @GET("quote")
    suspend fun getTickerMass(
        @Query("symbol")
        symbol: String ="AAPL",
        @Query("token")
        apiKey: String = API_KEY_TICKERS
    ): List<Ticker>
    @GET("stock/profile2")
    suspend fun searchForNews(
        @Query("symbol")
        symbol: String ="AAPL",
        @Query("token")
        apiKey: String = API_KEY_TICKERS
    ): List<Ticker>
}