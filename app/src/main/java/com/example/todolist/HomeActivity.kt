package com.example.todolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.model.TodoItemModel

@Composable
fun TemplateHome(viewModel: PreferencesViewModel = viewModel(), navController: NavController){
        val isDarkMode by remember { viewModel.isDarkMode }.collectAsState()
        val todoList by remember { viewModel.todoList }.collectAsState()

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

                        Button (onClick = { viewModel.toggleTheme(!isDarkMode)}, modifier = Modifier.padding(16.dp)) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Localized description"
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = if (isDarkMode) "Dark Mode" else "Light Mode" )
                        }
                    }
                }

                todoItems.map{ todoItem ->
                    item{TodoItem(todoItem, onClickCheck = { item ->
                        viewModel.updateTodoItem(item)
                    })}
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

                doneItems.map{ todoItem ->
                    item{TodoItem(todoItem, onClickCheck = { item ->
                        viewModel.updateTodoItem(item)
                    })}
                }

                item{
                    EmptyListMessage(doneItems.isEmpty(), "Do something")
                }
            }

        }
}

@Composable
fun Title(label: String){
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            text = label
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun Subtitle(label: String){
    Text(
        modifier = Modifier.padding(start = 16.dp),
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        text = label
    )
}

@Composable
fun TodoItem(item: TodoItemModel, onClickCheck: (item:TodoItemModel) -> Unit){
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickCheck(item)
            }
    ) {
        Checkbox(checked = item.isDone, onCheckedChange = {
            onClickCheck(item)
        })
        Text(item.label, style =  TextStyle(textDecoration = if(item.isDone) TextDecoration.LineThrough else TextDecoration.None))
    }
}

@Composable
fun EmptyListMessage(isEmpty: Boolean, message: String){
    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(if(isEmpty) message else "", style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 14.sp))
    }
}