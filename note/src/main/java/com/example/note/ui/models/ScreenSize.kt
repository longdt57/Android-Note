package com.example.note.ui.models

@Suppress("MagicNumber")
enum class ScreenSize(val columnCount: Int) {
    SMALL(1),    // Small devices (e.g., phones in portrait mode)
    MEDIUM(2),   // Medium devices (e.g., tablets or phones in landscape mode)
    LARGE(3);    // Large devices (e.g., large tablets or wide phones)

    companion object {
        fun fromWidth(widthDp: Int): ScreenSize {
            return when {
                widthDp <= 600 -> SMALL
                widthDp <= 900 -> MEDIUM
                else -> LARGE
            }
        }
    }
}