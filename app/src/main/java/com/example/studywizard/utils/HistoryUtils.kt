package com.example.studywizard.utils

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.Timestamp
import java.util.*

object HistoryUtils {
    private val firestore = Firebase.firestore

    fun addHistoryItem(uid: String, question: String, onComplete: ((Boolean, String?) -> Unit)? = null) {
        val historyItem = hashMapOf(
            "question" to question,
            "timestamp" to Timestamp.now()
        )

        firestore.collection("users")
            .document(uid)
            .collection("history")
            .add(historyItem)
            .addOnSuccessListener {
                onComplete?.invoke(true, null)
            }
            .addOnFailureListener { e ->
                onComplete?.invoke(false, e.message)
            }
    }
}

