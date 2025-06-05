package com.example.studywizard.FlashCard

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studywizard.Cohere_ML.CohereViewModel
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.utils.HistoryUtils

@Composable
fun FlashcardsScreen(
    viewModel: CohereViewModel = viewModel(),
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context = LocalContext.current
) {
    var inputText by remember { mutableStateOf("") }
    val flashcardsOutput by remember { derivedStateOf { viewModel.flashcardsOutput } }
    val isLoading by viewModel.isLoading.collectAsState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.generateFlashcards(context, it)
            }
        }
    )

    // Add history when flashcardsOutput changes and is not empty
    LaunchedEffect(flashcardsOutput) {
        val uid = authViewModel.currentUser?.uid
        if (!flashcardsOutput.isNullOrEmpty() && uid != null) {
            HistoryUtils.addHistoryItem(uid, "Flashcards generated")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Flashcard Generator", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste or type text to convert into flashcards...") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.generateFlashcardsFromText(inputText)
                    }
                },
                enabled = inputText.isNotBlank()
            ) {
                Text("Generate from Text")
            }

            Button(onClick = { imagePicker.launch("image/*") }) {
                Text("Upload Image")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Generating flashcards...")
                }
            }
        }
    }
}

