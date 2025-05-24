package com.example.studywizard.HomePage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.studywizard.Navigation.AppNavigation
import com.example.studywizard.Navigation.AppBar
import com.example.studywizard.Navigation.DrawerBody
import com.example.studywizard.Navigation.HeadDrawer
import com.example.studywizard.Navigation.MenuItem
import com.example.studywizard.Navigation.NavItem
import com.example.studywizard.auth.AuthState
import com.example.studywizard.auth.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val navItemList = listOf(
        NavItem(label = "Home", icon = Icons.Filled.Home, badgeCount = 0)
        // Add more nav items here if needed
    )

    var selectedIndex by remember { mutableStateOf(0) }
    val authState = authViewModel.authSate.observeAsState()

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
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
                    // Add more menu items here
                ),
                onItemClick = {
                    println("Clicked on ${it.title}")
                    scope.launch { drawerState.close() }
                    // Handle navigation here if needed:
                    navController.navigate(it.id)
                }
            )
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    onNavigationIconClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationRailItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (navItem.badgeCount > 0) {
                                            Badge { Text(text = navItem.badgeCount.toString()) }
                                        }
                                    }
                                ) {
                                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                                }
                            },
                            label = { Text(text = navItem.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavigation(modifier = Modifier.fillMaxSize(), authViewModel = authViewModel)
                ContentScreen(
                    modifier = Modifier.padding(innerPadding),
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
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    when (selectedIndex) {
        0 -> homePage(navController = navController, authViewModel = authViewModel)
        // Add other pages here
    }
}
