package com.example.studywizard.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studywizard.FlashCard.FlashcardsScreen
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.HomePage.HomePage
import com.example.studywizard.MenuPages.AboutPage
import com.example.studywizard.MenuPages.FeaturesPage
import com.example.studywizard.MenuPages.TeamPage
import com.example.studywizard.Summarize.SummaryScreen
import com.example.studywizard.auth.LoginPage
import com.example.studywizard.auth.SignupPage


@Composable
fun AppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }

        // ✅ Add the missing composables for navigation
        composable("about") {
            AboutPage(navController = navController, authViewModel = authViewModel)
        }
        composable("features") {
            FeaturesPage(navController = navController, authViewModel = authViewModel)
        }
        composable("team") {
            TeamPage(navController = navController, authViewModel = authViewModel)
        }
        composable("summary") {
            SummaryScreen()
        }
        composable("flashcards") {
            FlashcardsScreen()
        }
        composable("quiz") {
            SummaryScreen()
        }
    }
}
