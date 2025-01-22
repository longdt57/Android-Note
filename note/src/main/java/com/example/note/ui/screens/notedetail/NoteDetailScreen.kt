package com.example.note.ui.screens.notedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.ui.screens.notedetail.components.NoteDetailAppBar
import leegroup.module.compose.ui.models.BaseDestination

@Composable
fun NoteDetailScreen(
    content: String,
    navigator: (destination: Any) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        NoteDetailAppBar(onBack = { navigator(BaseDestination.Up()) })
        Text(content, modifier = Modifier.padding(24.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NoteDetailScreenPreview() {
    NoteDetailScreen("Note content", {})
}