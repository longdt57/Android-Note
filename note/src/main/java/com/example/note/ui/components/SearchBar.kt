package com.example.note.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.R
import leegroup.module.compose.ui.theme.ComposeTheme

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = { onQueryChanged(it) },
        placeholder = { Text(text = stringResource(R.string.search_hint)) },
        leadingIcon = {
            IconButton(onClick = { /* Handle search icon click */ }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            focusedIndicatorColor = Color.Transparent, // Remove the focused underline
            unfocusedIndicatorColor = Color.Transparent, // Remove the unfocused underline
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarEmptyPreview() {
    var query by remember { mutableStateOf("") }
    ComposeTheme {
        SearchBar(
            query = query,
            onQueryChanged = { query = it },
            onClear = { query = "" },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    var query by remember { mutableStateOf("Nature") }
    ComposeTheme {
        SearchBar(
            query = query,
            onQueryChanged = { query = it },
            onClear = { query = "" },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
