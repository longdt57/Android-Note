package com.app.androidcompose.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.note.ui.NoteDestination
import com.example.note.ui.noteNavGraph

@Composable
fun AppNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        route = AppDestination.RootNavGraph.route,
        startDestination = NoteDestination.NoteRoot.destination
    ) {
        noteNavGraph(navController = navController)
    }
}
