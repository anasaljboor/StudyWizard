package com.example.studywizard.MenuPages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studywizard.Navigation.ScaffoldWithDrawer
import com.example.studywizard.auth.AuthViewModel


@Composable
fun FeaturesPage(navController: NavController, authViewModel: AuthViewModel) {
    ScaffoldWithDrawer(navController, authViewModel) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("App Features", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            listOf(
                "✅ Solve any question instantly using AI",
                "📚 Generate smart flashcards from your notes",
                "🧠 Summarize long content into key points",
                "🔍 Search your history of solved questions",
                "🎤 Input using text, image, file, or voice"
            ).forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

