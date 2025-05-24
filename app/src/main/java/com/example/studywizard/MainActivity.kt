package com.example.studywizard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.studywizard.HomePage.homePage
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.ui.theme.StudyWizardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            StudyWizardTheme {
                val navController = rememberNavController()
                homePage(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}
