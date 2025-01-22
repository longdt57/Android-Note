package com.example.note.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

class FireStoreNoteManager @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()

    private val userId = "user123" // Sample user Id

    suspend fun addNoteToFireStore(note: FireStoreNote): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val noteMap = note.createHashMap()

            firestore.collection(USER_COLLECTION)
                .document(userId)
                .collection(NOTE_COLLECTION)
                .document(note.id.toString()) // Use the note ID as the document ID
                .set(noteMap) // Use `set` instead of `add` to specify the document ID
                .addOnSuccessListener {
                    Timber.d("Note added with ID: ${note.id}")
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    Timber.w(e, "Error adding note")
                    continuation.resume(false)
                }
        }
    }

    suspend fun getNotesFromFireStore(): List<FireStoreNote> {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection(USER_COLLECTION)
                .document(userId)
                .collection(NOTE_COLLECTION)
                .get()
                .addOnSuccessListener { result ->
                    val notes = result.map { document ->
                        document.toObject(FireStoreNote::class.java)
                    }
                    continuation.resume(notes)
                }
                .addOnFailureListener { e ->
                    continuation.resume(emptyList())
                    Timber.w(e, "Error getting documents.")
                }
        }
    }

    suspend fun updateNoteInFireStore(note: FireStoreNote): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val noteUpdate = note.createHashMap()

            firestore.collection(USER_COLLECTION)
                .document(userId)
                .collection(NOTE_COLLECTION)
                .document(note.id.toString())
                .update(noteUpdate as Map<String, Any>)
                .addOnSuccessListener {
                    Timber.d("Note with ID ${note.id} updated successfully for user $userId")
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    Timber.e(e, "Error updating note with ID ${note.id} for user $userId")
                    continuation.resume(false)
                }
        }
    }

    suspend fun deleteNoteFromFireStore(noteId: Long): Boolean {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection(USER_COLLECTION)
                .document(userId)
                .collection(NOTE_COLLECTION)
                .document(noteId.toString())
                .delete()
                .addOnSuccessListener {
                    Timber.d("Note deleted successfully")
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    Timber.w(e, "Error deleting note")
                    continuation.resume(false)
                }
        }
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val NOTE_COLLECTION = "notes"
    }
}
