package com.example.note.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.note.ui.models.NoteUiModel
import com.example.note.ui.screens.note.NoteScreen
import com.example.note.ui.screens.notedetail.NoteDetailScreen
import leegroup.module.compose.support.extensions.appNavigate
import leegroup.module.compose.support.extensions.composable

fun NavGraphBuilder.noteNavGraph(
    navController: NavHostController,
) {

    navigation(
        route = NoteDestination.NoteRoot.route,
        startDestination = NoteDestination.NoteScreen.destination
    ) {
        composable(NoteDestination.NoteScreen) {
            NoteScreen(
                navigator = { destination -> navController.appNavigate(destination) }
            )
        }
        composable<NoteUiModel> { entry ->
            val model = entry.toRoute<NoteUiModel>()
            NoteDetailScreen(
                content = model.content,
                navigator = { destination ->
                    navController.appNavigate(destination)
                }
            )
        }
    }
}
