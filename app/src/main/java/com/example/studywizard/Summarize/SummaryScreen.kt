package com.example.studywizard.Summarize

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
fun SummaryScreen(
    viewModel: CohereViewModel = viewModel(),
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context = LocalContext.current
) {
    var inputText by remember { mutableStateOf("") }
    val summaryOutput by remember { derivedStateOf { viewModel.summaryOutput } }
    val isLoading by viewModel.isLoading.collectAsState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.generateSummary(context, it) }
        }
    )

    // Add history when summaryOutput updates and is not empty
    LaunchedEffect(summaryOutput) {
        val uid = authViewModel.currentUser?.uid
        if (!summaryOutput.isNullOrEmpty() && uid != null) {
            HistoryUtils.addHistoryItem(uid, "Summary generated")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Summary Generator", style = MaterialTheme.typography.headlineMedium)
        }

        item {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Paste or type text to summarize...") }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.generateSummaryFromText(inputText)
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
        }

        item {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Generating summary...")
                    }
                }

                !summaryOutput.isNullOrEmpty() -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(
                            text = summaryOutput ?: "",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                else -> {
                    Text(
                        "Enter text or upload an image to generate a summary.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
