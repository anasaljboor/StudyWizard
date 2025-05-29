package com.example.studywizard.Cohere_ML

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await

class CohereViewModel : ViewModel() {

    var summaryOutput by mutableStateOf<String?>(null)
    var flashcardsOutput by mutableStateOf<String?>(null)
    private val _quizOutputState = MutableStateFlow<String?>(null)
    val quizOutputState = _quizOutputState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    var generatedOutput by mutableStateOf<String?>(null)




    private suspend fun extractText(context: Context, imageUri: Uri): String? {
        return recognizeTextFromImage(context, imageUri)
    }

    private suspend fun requestToCohere(prompt: String): String? {
        val response = CohereService.api.generate(
            GenerateRequest(prompt = prompt),
            auth = Constants.COHERE_API_KEY
        )
        if (!response.isSuccessful) {
            return "ERROR_${response.code()}"
        }
        return response.body()?.generations?.firstOrNull()?.text
    }




    fun generateSummary(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            val text = extractText(context, imageUri) ?: return@launch
            val prompt = buildSummaryPrompt(text)
            summaryOutput = requestToCohere(prompt)
        }
    }
    fun generateSummaryFromText(text: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val prompt = buildSummaryPrompt(text)
                val response = requestToCohere(prompt)
                summaryOutput = response ?: "EMPTY_RESPONSE"
            } catch (e: Exception) {
                summaryOutput = "EXCEPTION"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun generateFlashcards(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            val text = extractText(context, imageUri) ?: return@launch
            val prompt = buildFlashcardsPrompt(text)
            flashcardsOutput = requestToCohere(prompt)
        }
    }
    fun generateFlashcardsFromText(text: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val prompt = buildFlashcardsPrompt(text)
                val response = requestToCohere(prompt)
                flashcardsOutput = response ?: "EMPTY_RESPONSE"
            } catch (e: Exception) {
                flashcardsOutput = "EXCEPTION"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateQuizFromText(text: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val prompt = buildQuizPrompt(text)
                val response = requestToCohere(prompt)
                _quizOutputState.value = response ?: "EMPTY_RESPONSE"
            } catch (e: Exception) {
                _quizOutputState.value = "EXCEPTION"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun generateQuiz(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _quizOutputState.value = "" // Clear state to ensure update
            val text = extractText(context, imageUri) ?: return@launch
            val prompt = buildQuizPrompt(text)
            val response = requestToCohere(prompt)
            _quizOutputState.value = response ?: "ERROR"
        }
    }




    // You can define custom prompt builders if needed
    private fun buildQuizPrompt(text: String): String {
        return """
        Based on the following content, generate 3 multiple-choice quiz questions.
        Each question should be followed by choices A, B, C, D and clearly state the correct answer at the end.
        
        Content:
        "$text"
    """.trimIndent()
    }

    private fun buildSummaryPrompt(text: String): String {
        return """
            Summarize the following content:
            
            "$text"
        """.trimIndent()
    }

    private fun buildFlashcardsPrompt(text: String): String {
        return """
            Create 3 flashcards from the following text:
            
            "$text"
        """.trimIndent()

    }


    suspend fun recognizeTextFromImage(context: Context, uri: Uri): String? {
        return try {
            val image = InputImage.fromFilePath(context, uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val result = recognizer.process(image).await()
            result.text
        } catch (e: Exception) {
            Log.e("OCR", "Error: ${e.message}")
            null
        }
    }

}


