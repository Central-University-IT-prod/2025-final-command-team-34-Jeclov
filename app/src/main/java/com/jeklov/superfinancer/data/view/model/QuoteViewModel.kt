package com.jeklov.superfinancer.data.view.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeklov.superfinancer.data.api.RetrofitInstance
import com.jeklov.superfinancer.data.models.news.NewsResponse
import com.jeklov.superfinancer.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.jeklov.superfinancer.data.util.Constants.Companion.NO_SIGNAL
import com.jeklov.superfinancer.data.util.Constants.Companion.UNABLE_TO_CONNECT
import com.jeklov.superfinancer.data.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class QuoteViewModel: ViewModel() {
    /*var quote: MutableLiveData<Resource<String?>> = MutableLiveData()
    val quoteResponse: String? = null

    init {
        getQuote()
    }

    private suspend fun getQuoteFromAPI() {
        quote.postValue(Resource.Loading())
        try {
            if (true) {
                val response = RetrofitInstance.quoteAPI.getQuote()
                quote.postValue(quoteResponse(response))
            } else {
                quote.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> quote.postValue(Resource.Error(UNABLE_TO_CONNECT))
                else -> quote.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private fun quoteResponse(response: Response<String>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (quoteResponse == null) {
                    quoteResponse = resultResponse
                } else {
                    val oldNews = resultResponse.results
                    val newNews = resultResponse.results
                    oldNews.addAll(newNews)
                }
                return Resource.Success(quoteResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    fun getQuote() {
        viewModelScope.launch {
            getQuoteFromAPI()
        }
    }*/

        var quote: String? = null//MutableLiveData<Resource<String?>> = MutableLiveData()

        init {
            getQuote()
        }

        private suspend fun getQuoteFromAPI() {
            //quote.postValue(Resource.Loading())
            try {
                quote = RetrofitInstance.quoteAPI.getQuote()//.postValue(RetrofitInstance.quoteAPI.getQuote())
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        /*private suspend fun allNewsInternet() {
            allNews.postValue(Resource.Loading())
            try {
                if (internetConnection((this.getQuote()))) {
                    val response = newsRepository.getNews(
                        limitNumber = 20,
                        offsetNumber = 0
                    )
                    allNews.postValue(allNewsResponse(response))
                } else {
                    allNews.postValue(Resource.Error(NO_INTERNET_CONNECTION))
                }
            } catch (t: Throwable) {
                when(t) {
                    is IOException -> allNews.postValue(Resource.Error(UNABLE_TO_CONNECT))
                    else -> allNews.postValue(Resource.Error(NO_SIGNAL))
                }
            }
        }*/

        fun internetConnection(context: Context): Boolean {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
                return getNetworkCapabilities(activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                } ?: false
            }
        }

        fun getQuote() {
            viewModelScope.launch {
                getQuoteFromAPI()
            }
        }

}