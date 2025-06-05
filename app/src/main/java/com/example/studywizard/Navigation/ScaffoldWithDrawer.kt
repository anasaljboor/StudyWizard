package com.example.studywizard.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.studywizard.auth.AuthState
import com.example.studywizard.auth.AuthViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun ScaffoldWithDrawer(
    navController: NavController,
    authViewModel: AuthViewModel,
    currentContent: @Composable () -> Unit,
    topBar: @Composable (() -> Unit)? = null
) {
    val navItemList = listOf(
        NavItem("Home", Icons.Filled.Home, 0),
        NavItem("Quiz", Icons.Filled.Quiz, 0),
        NavItem("Summary", Icons.Filled.Summarize, 0),
        NavItem("FlashCard", Icons.Filled.FlashOn, 0)
    )

    var selectedIndex by remember { mutableStateOf(-1) }
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
                                navController.navigate("signin") {
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
                                // You can add navigation logic here if needed
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        ) { padding ->
            Surface(modifier = Modifier.padding(padding)) {
                currentContent()
            }
        }
    }
}
