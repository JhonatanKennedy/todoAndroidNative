package com.example.todolist.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todolist.templates.QuoteTemplate
import com.example.todolist.templates.TemplateAddItem
import com.example.todolist.templates.TemplateHistory
import com.example.todolist.templates.TemplateHome


@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigator(navController) }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { TemplateHome(navController = navController) }
            composable("history") { TemplateHistory() }
            composable("addItem") { TemplateAddItem(navController = navController) }
            composable("quote") { QuoteTemplate() }
        }
    }
}

@Composable
fun BottomNavigator(navController: NavController){
    val routes = listOf("home", "history", "quote")
    val labels = listOf("Home", "History", "Quote")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.DateRange, Icons.Filled.Favorite)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.DateRange, Icons.Outlined.FavoriteBorder)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        routes.forEachIndexed { index, route ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (currentRoute == route) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = labels[index]
                    )
                },
                label = { Text(labels[index]) },
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}