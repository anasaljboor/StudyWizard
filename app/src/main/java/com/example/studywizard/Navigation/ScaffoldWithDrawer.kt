package com.example.studywizard.Navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.studywizard.FlashCard.FlashcardsScreen
import com.example.studywizard.HomePage.HomePage
import com.example.studywizard.MenuPages.AboutPage
import com.example.studywizard.MenuPages.FeaturesPage
import com.example.studywizard.MenuPages.TeamPage
import com.example.studywizard.QuizGen.QuizScreen
import com.example.studywizard.Summarize.SummaryScreen
import com.example.studywizard.auth.AuthState
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.auth.LoginPage
import com.example.studywizard.auth.SignupPage
import com.example.studywizard.profile.ProfilePage
import kotlinx.coroutines.launch

@Composable
fun ScaffoldWithDrawer(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    topBar: @Composable (() -> Unit)? = null
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
                HeadDrawer(authViewModel = authViewModel, onProfileClick = {
                    navController.navigate("profile")
                })
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
                            "home" -> navController.navigate("home")
                            "about" -> navController.navigate("about")
                            "features" -> navController.navigate("features")
                            "team" -> navController.navigate("team")
                            "logout" -> {
                                authViewModel.signout()
                                navController.navigate("login") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = topBar ?: { AppBar { scope.launch { drawerState.open() } } },
            bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                when (item.label.lowercase()) {
                                    "home" -> navController.navigate("home") { launchSingleTop = true }
                                    "quiz" -> navController.navigate("quiz") { launchSingleTop = true }
                                    "summary" -> navController.navigate("summary") { launchSingleTop = true }
                                    "flashcard" -> navController.navigate("flashcards") { launchSingleTop = true }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(padding)
            ) {
                composable("home") {
                    HomePage(navController = navController, authViewModel = authViewModel)
                }
                composable("quiz") {
                    QuizScreen(navController = navController, authViewModel = authViewModel)
                }
                composable("summary") {
                    SummaryScreen(navController = navController, authViewModel = authViewModel)
                }
                composable("flashcards") {
                    FlashcardsScreen(navController = navController, authViewModel = authViewModel)
                }
                composable("profile") {
                    ProfilePage(navController = navController, authViewModel = authViewModel)
                }
                composable("about") {
                    AboutPage(navController = navController, authViewModel = authViewModel)
                }
                composable("features") {
                    FeaturesPage(navController = navController, authViewModel = authViewModel)
                }
                composable("team") {
                    TeamPage(navController = navController, authViewModel = authViewModel)
                }
                composable("login") {
                    LoginPage(modifier = Modifier, navController = navController, authViewModel = authViewModel)
                }
                composable("signup") {
                    SignupPage(modifier = Modifier, navController = navController, authViewModel = authViewModel)
                }
            }
        }
    }
}
