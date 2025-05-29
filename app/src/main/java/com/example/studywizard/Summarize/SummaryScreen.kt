package com.example.studywizard.Summarize

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studywizard.Cohere_ML.CohereViewModel

@Composable
fun SummaryScreen(
    viewModel: CohereViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    var inputText by remember { mutableStateOf("") }
    val summaryOutput by remember { derivedStateOf { viewModel.summaryOutput } }
    val isLoading by viewModel.isLoading.collectAsState()

    // 🆕 Image picker to allow summary from image
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.generateSummary(context, it) }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Summary Generator", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste or type text to summarize...") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Generating summary...")
                }
            }

            summaryOutput != null -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        summaryOutput ?: "",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                Text("Enter text or upload an image to generate a summary.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
