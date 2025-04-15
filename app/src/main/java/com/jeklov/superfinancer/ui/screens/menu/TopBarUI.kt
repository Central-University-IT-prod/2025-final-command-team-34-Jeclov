package com.jeklov.superfinancer.ui.screens.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeklov.superfinancer.R
import com.jeklov.superfinancer.data.api.RetrofitInstance
import com.jeklov.superfinancer.data.view.model.QuoteViewModel
import com.jeklov.superfinancer.ui.theme.BlueLT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUI(
    topBarState: MutableState<Boolean>,
) {
    //val viewModel: QuoteViewModel = viewModel()
    //var quote = viewModel.quote//.value
    //val call = RetrofitInstance.quoteAPI.getQuote()
    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            TopAppBar(
                title = {
                    Row {
                        Icon(painterResource(R.drawable.logo), stringResource(R.string.app_name))
                        Icon(painterResource(R.drawable.sf_name), stringResource(R.string.app_name))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BlueLT,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    )
}