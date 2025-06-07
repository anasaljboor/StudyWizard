package com.example.studywizard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue // ✅ required for `by` delegate
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studywizard.Navigation.ScaffoldWithDrawer
import com.example.studywizard.auth.AuthState
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.auth.LoginPage
import com.example.studywizard.auth.SignupPage
import com.example.studywizard.ui.theme.StudyWizardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()

        setContent {
            StudyWizardTheme {
                val navController = rememberNavController()
                val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)

                if (authState is AuthState.Authenticated) {
                    ScaffoldWithDrawer(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                } else {
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            LoginPage(navController = navController, authViewModel = authViewModel)
                        }
                        composable("signup") {
                            SignupPage(navController = navController, authViewModel = authViewModel)
                        }
                    }
                }
            }
        }
    }
}
