package com.example.studywizard.HomePage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studywizard.Navigation.AppNavigation
import com.example.studywizard.auth.AuthState
import com.example.studywizard.auth.AuthViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.mutableIntStateOf
import com.example.studywizard.Navigation.NavItem

@Composable

fun homePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){

   val navItemList = listOf(
       NavItem(label = "Home", icon = Icons.Filled.Home, 0),
//       ADD THE OTHER PAGES WHEN DONE
//       NavItem(label = "Notification", icon = Icons.Filled.Notifications , 1),
//       NavItem(label = "Settings", icon = Icons.Filled.Settings , 0)
   )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationRailItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index } ,
                        icon = {
                            BadgedBox(badge = {
                                if(navItem.badgeCount > 0)
                                    Badge(){
                                        Text(text = navItem.badgeCount.toString())
                                     }
                            }) {
                                Icon(imageVector = navItem.icon , contentDescription = "Icon")
                                }
                            },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        }
        ) { innerPadding ->
        AppNavigation(modifier = Modifier.padding(innerPadding) , authViewModel = authViewModel)
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex , authViewModel = authViewModel , navController = navController)
    }
    val authState = authViewModel.authSate.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "HOME PAGE" , fontSize = 32.sp)

        TextButton(onClick = {
            authViewModel.signout()
        }) {
            Text(text = "Sign out")
        }
    }

}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int,  navController: NavController, authViewModel: AuthViewModel){
    when(selectedIndex) {
        0-> homePage(navController = navController, authViewModel = authViewModel)
        //       ADD THE OTHER PAGES WHEN DONE
//        1->
//        2->
    }
}