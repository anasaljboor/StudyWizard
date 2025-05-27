package com.example.studywizard.HomePage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class HomePageViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    private val _topics = MutableLiveData<List<String>>()
    val topics: LiveData<List<String>> get() = _topics

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _history = MutableLiveData<List<String>>()  // history items as string questions
    val history: LiveData<List<String>> get() = _history

    private val _historyStatus = MutableLiveData<String>()
    val historyStatus: LiveData<String> = _historyStatus

    fun fetchTopics(uid: String) {
        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val topicsList = doc.get("topics") as? List<*>
                val stringList = topicsList?.mapNotNull { it as? String } ?: emptyList()
                _topics.value = stringList
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message
            }
    }

    fun fetchHistory(uid: String) {
        _error.value = null
        _history.value = emptyList() // Clear old data if you want

        firestore.collection("users")
            .document(uid)
            .collection("history")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val questions = result.mapNotNull { it.getString("question") }
                _history.value = questions
            }
            .addOnFailureListener { e ->
                _error.value = "Failed to load history: ${e.message}"
            }
    }

    fun addToHistory(uid: String?, question: String) {
        if (uid == null) {
            _historyStatus.value = "Error: User not logged in"
            return
        }

        val historyItem = hashMapOf(
            "question" to question,
            "timestamp" to Timestamp.now()
        )

        firestore.collection("users")
            .document(uid)
            .collection("history")
            .add(historyItem)
            .addOnSuccessListener {
                _historyStatus.value = "Question saved successfully"
            }
            .addOnFailureListener { e ->
                _historyStatus.value = "Failed to save question: ${e.message}"
            }
    }
}
