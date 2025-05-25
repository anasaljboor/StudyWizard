package com.example.studywizard.HomePage

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studywizard.Navigation.*
import com.example.studywizard.auth.AuthState
import com.example.studywizard.auth.AuthViewModel
import kotlinx.coroutines.launch
import com.example.studywizard.QuizGen.QuizScreen

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val navItemList = listOf(
        NavItem(label = "Home", icon = Icons.Filled.Home, badgeCount = 0),
        NavItem(label = "Quiz", icon = Icons.Filled.Quiz, badgeCount = 0),
        NavItem(label = "Summary", icon = Icons.Filled.Summarize, badgeCount = 0),
        NavItem(label = "FlashCard", icon = Icons.Filled.FlashOn, badgeCount = 0)
    )

    var selectedIndex by remember { mutableStateOf(0) }
    val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Redirect if not authenticated
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
    )

    {
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
                                BadgedBox(
                                    badge = {
                                        if (navItem.badgeCount > 0) {
                                            Badge { Text("${navItem.badgeCount}") }
                                        }
                                    }
                                ) {
                                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
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
    when (selectedIndex) {
        0 -> HomeScreen()
        1 -> QuizScreen() // Add this line
        // 2 -> SummaryScreen()
        // 3 -> FlashCardScreen()
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
    }
}

