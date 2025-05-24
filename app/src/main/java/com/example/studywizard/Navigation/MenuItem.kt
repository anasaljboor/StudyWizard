package com.example.studywizard.Navigation

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.internal.OpDescriptor

data class MenuItem(
    val id: String,
    val title: String,
    val contentDescriptor: String,
    val icon: ImageVector
)
