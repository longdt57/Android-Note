package leegroup.module.compose.support.extensions

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

fun randomString(words: Int): String = LoremIpsum(words).values.joinToString()