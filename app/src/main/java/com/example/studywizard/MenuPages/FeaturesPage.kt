package com.example.studywizard.MenuPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.Navigation.ScaffoldWithDrawer
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturesPage(navController: NavController, authViewModel: AuthViewModel) {
    ScaffoldWithDrawer(
        navController = navController,
        authViewModel = authViewModel,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "App Features",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        currentContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Explore powerful features designed to enhance your learning experience!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                val features = listOf(
                    FeatureItem(Icons.Default.Lightbulb, "Instant AI Solutions", "Solve any question instantly using AI."),
                    FeatureItem(Icons.Default.AutoStories, "Smart Flashcards", "Generate smart flashcards from your notes."),
                    FeatureItem(Icons.Default.Summarize, "Content Summarization", "Summarize long content into key points."),
                    FeatureItem(Icons.Default.History, "History Search", "Search your history of solved questions."),
                    FeatureItem(Icons.Default.Mic, "Multimodal Input", "Input using text, image, file, or voice.")
                )

                features.forEach { feature ->
                    FeatureCard(feature)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    )
}

data class FeatureItem(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String, val description: String)

@Composable
fun FeatureCard(feature: FeatureItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(feature.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(feature.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}
