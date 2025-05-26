package com.example.studywizard.QuizGen

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studywizard.Cohere_ML.CohereViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.TextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.material3.RadioButton

@Composable
fun QuizScreen(
    viewModel: CohereViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    var inputText by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val quizOutput = viewModel.quizOutput

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedImageUri = uri }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedImageUri = uri }

    // Parsed list of questions
    val parsedQuestions = remember(quizOutput) {
        quizOutput?.let { parseQuizOutput(it) } ?: emptyList()
    }

    // Track user answers
    val userAnswers = remember { mutableStateMapOf<Int, String>() }
    val feedbackShown = remember { mutableStateMapOf<Int, Boolean>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Quiz Generator", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (quizOutput == null && (inputText.isNotBlank() || selectedImageUri != null)) {
            Text("Waiting for output...", style = MaterialTheme.typography.bodyMedium)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(parsedQuestions.size) { index ->
                val question = parsedQuestions[index]
                Column {
                    Text("${index + 1}. ${question.text}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    question.choices.forEach { choice ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = userAnswers[index] == choice.key,
                                onClick = {
                                    userAnswers[index] = choice.key
                                    feedbackShown[index] = true
                                }
                            )
                            Text("${choice.key}. ${choice.value}")
                        }
                    }

                    if (feedbackShown[index] == true) {
                        val isCorrect = userAnswers[index] == question.correctAnswer
                        Text(
                            text = if (isCorrect) "✅ Correct!" else "❌ Incorrect. Answer: ${question.correctAnswer}",
                            color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // Input Bar
        Row(
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


