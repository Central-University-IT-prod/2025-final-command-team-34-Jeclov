package com.jeklov.superfinancer.data.api

import com.jeklov.superfinancer.data.util.Constants.Companion.BASE_URL_NEWS
import com.jeklov.superfinancer.data.util.Constants.Companion.BASE_URL_QUOTE
import com.jeklov.superfinancer.data.util.Constants.Companion.BASE_URL_TICKERS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {

        private val retrofitNews by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL_NEWS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val newsAPI by lazy {
            retrofitNews.create(NewsAPI::class.java)
        }

        private val retrofitTicker by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL_TICKERS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val tickerAPI by lazy {
            retrofitTicker.create(TickerAPI::class.java)
        }

        private val retrofitQuote by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL_QUOTE)
                //.addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val quoteAPI by lazy {
            retrofitQuote.create(QuoteAPI::class.java)
        }
    }
}