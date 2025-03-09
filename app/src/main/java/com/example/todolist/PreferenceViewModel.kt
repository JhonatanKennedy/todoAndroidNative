package com.example.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.TodoItemModel
import com.example.todolist.repo.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PreferencesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PreferencesRepository(application)

    val isDarkMode: StateFlow<Boolean> = repository.isDarkMode.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    val todoList: StateFlow<List<TodoItemModel>> = repository.todoList.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            repository.updateDarkMode(isDark)
        }
    }

    fun addTodoItem(newItem: TodoItemModel) {
        viewModelScope.launch {
            repository.addTodoItem(newItem)
        }
    }

    fun updateTodoItem(updatedItem: TodoItemModel) {
        viewModelScope.launch {
            repository.updateTodoItem(updatedItem)
        }
    }

    fun removeTodoItem(itemId: String) {
        viewModelScope.launch {
            repository.removeTodoItem(itemId)
        }
    }

}