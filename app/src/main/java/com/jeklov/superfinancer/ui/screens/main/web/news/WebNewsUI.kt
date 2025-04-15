package com.jeklov.superfinancer.ui.screens.main.web.news

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.jeklov.superfinancer.data.util.Constants.Companion.UNREAL_URL_DIVIDER

@Composable
fun WebNewsUI(
    paddingValues: PaddingValues,
    url: String?
) {

    Box(
        Modifier
            .fillMaxWidth()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        val realUrl = url
            ?.replace(UNREAL_URL_DIVIDER, "/")
            //?.replace("http://", "https://") // try to https reconnect
        if(realUrl != null){
            WebViewScreen(url = realUrl)
        } else {
            WebViewScreen(url = "https://www.example.com")
        }

    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String) {
    val context = LocalContext.current

    AndroidView(factory = {
        WebView(context).apply {
            webViewClient = WebViewClient() // Устанавливаем WebViewClient для обработки ссылок
            settings.javaScriptEnabled = true // Включаем JavaScript, если необходимо
            loadUrl(url) // Загружаем URL
        }
    })
}