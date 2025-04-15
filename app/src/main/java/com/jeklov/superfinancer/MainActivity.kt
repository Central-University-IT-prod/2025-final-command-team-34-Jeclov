package com.jeklov.superfinancer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jeklov.superfinancer.data.db.MainDB
import com.jeklov.superfinancer.ui.screens.menu.NavMenuUI
import com.jeklov.superfinancer.ui.theme.SuperFinancerTheme

class MainActivity : ComponentActivity() {
    private lateinit var dataBase: MainDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dataBase = MainDB.getInstance(this)
        setContent {
            SuperFinancerTheme {
                NavMenuUI(dataBase, this, application)
            }
        }
    }
}