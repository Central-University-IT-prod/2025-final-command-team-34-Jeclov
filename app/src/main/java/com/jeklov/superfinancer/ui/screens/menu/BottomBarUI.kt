package com.jeklov.superfinancer.ui.screens.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jeklov.superfinancer.ui.screens.Screens
import com.jeklov.superfinancer.ui.theme.BlueLT

@Composable
fun BottomBarUI(
    navigationController: NavHostController,
    screenItemsBar: List<Screens>,
    bottomBarState: MutableState<Boolean>
) {
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomAppBar(containerColor = BlueLT) {
                screenItemsBar.forEach { screens ->
                    if (screens.showIconOnBottomBar && screens.iconRes != null) {
                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                navigationController.navigate(screens.screen) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    modifier = Modifier
                                        .size(if (currentDestination?.route == screens.screen)  30.dp else 20.dp),
                                    painter = painterResource(screens.iconRes),
                                    contentDescription = stringResource(screens.titleRes)
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}