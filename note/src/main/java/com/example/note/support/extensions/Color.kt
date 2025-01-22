package com.example.note.support.extensions

import androidx.compose.ui.graphics.Color

@Suppress("MagicNumber")
fun randomColor() = Color((0..255).random(), (0..255).random(), (0..255).random())