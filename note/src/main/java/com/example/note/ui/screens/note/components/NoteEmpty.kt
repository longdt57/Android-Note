package com.example.note.ui.screens.note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.note.R
import leegroup.module.compose.ui.theme.ComposeTheme

@Composable
fun NoteEmpty(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            stringResource(R.string.note_empty_text),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewNoteEmpty() {
    ComposeTheme {
        NoteEmpty(modifier = Modifier.fillMaxSize())
    }
}