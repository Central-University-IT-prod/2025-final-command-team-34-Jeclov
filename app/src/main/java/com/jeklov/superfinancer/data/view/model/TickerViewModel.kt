//package com.jeklov.superfinancer.data.view.model
//
//import android.app.Application
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.NetworkCapabilities
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.jeklov.superfinancer.data.models.Ticker
//import com.jeklov.superfinancer.data.models.ticker.TickerResponse
//import com.jeklov.superfinancer.data.repository.TickerRepository
//import com.jeklov.superfinancer.data.util.Constants.Companion.NO_INTERNET_CONNECTION
//import com.jeklov.superfinancer.data.util.Constants.Companion.NO_SIGNAL
//import com.jeklov.superfinancer.data.util.Constants.Companion.UNABLE_TO_CONNECT
//import com.jeklov.superfinancer.data.util.Resource
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.SharedFlow
//import kotlinx.coroutines.launch
//import okio.IOException
//import retrofit2.Response
//
//class TickerViewModel(
//    app: Application,
//    val tickerRepository: TickerRepository
//) : AndroidViewModel(app) {
//
//    private val _counter = MutableSharedFlow<Int>()
//    val counter: SharedFlow<Int> = _counter
//
//    private var currentValue = 60
//    private var isCounting = false
//
//    // LiveData all Ticker
//    val allTicker: MutableLiveData<Resource<TickerResponse>> = MutableLiveData()
//    var allTickerPages = 1
//    var allTickerResponse: TickerResponse? = null
//
//    val searchTicker: MutableLiveData<Resource<TickerResponse>> = MutableLiveData()
//    var searchTickerPages = 1
//    var searchTickerResponse: TickerResponse? = null
//    var newSearchQuery: String? = null
//    var oldSearchQuery: String? = null
//
//    init {
//        getAllTicker()
//    }
//
//    fun getAllTicker() = viewModelScope.launch {
//        allTickerInternet()
//    }
//
//    fun searchTicker(searchQuery: String) = viewModelScope.launch {
//        searchTickerInternet(searchQuery)
//    }
//
//    private fun allTickerResponse(response: Response<TickerResponse>): Resource<TickerResponse> {
//        if (response.isSuccessful) {
//            response.body()?.let { resultResponse ->
//                allTickerPages++
//                if (allTickerResponse == null) {
//                    allTickerResponse = resultResponse
//                } else {
//                    val oldTicker = resultResponse.results
//                    val newTicker = resultResponse.results
//                    oldTicker.addAll(newTicker)
//                }
//                return Resource.Success(allTickerResponse ?: resultResponse)
//            }
//        }
//        return Resource.Error(response.message())
//    }
//
//    private fun handleSearchTickerResponse(response: Response<TickerResponse>): Resource<TickerResponse> {
//        if (response.isSuccessful) {
//            response.body()?.let { resultResponse ->
//                if (searchTickerResponse == null || newSearchQuery != oldSearchQuery) {
//                    searchTickerPages = 1
//                    oldSearchQuery = newSearchQuery
//                    searchTickerResponse = resultResponse
//                } else {
//                    allTickerPages++
//                    val oldTicker = searchTickerResponse?.results
//                    val newTicker = resultResponse.results
//                    oldTicker?.addAll(newTicker)
//                }
//                return Resource.Success(allTickerResponse ?: resultResponse)
//            }
//        }
//        return Resource.Error(response.message())
//    }
//
//    fun addToFavorites(ticker: Ticker) = viewModelScope.launch {
//        tickerRepository.upset(ticker)
//    }
//
//    fun getFavoriteTicker() = tickerRepository.getFavoriteTicker()
//
//    fun deleteTicker(ticker: Ticker) = viewModelScope.launch {
//        tickerRepository.deleteTicker(ticker)
//    }
//
//    fun internetConnection(context: Context): Boolean {
//        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
//            return getNetworkCapabilities(activeNetwork)?.run {
//                when {
//                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                    else -> false
//                }
//            } ?: false
//        }
//    }
//
//    private suspend fun allTickerInternet() {
//        allTicker.postValue(Resource.Loading())
//        try {
//            if (internetConnection((this.getApplication()))) {
//                val response = tickerRepository.getTicker(
//                    limitNumber = 20,
//                    offsetNumber = 0
//                )
//                allTicker.postValue(allTickerResponse(response))
//            } else {
//                allTicker.postValue(Resource.Error(NO_INTERNET_CONNECTION))
//            }
//        } catch (t: Throwable) {
//            when(t) {
//                is IOException -> allTicker.postValue(Resource.Error(UNABLE_TO_CONNECT))
//                else -> allTicker.postValue(Resource.Error(NO_SIGNAL))
//            }
//        }
//    }
//
//    private suspend fun searchTickerInternet(searchQuery: String) {
//        newSearchQuery = searchQuery
//        searchTicker.postValue(Resource.Loading())
//        try {
//            if (internetConnection((this.getApplication()))) {
//                val response = tickerRepository.searchTicker(searchQuery, searchTickerPages)
//                searchTicker.postValue(handleSearchTickerResponse(response))
//            } else {
//                searchTicker.postValue(Resource.Error(NO_INTERNET_CONNECTION))
//            }
//        } catch (t: Throwable) {
//            when(t) {
//                is IOException -> searchTicker.postValue(Resource.Error(UNABLE_TO_CONNECT))
//                else -> searchTicker.postValue(Resource.Error(NO_SIGNAL))
//            }
//        }
//    }
//
//    fun startCounter() {
//        if (!isCounting) {
//            isCounting = true
//            viewModelScope.launch {
//                while (currentValue > 0) {
//                    delay(1000)
//                    currentValue--
//                    _counter.emit(currentValue) // Emit the updated value
//                }
//                isCounting = false
//                getAllTicker() // Call a function to reset the counter or perform another action
//                currentValue = 60
//                currentValue = 60
//            }
//        }
//    }
//}