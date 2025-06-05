package com.example.studywizard.QuizGen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studywizard.Cohere_ML.CohereViewModel
import com.example.studywizard.HomePage.HomePageViewModel
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.utils.HistoryUtils

@Composable
fun QuizScreen(

    viewModel: CohereViewModel = viewModel(),
    navController: NavController,
    authViewModel: AuthViewModel,
    homePageViewModel: HomePageViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    var inputText by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val quizOutput by viewModel.quizOutputState.collectAsState()

    val parsedQuestions = quizOutput?.let { parseQuizOutput(it) } ?: emptyList()
    val userAnswers = remember { mutableStateMapOf<Int, String>() }
    val feedbackShown = remember { mutableStateMapOf<Int, Boolean>() }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.generateQuiz(context, it) }
        }
    )

    // Save generated quiz questions to user history when quizOutput changes
    LaunchedEffect(quizOutput) {
        val uid = authViewModel.currentUser?.uid
        if (uid != null && parsedQuestions.isNotEmpty()) {
            parsedQuestions.forEach { question ->
                // Save each question text as a history item
                HistoryUtils.addHistoryItem(uid, "Quiz question: ${question.text}")
            }
        }
        userAnswers.clear()
        feedbackShown.clear()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Create a New Quiz", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter a topic or paste in notes...") }
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

                Button(onClick = { imagePicker.launch("image/*") }) {
                    Text("Upload Image")
                }
            }
        }

        when {
            isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Generating quiz...")
                }
            }

            parsedQuestions.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    itemsIndexed(parsedQuestions) { index, question ->
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
