package com.jeklov.superfinancer.data.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jeklov.superfinancer.data.api.RetrofitInstance
import com.jeklov.superfinancer.data.models.news.News
import com.jeklov.superfinancer.data.models.news.NewsResponse
import com.jeklov.superfinancer.data.repository.NewsRepository
import com.jeklov.superfinancer.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.jeklov.superfinancer.data.util.Constants.Companion.NO_SIGNAL
import com.jeklov.superfinancer.data.util.Constants.Companion.UNABLE_TO_CONNECT
import com.jeklov.superfinancer.data.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    app: Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    private val _counter = MutableSharedFlow<Int>()
    val counter: SharedFlow<Int> = _counter

    private var currentValue = 60
    private var isCounting = false

    // LiveData all News
    val allNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var allNewsPages = 1
    var allNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPages = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    init {
        getAllNews()
    }

    fun getAllNews() = viewModelScope.launch {
        allNewsInternet()
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }

    private fun allNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                allNewsPages++
                if (allNewsResponse == null) {
                    allNewsResponse = resultResponse
                } else {
                    val oldNews = resultResponse.results
                    val newNews = resultResponse.results
                    oldNews.addAll(newNews)
                }
                return Resource.Success(allNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPages = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    allNewsPages++
                    val oldNews = searchNewsResponse?.results
                    val newNews = resultResponse.results
                    oldNews?.addAll(newNews)
                }
                return Resource.Success(allNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToFavorites(news: News) = viewModelScope.launch {
        newsRepository.upset(news)
    }

    fun getFavoriteNews() = newsRepository.getFavoriteNews()

    fun deleteNews(news: News) = viewModelScope.launch {
        newsRepository.deleteNews(news)
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

    private suspend fun allNewsInternet() {
        allNews.postValue(Resource.Loading())
        try {
            if (internetConnection((this.getApplication()))) {
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
    }

    private suspend fun searchNewsInternet(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if (internetConnection((this.getApplication()))) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPages)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error(UNABLE_TO_CONNECT))
                else -> searchNews.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    fun startCounter() {
        if (!isCounting) {
            isCounting = true
            viewModelScope.launch {
                while (currentValue > 0) {
                    delay(1000)
                    currentValue--
                    _counter.emit(currentValue) // Emit the updated value
                }
                isCounting = false
                getAllNews() // Call a function to reset the counter or perform another action
                currentValue = 60
                currentValue = 60
            }
        }
    }
}