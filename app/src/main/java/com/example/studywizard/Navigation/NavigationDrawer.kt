package com.example.studywizard.Navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studywizard.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.studywizard.Navigation.MenuItem

@Composable
fun HeadDrawer(
    authViewModel: AuthViewModel,
    onProfileClick: () -> Unit  // Callback when user clicks on profile area
) {
    var fullName by remember { mutableStateOf("User") }
    var email by remember { mutableStateOf("email@example.com") }

    LaunchedEffect(Unit) {
        authViewModel.getUserName { name ->
            if (!name.isNullOrBlank()) fullName = name
        }
        val user = FirebaseAuth.getInstance().currentUser
        email = user?.email ?: "No email"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onProfileClick() },  // Make entire row clickable
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Icon",
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(text = fullName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = email, fontSize = 14.sp, color = Color.Gray)
        }
    }
}


@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) }
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescriptor,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.title,
                        style = itemTextStyle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
