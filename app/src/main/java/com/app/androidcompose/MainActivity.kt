package com.app.androidcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.app.androidcompose.ui.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import leegroup.module.compose.ui.theme.ComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                AppNavGraph(navController = rememberNavController())
            }
        }
    }
}
