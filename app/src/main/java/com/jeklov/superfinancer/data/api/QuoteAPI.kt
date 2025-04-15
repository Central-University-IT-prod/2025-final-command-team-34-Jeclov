package com.jeklov.superfinancer.data.api

import com.jeklov.superfinancer.data.models.Quote
import retrofit2.http.GET
import android.util.Log
import retrofit2.Response

interface QuoteAPI {
        @GET("")
        fun getQuote(): String

}