package com.example.note.support.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DatetimeUtil {

    private const val NOTE_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun formatDateTime(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        val format = SimpleDateFormat(NOTE_DATE_TIME_FORMAT, locale)
        return format.format(Date(timestamp))
    }
}