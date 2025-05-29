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
    val isLoading by viewModel.isLoading.collectAsState()
    val quizOutput by viewModel.quizOutputState.collectAsState()

    val parsedQuestions = quizOutput?.let { parseQuizOutput(it) } ?: emptyList()
    val userAnswers = remember { mutableStateMapOf<Int, String>() }
    val feedbackShown = remember { mutableStateMapOf<Int, Boolean>() }

    // NEW: Image picker for uploading content
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                viewModel.generateQuiz(context, it)
            }
        }
    )

    LaunchedEffect(quizOutput) {
        userAnswers.clear()
        feedbackShown.clear()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Quiz Generator", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter text to generate quiz questions...") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (inputText.isNotBlank()) {
                    viewModel.generateQuizFromText(inputText)
                    inputText = ""
                }
            }) {
                Text("Generate from Text")
            }

            Button(onClick = {
                imagePicker.launch("image/*")
            }) {
                Text("Upload Image")
            }
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
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
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
            }
        }
    }
}