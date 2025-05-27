package com.example.studywizard.HomePage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studywizard.FlashCard.FlashcardsScreen
import com.example.studywizard.Navigation.*
import com.example.studywizard.QuizGen.QuizScreen
import com.example.studywizard.Summarize.SummaryScreen
import com.example.studywizard.auth.AuthState
import com.example.studywizard.auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val navItemList = listOf(
        NavItem("Home", Icons.Filled.Home, 0),
        NavItem("Quiz", Icons.Filled.Quiz, 0),
        NavItem("Summary", Icons.Filled.Summarize, 0),
        NavItem("FlashCard", Icons.Filled.FlashOn, 0)
    )

    var selectedIndex by remember { mutableStateOf(0) }
    val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                HeadDrawer()
                DrawerBody(
                    items = listOf(
                        MenuItem("home", "Home", "Go to home screen", Icons.Default.Home),
                        MenuItem("about", "About", "Learn about this app", Icons.Default.Info),
                        MenuItem("features", "Features", "App functionality", Icons.Default.Star),
                        MenuItem("team", "Our Team", "Who built the app", Icons.Default.Group),
                        MenuItem("logout", "Logout", "Sign out", Icons.Default.Logout),
                    ),
                    onItemClick = {
                        scope.launch { drawerState.close() }
                        when (it.id) {
                            "home" -> selectedIndex = 0
                            "about" -> navController.navigate("about")
                            "features" -> navController.navigate("features")
                            "team" -> navController.navigate("team")
                            "logout" -> {
                                authViewModel.signout()
                                navController.navigate("signin") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                AppBar {
                    scope.launch { drawerState.open() }
                }
            },
            bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = {
                                BadgedBox(badge = {
                                    if (navItem.badgeCount > 0) {
                                        Badge { Text("${navItem.badgeCount}") }
                                    }
                                }) {
                                    Icon(navItem.icon, contentDescription = navItem.label)
                                }
                            },
                            label = { Text(navItem.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                ContentScreen(
                    selectedIndex = selectedIndex,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun ContentScreen(
    selectedIndex: Int,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // Use viewModel() to get lifecycle-aware HomePageViewModel instance
    val homePageViewModel: HomePageViewModel = viewModel()

    when (selectedIndex) {
        0 -> HomeScreen(authViewModel = authViewModel, homePageViewModel = homePageViewModel)
        1 -> QuizScreen()
        2 -> SummaryScreen()
        3 -> FlashcardsScreen()
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

    LaunchedEffect(userId) {
        if (userId != null) {
            homePageViewModel.fetchHistory(userId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            userId == null -> {
                Text("Please log in to see your history.")
            }
            historyError != null -> {
                Text("Error loading history: $historyError")
            }
            history.isEmpty() -> {
                Text("No history yet.")
            }
            else -> {
                LazyColumn {
                    items(history.size) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = history[index],
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}