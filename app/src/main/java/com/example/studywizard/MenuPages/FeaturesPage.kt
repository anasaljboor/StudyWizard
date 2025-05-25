package com.example.studywizard.MenuPages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FeaturesPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("App Features", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        val features = listOf(
            "✅ Solve any question instantly using AI",
            "📚 Generate smart flashcards from your notes",
            "🧠 Summarize long content into key points",
            "🔍 Search your history of solved questions",
            "🎤 Input using text, image, file, or voice"
        )

        features.forEach {
            Text(text = it, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
