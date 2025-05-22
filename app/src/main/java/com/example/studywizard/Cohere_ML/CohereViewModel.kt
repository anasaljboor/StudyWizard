package com.example.studywizard.Cohere_ML

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch

class CohereViewModel: ViewModel() {
    var generatedOutput: String? = null

    fun processImageAndGenerateAI(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            val text = recognizeTextFromImage(context, imageUri) ?: return@launch
            val prompt = """
                Summarize the following notes:
                $text

                Then generate 3 flashcards and 3 quiz questions.
            """.trimIndent()

            val response = CohereService.api.generate(
                GenerateRequest(prompt = prompt),
                auth = "Bearer JbAjvTbUEYwxf9inW6hT4AgqG2UqcLR3RmNPgmmV"
            )

            generatedOutput = response.body()?.generations?.firstOrNull()?.text
            // Now you can update UI using State or LiveData
        }
    }
}