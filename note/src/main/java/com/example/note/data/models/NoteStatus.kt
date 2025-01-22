package com.example.note.data.models

import androidx.annotation.StringDef
import com.example.note.data.models.NoteStatus.Companion.ADD
import com.example.note.data.models.NoteStatus.Companion.DELETE
import com.example.note.data.models.NoteStatus.Companion.UPDATE

@Retention(AnnotationRetention.SOURCE)
@StringDef(ADD, UPDATE, DELETE)
annotation class NoteStatus {
    companion object {
        const val ADD = "add"
        const val UPDATE = "update"
        const val DELETE = "delete"
    }
}