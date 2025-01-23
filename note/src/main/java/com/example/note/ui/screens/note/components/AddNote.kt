package com.example.note.ui.screens.note.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.R
import leegroup.module.compose.ui.theme.ComposeTheme

@Composable
fun AddNoteInput(onAdd: (String) -> Unit, modifier: Modifier = Modifier) {
    var noteText by remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = noteText,
            onValueChange = { noteText = it },
            placeholder = { Text(stringResource(R.string.add_hint)) },
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                focusedIndicatorColor = Color.Transparent, // Remove the focused underline
                unfocusedIndicatorColor = Color.Transparent, // Remove the unfocused underline
            ),
            trailingIcon = {
                if (noteText.isNotEmpty()) {
                    IconButton(onClick = {
                        noteText = ""
                    }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
            },
        )
        Button(
            onClick = {
                if (noteText.isNotBlank()) {
                    onAdd(noteText)
                    noteText = ""
                }
            },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(stringResource(R.string.add))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AddNoteInputPreview() {
    ComposeTheme {
        AddNoteInput(onAdd = {})
    }
}
