package com.jeklov.post.ui.screens

import androidx.annotation.DrawableRes
import com.jeklov.event.R

sealed class PostScreens(
    val titleRes: Int,
    val screen: String,
    @DrawableRes val iconRes: Int? = null,
    val showIconOnBottomBar: Boolean = true,
    val showTopBar: Boolean = true,
    val showBottomBar: Boolean = true
) {
    data object PostMain : PostScreens(
        titleRes = R.string.news_feed,
        screen = "main_feed_screen",
        iconRes = R.drawable.feed
    )

    data object PostCreate : PostScreens(
        titleRes = R.string.create_post,
        screen = "create_post_screen",
        iconRes = R.drawable.feed
    )
}