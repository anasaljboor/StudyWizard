package com.example.studywizard.HomePage

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studywizard.FlashCard.FlashcardsScreen
import com.example.studywizard.Navigation.*
import com.example.studywizard.QuizGen.QuizScreen
import com.example.studywizard.Summarize.SummaryScreen
import com.example.studywizard.auth.AuthState
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.Cohere_ML.CohereViewModel // <-- Import your CohereViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val navItems = listOf(
        NavItem("Home", Icons.Filled.Home, 0),
        NavItem("Quiz", Icons.Filled.Quiz, 0),
        NavItem("Summary", Icons.Filled.Summarize, 0),
        NavItem("FlashCard", Icons.Filled.FlashOn, 0)
    )

    var selectedIndex by remember { mutableStateOf(0) }
    val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var userName by remember { mutableStateOf<String?>(null) }

    // Obtain CohereViewModel instance here
    val cohereViewModel: CohereViewModel = viewModel()

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                HeadDrawer(authViewModel = authViewModel, onProfileClick = {
                    navController.navigate("profile")
                })
                DrawerBody(
                    items = listOf(
                        MenuItem("home", "Home", "Go to home screen", Icons.Default.Home),
                        MenuItem("about", "About", "Learn about this app", Icons.Default.Info),
                        MenuItem("features", "Features", "App functionality", Icons.Default.Star),
                        MenuItem("team", "Our Team", "Who built the app", Icons.Default.Group),
                    ),
                    onItemClick = {
                        scope.launch { drawerState.close() }
                        when (it.id) {
                            "home" -> selectedIndex = 0
                            "about" -> navController.navigate("about")
                            "features" -> navController.navigate("features")
                            "team" -> navController.navigate("team")
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "StudyWizard",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceTint) {
                    navItems.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = {
                                Icon(navItem.icon, contentDescription = navItem.label)
                            },
                            label = { Text(navItem.label) },
                            alwaysShowLabel = false,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                selectedTextColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = "Welcome${userName?.let { ", $it" } ?: ""} 👋",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    ContentScreen(
                        selectedIndex = selectedIndex,
                        navController = navController,
                        authViewModel = authViewModel,
                        cohereViewModel = cohereViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ContentScreen(
    selectedIndex: Int,
    navController: NavController,
    authViewModel: AuthViewModel,
    cohereViewModel: CohereViewModel
) {
    val homePageViewModel: HomePageViewModel = viewModel()

    when (selectedIndex) {
        0 -> HomeScreen(authViewModel, homePageViewModel)
        1 -> QuizScreen(cohereViewModel, authViewModel, navController)
        2 -> SummaryScreen(cohereViewModel, authViewModel, navController)
        3 -> FlashcardsScreen(cohereViewModel, authViewModel, navController)
        else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Coming Soon!", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

fun FlashcardsScreen(viewModel: CohereViewModel, navController: AuthViewModel, authViewModel: NavController) {

}

fun SummaryScreen(viewModel: CohereViewModel, navController: AuthViewModel, authViewModel: NavController) {

}

fun QuizScreen(viewModel: CohereViewModel, navController: AuthViewModel, authViewModel: NavController) {

}

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    homePageViewModel: HomePageViewModel
) {
    val history by homePageViewModel.history.observeAsState(emptyList())
    val historyError by homePageViewModel.error.observeAsState()
    val userId = authViewModel.getUserId()

    var userName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        authViewModel.getUserName { userName = it ?: "User" }
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (historyError != null) {
            Text(
                "Error loading history: $historyError",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        } else if (history.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No history yet. Start by generating some content!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
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

@Composable
fun HistoryCard(title: String) {
    val type = when {
        title.contains("Quiz", ignoreCase = true) -> "Quiz"
        title.contains("Summary", ignoreCase = true) -> "Summary"
        title.contains("Flashcards", ignoreCase = true) -> "Flashcards"
        else -> "History"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
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
