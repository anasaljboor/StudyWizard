package com.example.studywizard.MenuPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studywizard.Navigation.ScaffoldWithDrawer
import com.example.studywizard.auth.AuthViewModel
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamPage(navController: NavController, authViewModel: AuthViewModel) {
    ScaffoldWithDrawer(
        navController = navController,
        authViewModel = authViewModel,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Our Team",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        currentContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                val teamMembers = listOf(
                    TeamMember("Anas", "Backend & AI Developer", "👨‍💻"),
                    TeamMember("Ayham", "Backend & UI/UX Designer", "👨‍💻")
                )

                teamMembers.forEach { member ->
                    TeamMemberCard(member)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    )
}

data class TeamMember(val name: String, val role: String, val emoji: String)

@Composable
fun TeamMemberCard(member: TeamMember) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Circle avatar with emoji
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.emoji,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = member.role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}
