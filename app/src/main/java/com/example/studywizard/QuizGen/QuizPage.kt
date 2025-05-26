package com.example.studywizard.QuizGen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studywizard.Cohere_ML.CohereViewModel

@Composable
fun QuizScreen(
    viewModel: CohereViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    var inputText by remember { mutableStateOf("") }
    val quizOutput by viewModel.quizOutputState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val parsedQuestions = quizOutput?.let { parseQuizOutput(it) } ?: emptyList()
    val userAnswers = remember { mutableStateMapOf<Int, String>() }
    val feedbackShown = remember { mutableStateMapOf<Int, Boolean>() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Quiz Generator", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your text here") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (inputText.isNotBlank()) {
                viewModel.generateQuizFromText(inputText)
                inputText = ""
            }
        }) {
            Text("Generate Quiz")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Generating quiz...")
                }
            }

            parsedQuestions.isNotEmpty() -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(parsedQuestions.size) { index ->
                        val question = parsedQuestions[index]
                        Column {
                            Text(
                                "${index + 1}. ${question.text}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            question.choices.forEach { (key, value) ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = userAnswers[index] == key,
                                        onClick = {
                                            userAnswers[index] = key
                                            feedbackShown[index] = true
                                        }
                                    )
                                    Text("$key. $value")
                                }
                            }

                            if (feedbackShown[index] == true) {
                                val correct = userAnswers[index] == question.correctAnswer
                                Text(
                                    if (correct) "✅ Correct!"
                                    else "❌ Incorrect. Answer: ${question.correctAnswer}",
                                    color = if (correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            quizOutput == "EXCEPTION" -> {
                Text("❌ Error communicating with Cohere.", color = MaterialTheme.colorScheme.error)
            }

            quizOutput == "EMPTY_RESPONSE" -> {
                Text("⚠️ Cohere returned an empty response.")
            }

            quizOutput?.startsWith("ERROR_") == true -> {
                Text("❌ API error: ${quizOutput}", color = MaterialTheme.colorScheme.error)
            }

            else -> {
                Text("Submit text to generate a quiz.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

}

        // Input Controls
        /*Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                Icon(Icons.Default.Image, contentDescription = "Upload Image")
            }
            IconButton(onClick = { /* TODO: Add Camera */ }) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Take Photo")
            }
            IconButton(onClick = { filePickerLauncher.launch("*/*") }) {
                Icon(Icons.Default.UploadFile, contentDescription = "Upload File")
            }

            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter a question...") }
            )

            IconButton(onClick = {
                if (inputText.isNotBlank()) {
                    viewModel.generateQuizFromText(inputText)
                    inputText = ""
                } else if (selectedImageUri != null) {
                    viewModel.generateQuiz(context, selectedImageUri!!)
                    selectedImageUri = null
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}
*/