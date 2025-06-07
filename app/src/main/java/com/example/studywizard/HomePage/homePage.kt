package com.example.studywizard.HomePage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.studywizard.auth.AuthState
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.Cohere_ML.CohereViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)
    val cohereViewModel: CohereViewModel = viewModel()
    val homePageViewModel: HomePageViewModel = viewModel()
    var userName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        authViewModel.getUserName { userName = it }
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Welcome${userName?.let { ", $it" } ?: ""} 👋",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        HomeScreen(authViewModel, homePageViewModel)
    }
}

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    homePageViewModel: HomePageViewModel
) {
    val history by homePageViewModel.history.observeAsState(emptyList())
    val historyError by homePageViewModel.error.observeAsState()
    val userId = authViewModel.getUserId()

    LaunchedEffect(Unit) {
        authViewModel.getUserName { /* no-op */ }
    }

    LaunchedEffect(userId) {
        if (userId != null) homePageViewModel.fetchHistory(userId)
    }

    if (userId == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Please log in to see your history.", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when {
            historyError != null -> {
                Text(
                    "Error loading history: $historyError",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            history.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No history yet. Start by generating some content!", style = MaterialTheme.typography.bodyLarge)
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(history) { title ->
                        HistoryCard(title)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(title: String) {
    val type = when {
        title.contains("Quiz", ignoreCase = true) -> "Quiz"
        title.contains("Summary", ignoreCase = true) -> "Summary"
        title.contains("Flashcards", ignoreCase = true) -> "Flashcards"
        else -> "History"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (type) {
                    "Quiz" -> Icons.Filled.Quiz
                    "Summary" -> Icons.Filled.Summarize
                    "Flashcards" -> Icons.Filled.FlashOn
                    else -> Icons.Filled.History
                },
                contentDescription = type,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = type,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
