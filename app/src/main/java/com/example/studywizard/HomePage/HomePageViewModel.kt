package com.example.studywizard.HomePage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class HomePageViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    private val _topics = MutableLiveData<List<String>>()
    val topics: LiveData<List<String>> get() = _topics

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchTopics(uid: String) {
        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val topicsList = doc.get("topics") as? List<*> // get the raw list
                val stringList = topicsList?.mapNotNull { it as? String } ?: emptyList()
                _topics.value = stringList
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message
            }
    }
}
