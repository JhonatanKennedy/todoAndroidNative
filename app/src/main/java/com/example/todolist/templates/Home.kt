package com.example.todolist.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.components.Subtitle
import com.example.todolist.components.Title
import com.example.todolist.components.TodoItemComponent
import com.example.todolist.components.EmptyListMessage
import com.example.todolist.viewModel.PreferencesViewModel

@Composable
fun TemplateHome(preferencesViewModel: PreferencesViewModel = viewModel(), navController: NavController){
        val isDarkMode by remember { preferencesViewModel.isDarkMode }.collectAsState()
        val todoList by remember { preferencesViewModel.todoList }.collectAsState()

        val todoItems = todoList.filter { !it.isDone }
        val doneItems = todoList.filter { it.isDone }

        Scaffold(
            floatingActionButton = {
                IconButton (onClick = { navController.navigate("addItem") }) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
            ) {
                item{
                    Title("TODO List")
                }

                item{
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Subtitle("Items to do")

                        Button (onClick = { preferencesViewModel.toggleTheme(!isDarkMode)}, modifier = Modifier.padding(16.dp)) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Localized description"
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = if (isDarkMode) "Dark Mode" else "Light Mode" )
                        }
                    }
                }

                items(todoItems, key = { it.id }) { item ->
                    TodoItemComponent(item, onClickCheck = { toDoItem ->
                        preferencesViewModel.updateTodoItem(toDoItem)
                    })
                }
                item{
                    EmptyListMessage(todoItems.isEmpty(), "Add an item to the list")
                }

                item{
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Subtitle("Items done")
                    }
                }

                items(doneItems, key = { it.id }) { item ->
                    TodoItemComponent(item, onClickCheck = { toDoItem ->
                        preferencesViewModel.updateTodoItem(toDoItem)
                    })
                }

                item{
                    EmptyListMessage(doneItems.isEmpty(), "Do something")
                }
            }
        }
}