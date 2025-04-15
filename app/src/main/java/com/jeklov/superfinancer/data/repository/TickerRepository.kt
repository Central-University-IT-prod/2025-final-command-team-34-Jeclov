//package com.jeklov.superfinancer.data.repository
//
//import com.jeklov.superfinancer.data.api.RetrofitInstance
//import com.jeklov.superfinancer.data.db.MainDB
//import com.jeklov.superfinancer.data.models.Ticker
//
//class TickerRepository(val db: MainDB) {
//    suspend fun getTicker(limitNumber: Int, offsetNumber: Int) =
//        RetrofitInstance.tickerAPI.getTicker(
//            limitNumber = limitNumber,
//            offsetNumber = offsetNumber
//        )
//    suspend fun searchTicker(searchQuery: String, pageNumber: Int) =
//        RetrofitInstance.tickerAPI.searchForTicker(
//            pageNumber = pageNumber,
//            q = searchQuery
//        )
//    suspend fun upset(ticker: Ticker) = db.tickerDao().insertTickerItem(ticker)
//
//    fun getFavoriteTicker() = db.tickerDao().getAllTickerItems()
//
//    suspend fun deleteTicker(ticker: Ticker) = db.tickerDao().deleteTickerItem(ticker)
//}