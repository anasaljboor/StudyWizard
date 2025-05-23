package com.example.studywizard.Cohere_ML

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CohereViewModel : ViewModel() {

    var summaryOutput by mutableStateOf<String?>(null)
    var flashcardsOutput by mutableStateOf<String?>(null)
    var quizOutput by mutableStateOf<String?>(null)
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
            Log.e("Cohere", "Error: ${response.code()} - ${response.errorBody()?.string()}")
            return null
        }

        return response.body()?.generations?.firstOrNull()?.text
    }


    fun processImageAndGenerateAI(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            val text = extractText(context, imageUri) ?: return@launch
            val prompt = """
                Summarize the following notes:
                $text

                Then generate 3 flashcards and 3 quiz questions.
            """.trimIndent()

            generatedOutput = requestToCohere(prompt)
        }
    }

    fun generateSummary(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            val text = extractText(context, imageUri) ?: return@launch
            val prompt = buildSummaryPrompt(text)
            summaryOutput = requestToCohere(prompt)
        }
    }

    fun generateFlashcards(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            val text = extractText(context, imageUri) ?: return@launch
            val prompt = buildFlashcardsPrompt(text)
            flashcardsOutput = requestToCohere(prompt)
        }
    }

    fun generateQuiz(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            val text = extractText(context, imageUri) ?: return@launch
            val prompt = buildQuizPrompt(text)
            quizOutput = requestToCohere(prompt)
        }
    }
}
