package com.jeklov.superfinancer.ui.screens.menu

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
//import com.jeklov.post.ui.screens.feed.PostMainUI
import com.jeklov.superfinancer.MainActivity
import com.jeklov.superfinancer.data.db.MainDB
import com.jeklov.superfinancer.ui.screens.Screens
import com.jeklov.superfinancer.ui.screens.feed.PostCreateScreen
import com.jeklov.superfinancer.ui.screens.feed.PostListScreenUI
import com.jeklov.superfinancer.ui.screens.finance.FinanceUI
import com.jeklov.superfinancer.ui.screens.main.MainUI
import com.jeklov.superfinancer.ui.screens.main.search.SearchUI
import com.jeklov.superfinancer.ui.screens.main.web.news.WebNewsUI
import com.jeklov.superfinancer.ui.screens.top.TopUI

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavMenuUI(
    database: MainDB, context: MainActivity, application: Application
) {

    val navigationController = rememberNavController()

    val screenItemsBar = listOf(
        Screens.Main, Screens.Search, Screens.Finance, Screens.WebNews, Screens.Feed, Screens.Top
    )

    // State of topBar, set state to false, if current page showTopBar = false
    val topBarState = rememberSaveable { (mutableStateOf(true)) }

    // State of bottomBar, set state to false, if current page showBottomBar = false
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }


    // Some logic for hide bottom menu in some pages (Screens.SomePage.showBottomBar)
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    screenItemsBar.forEach { screens ->

        // Find real address without transmitting data
        val route = navBackStackEntry?.destination?.route
        val index = route?.indexOf("/")

        val realRoute =
            if (index == -1) route else navBackStackEntry?.destination?.route?.substring(
                0, index!!
            )
        if (realRoute == screens.screen) {
            topBarState.value = screens.showTopBar
            bottomBarState.value = screens.showBottomBar
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarUI(topBarState)
        },
        bottomBar = {
            BottomBarUI(navigationController, screenItemsBar, bottomBarState)
        }
    ) { paddingValues -> // padding of top bar and bottom bar
        NavHost(
            navController = navigationController, startDestination = Screens.Main.screen
        ) {
            composable(Screens.Feed.screen) {
//                PostListScreenUI(
//                    paddingValues,
//                    navigationController,
//                    context
//                )
            }
            composable(Screens.Main.screen) {
                MainUI(paddingValues, navigationController, context, application)
            }
            composable(Screens.Search.screen) {
                SearchUI(paddingValues = paddingValues, navigationController, context, application)
            }

            composable(Screens.Finance.screen) {
                FinanceUI(
                    paddingValues = paddingValues,
                    database,
                    context
                )
            }
            composable(Screens.Top.screen) {
                TopUI(paddingValues)
            }
            composable(Screens.WebNews.screen + "/{url}") {
                var url: String? = null
                try {
                    url = it.arguments?.getString("url")
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
                WebNewsUI(
                    paddingValues = paddingValues,
                    url = url
                )
            }
            composable(Screens.PostCreate.screen) {
                PostCreateScreen(
                    availableTags = listOf("tag1", "tag2", "tag3"), context
                )
            }
        }
    }
}