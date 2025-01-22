package com.example.note.domain.param

data class GetNoteParam(
    val query: String,
    val limit: Int,
    val fromDate: Long
)