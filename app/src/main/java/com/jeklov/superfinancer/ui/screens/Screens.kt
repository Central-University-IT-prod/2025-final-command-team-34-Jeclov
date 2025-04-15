package com.jeklov.superfinancer.ui.screens

import androidx.annotation.DrawableRes
import com.jeklov.post.ui.screens.PostScreens
import com.jeklov.superfinancer.R

sealed class Screens(
    val titleRes: Int,
    val screen: String,
    @DrawableRes val iconRes: Int? = null,
    val showIconOnBottomBar: Boolean = true,
    val showTopBar: Boolean = true,
    val showBottomBar: Boolean = true
) {
    data object Main : Screens(
        titleRes = R.string.page_name_main,
        screen = "main_screen",
        iconRes = R.drawable.home
    )

    data object Search : Screens(
        titleRes = R.string.page_name_search,
        screen = "search_screen",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )

    data object Finance : Screens(
        titleRes = R.string.page_name_finance,
        screen = "finance_screen",
        iconRes = R.drawable.money
    )

    data object Feed : Screens(
        titleRes = R.string.page_name_finance,
        screen = "feed_screen",
        iconRes = com.jeklov.event.R.drawable.feed
    )

    data object WebNews : Screens(
        titleRes = R.string.page_web_news,
        screen = "web_news",
        iconRes = R.drawable.icon_feed_black,
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )
    data object Top : Screens(
        titleRes = R.string.page_web_news,
        screen = "web_news",
        iconRes = R.drawable.top,
    )

    data object PostMain : Screens(
        titleRes = com.jeklov.event.R.string.news_feed,
        screen = "main_feed_screen",
        iconRes = com.jeklov.event.R.drawable.feed
    )

    data object PostCreate : Screens(
        titleRes = com.jeklov.event.R.string.create_post,
        screen = "create_post_screen",
        iconRes = com.jeklov.event.R.drawable.feed
    )
}