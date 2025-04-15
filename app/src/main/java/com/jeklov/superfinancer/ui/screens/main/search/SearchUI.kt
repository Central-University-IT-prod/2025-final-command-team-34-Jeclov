package com.jeklov.superfinancer.ui.screens.main.search

import android.app.Application
import android.graphics.drawable.Drawable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jeklov.superfinancer.MainActivity
import com.jeklov.superfinancer.R
import com.jeklov.superfinancer.data.db.MainDB
import com.jeklov.superfinancer.data.models.news.NewsResponse
import com.jeklov.superfinancer.data.repository.NewsRepository
import com.jeklov.superfinancer.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.jeklov.superfinancer.data.util.Constants.Companion.NO_SIGNAL
import com.jeklov.superfinancer.data.util.Constants.Companion.TOO_MANY_REQUESTS
import com.jeklov.superfinancer.data.util.Constants.Companion.UNABLE_TO_CONNECT
import com.jeklov.superfinancer.data.util.Resource
import com.jeklov.superfinancer.data.view.model.NewsViewModel
import com.jeklov.superfinancer.data.view.model.NewsViewModelProviderFactory
import com.jeklov.superfinancer.ui.screens.main.NewsItemView
import com.jeklov.superfinancer.ui.theme.BlueLT

@Composable
fun SearchUI(
    paddingValues: PaddingValues,
    navigationController: NavHostController,
    context: MainActivity,
    application: Application
) {

    var text by remember { mutableStateOf("") }

    val newsRepository = NewsRepository(MainDB.getInstance(context))
    val viewModel: NewsViewModel =
        viewModel(factory = NewsViewModelProviderFactory(application, newsRepository))
    val newsSearchData by viewModel.searchNews.observeAsState(Resource.Loading())

    val backgroundDrawable: Drawable =
        context.resources.getDrawable(R.drawable.box_frame_search) // Замените R.drawable.background на ваш xml drawable

    val counterValue =
        remember { mutableStateOf(-1) }
    // Initial state -1, because we find real nome in viewModel.
    // And while we wait (while -1) we show little CircularProgressIndicator instead of a number.
    // We use viewmodel because we want the user to be able to navigate pages while waiting and the counter not to reset

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paint(
                        painter = painterResource(R.drawable.box_frame_search),
                        contentScale = ContentScale.FillBounds
                    )
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navigationController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                val focusManager = LocalFocusManager.current
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.width(200.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.searchNews(text)
                            focusManager.clearFocus()
                        }
                    ),
                )
                IconButton(onClick = { viewModel.searchNews(text) }) {
                    Icon(
                        imageVector = Icons.Filled.FindReplace,
                        contentDescription = "Find"
                    )
                }
            }
            NewsList(newsSearchData, viewModel, counterValue, navigationController)
        }
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