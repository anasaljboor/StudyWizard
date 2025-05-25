package com.example.studywizard.MenuPages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AboutPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("About StudyWizard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "StudyWizard is an AI-powered learning assistant designed to help students learn smarter. Whether it's solving questions, generating flashcards, or summarizing content, we’ve got you covered!",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
