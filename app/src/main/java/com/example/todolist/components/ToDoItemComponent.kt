package com.example.todolist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.example.todolist.model.TodoItemModel

@Composable
fun TodoItemComponent(item: TodoItemModel, onClickCheck: (item: TodoItemModel) -> Unit, isEnabled: Boolean = true){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) {
                onClickCheck(item)
            }
    ) {
        Checkbox(
            checked = item.isDone,
            enabled = isEnabled,
            onCheckedChange = {
                onClickCheck(item)
            }
        )
        Text(item.label, style =  TextStyle(textDecoration = if(item.isDone) TextDecoration.LineThrough else TextDecoration.None))
    }
}