package com.jeklov.superfinancer.ui.screens.main

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jeklov.superfinancer.MainActivity
import com.jeklov.superfinancer.R
import com.jeklov.superfinancer.data.db.MainDB
import com.jeklov.superfinancer.data.models.news.News
import com.jeklov.superfinancer.data.models.news.NewsResponse
import com.jeklov.superfinancer.data.repository.NewsRepository
import com.jeklov.superfinancer.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.jeklov.superfinancer.data.util.Constants.Companion.NO_SIGNAL
import com.jeklov.superfinancer.data.util.Constants.Companion.TOO_MANY_REQUESTS
import com.jeklov.superfinancer.data.util.Constants.Companion.UNABLE_TO_CONNECT
import com.jeklov.superfinancer.data.util.Constants.Companion.UNREAL_URL_DIVIDER
import com.jeklov.superfinancer.data.util.Resource
import com.jeklov.superfinancer.data.view.model.NewsViewModel
import com.jeklov.superfinancer.data.view.model.NewsViewModelProviderFactory
import com.jeklov.superfinancer.ui.screens.Screens
import com.jeklov.superfinancer.ui.theme.BlueLT
import kotlinx.coroutines.flow.collectLatest


@Suppress("DEPRECATION")
@Composable
fun MainUI(
    paddingValues: PaddingValues,
    navigationController: NavHostController,
    context: MainActivity,
    application: Application
) {
    val newsRepository = NewsRepository(MainDB.getInstance(context))
    val viewModel: NewsViewModel =
        viewModel(factory = NewsViewModelProviderFactory(application, newsRepository))
    val newsData by viewModel.allNews.observeAsState(Resource.Loading())

    var isRefreshing by remember { mutableStateOf(false) }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val counterValue =
        remember { mutableStateOf(-1) }
    // Initial state -1, because we find real nome in viewModel.
    // And while we wait (while -1) we show little CircularProgressIndicator instead of a number.
    // We use viewmodel because we want the user to be able to navigate pages while waiting and the counter not to reset

    LaunchedEffect(key1 = true) {
        viewModel.counter.collectLatest { newValue ->
            counterValue.value = newValue
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(navigationController)

            SwipeRefresh(modifier = Modifier.fillMaxSize(), state = swipeRefreshState, onRefresh = {
                isRefreshing = true
                viewModel.getAllNews() // updating data
                isRefreshing = false
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TickerList()
                    NewsList(newsData, viewModel, counterValue, navigationController)
                }
            }
        }
    }
}

@Composable
fun SearchBar(navigationController: NavHostController) {
    Box(modifier = Modifier.clickable {
        navigationController.navigate(Screens.Search.screen) {
            launchSingleTop = true
            restoreState = true
        }
    }) {
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Поиск...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            trailingIcon = {
                IconButton(onClick = {
                    navigationController.navigate(Screens.Search.screen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Icon(Icons.Default.FindReplace, "Find")
                }
            }
        )
    }
}

@Composable
fun TickerList() {
    Text(text = "тиккеры будут здесь")
}

@Composable
fun NewsItemView(news: News, navigationController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navigationController.navigate(
                    Screens.WebNews.screen + "/" + news.url.replace(
                        "/",
                        UNREAL_URL_DIVIDER
                    )
                ) {
                    launchSingleTop = true
                    restoreState = true
                }
            }) {
        Text(text = news.title, fontWeight = FontWeight.Bold)
        Text(text = news.section)
        Text(text = news.url)
    }
}


@Composable
fun NewsList(
    newsData: Resource<NewsResponse>,
    viewModel: NewsViewModel,
    counterValue: MutableState<Int>,
    navigationController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (newsData) {
            is Resource.Loading<*> -> {
                CircularProgressIndicator(color = BlueLT)
            }

            is Resource.Success<*> -> {
                val newsResponse = (newsData as Resource.Success<NewsResponse>).data
                newsResponse?.results?.let { newsList ->
                    LazyColumn {
                        items(newsList) { newsItem ->
                            NewsItemView(newsItem, navigationController)
                        }
                    }
                } ?: run {
                    Text(stringResource(R.string.no_news))
                }
            }

            is Resource.Error<*> -> {
                val errorMessage = (newsData as Resource.Error<NewsResponse>).message
                when (errorMessage) {
                    NO_INTERNET_CONNECTION -> {
                        Text(
                            text = stringResource(R.string.no_internet_connection),
                            color = Color.Red
                        )
                        viewModel.getAllNews()
                    }

                    UNABLE_TO_CONNECT -> {
                        Text(text = stringResource(R.string.unable_to_connect), color = Color.Red)
                        viewModel.getAllNews()
                    }

                    NO_SIGNAL -> {
                        Text(text = stringResource(R.string.no_signal), color = Color.Red)
                        viewModel.getAllNews()
                    }

                    TOO_MANY_REQUESTS -> {
                        Column {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(stringResource(R.string.so_many_requests))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(stringResource(R.string.try_again_after) + if (counterValue.value != -1) counterValue.value else "")
                                    if (counterValue.value == -1) {
                                        CircularProgressIndicator(
                                            color = BlueLT,
                                            modifier = Modifier.size(
                                                15.dp
                                            )
                                        )
                                    }
                                    viewModel.startCounter()
                                }
                            }
                        }
                    }

                    else -> {
                        Text(
                            text = errorMessage ?: stringResource(R.string.error_has_occurred),
                            color = Color.Red
                        )
                        viewModel.getAllNews()
                    }
                }
            }
        }
    }
}