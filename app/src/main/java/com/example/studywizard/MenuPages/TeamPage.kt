package com.example.studywizard.MenuPages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.Navigation.ScaffoldWithDrawer


@Composable
fun TeamPage(navController: NavController, authViewModel: AuthViewModel) {
    ScaffoldWithDrawer(navController, authViewModel) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Meet the Team", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            listOf(
                "👨‍💻 Ayham — Backend & AI Developer",
                "👩‍🎨 Anas — Backend & UI/UX Designer",
            ).forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

