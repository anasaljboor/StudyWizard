package com.example.studywizard.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.auth.AuthState

@Composable
fun ProfilePage(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var fullName by remember { mutableStateOf<String?>(null) }
    val userEmail = authViewModel.getUserId()?.let { uid ->
        authViewModel.authState.value?.let {
            authViewModel.authState.value.toString()
        }
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        authViewModel.getUserName { name ->
            fullName = name
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Profile", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Name: ${fullName ?: "Loading..."}", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Email: ${authViewModel.getUserId() ?: "Not Available"}", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                authViewModel.signout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
