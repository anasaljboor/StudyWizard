package com.example.studywizard.HomePage

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val navItemList = listOf(
        NavItem(label = "Home", icon = Icons.Filled.Home, badgeCount = 0)
    )

    var selectedIndex by remember { mutableStateOf(0) }
    val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate("login")
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HeadDrawer()
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "home",
                        title = "Home",
                        contentDescriptor = "Go to home screen",
                        icon = Icons.Default.Home
                    )
                ),
                onItemClick = {
                    scope.launch { drawerState.close() }
                    if (it.id == "home") selectedIndex = 0
                }
            )
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
        // Future indices for other screens can be added here
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
        Text("Welcome to the Home Screen!", style = MaterialTheme.typography.headlineMedium)
    }
}
