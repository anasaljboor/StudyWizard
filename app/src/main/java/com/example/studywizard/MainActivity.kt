package com.example.studywizard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.studywizard.Navigation.AppNavigation
import com.example.studywizard.auth.AuthViewModel
import com.example.studywizard.ui.theme.StudyWizardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            StudyWizardTheme {
                AppNavigation(authViewModel = authViewModel)
            }
        }
    }
}
