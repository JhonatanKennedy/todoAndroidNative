package com.example.todolist.model

import java.util.UUID
import com.google.gson.annotations.SerializedName

data class TodoItemModel(
    val id: String = UUID.randomUUID().toString(),
    val label: String,
    @SerializedName("isDone") val isDone: Boolean = false
)
