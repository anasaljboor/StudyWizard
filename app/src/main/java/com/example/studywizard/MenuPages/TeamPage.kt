package com.example.studywizard.MenuPages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TeamPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Meet the Team", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        val teamMembers = listOf(
            "👨‍💻 Yousef — Backend & AI Developer",
            "👩‍🎨 Sarah — UI/UX Designer",
            "🧑‍💻 Ahmad — Mobile App Developer",
            "👨‍🔧 Omar — Product Manager"
        )

        teamMembers.forEach {
            Text(text = it, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
