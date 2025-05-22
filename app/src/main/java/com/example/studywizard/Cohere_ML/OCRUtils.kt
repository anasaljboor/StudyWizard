package com.example.studywizard.Cohere_ML

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await

suspend fun recognizeTextFromImage(context: Context, imageUri: Uri): String? {
    return try {
        val image = InputImage.fromFilePath(context, imageUri)

        // ✅ Working version
        val recognizer = TextRecognition.getClient(
            TextRecognizerOptions.Builder().build()
        )

        val result = recognizer.process(image).await()
        result.text
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
