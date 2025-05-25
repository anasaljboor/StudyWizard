package com.example.studywizard.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.HomePage.homePage
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
            homePage(modifier, navController, authViewModel)
        }
    }
}
