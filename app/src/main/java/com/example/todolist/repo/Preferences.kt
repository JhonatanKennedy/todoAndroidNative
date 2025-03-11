package com.example.todolist.repo

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.todolist.dataStore
import com.example.todolist.model.TodoItemModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PreferencesRepository(private val context: Context) {
    private val gson = Gson()

    private val THEME_KEY = booleanPreferencesKey("dark_mode")
    private val TODO_KEY = stringSetPreferencesKey("todo_list")

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: false
        }

    val todoList: Flow<List<TodoItemModel>> = context.dataStore.data
        .map { preferences ->
            val todoListJson = preferences[TODO_KEY] ?: emptySet()
            todoListJson.map { json ->
                gson.fromJson(json, TodoItemModel::class.java)
            }
        }


    suspend fun updateDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }


    //List
    suspend fun addTodoItem(newItem: TodoItemModel) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[TODO_KEY]?.toMutableSet() ?: mutableSetOf()
            val newItemJson = gson.toJson(newItem)
            currentList.add(newItemJson)
            preferences[TODO_KEY] = currentList
        }
    }

    suspend fun updateTodoItem(updatedItem: TodoItemModel) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[TODO_KEY]?.toMutableSet() ?: mutableSetOf()
            val updatedList = currentList.map { json ->
                gson.fromJson(json, TodoItemModel::class.java)
            }.map { item ->
                if (item.id == updatedItem.id) {
                    updatedItem.copy(isDone = !updatedItem.isDone)
                } else {
                    item
                }
            }
            preferences[TODO_KEY] = updatedList.map { gson.toJson(it) }.toSet()
        }
    }

    suspend fun removeTodoItem(itemId: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[TODO_KEY]?.toMutableSet() ?: mutableSetOf()
            val updatedList = currentList.map { json ->
                gson.fromJson(json, TodoItemModel::class.java)
            }.filter { item ->
                item.id != itemId
            }
            preferences[TODO_KEY] = updatedList.map { gson.toJson(it) }.toSet()
        }
    }

    suspend fun clearTodoList() {
        context.dataStore.edit { preferences ->
            preferences.remove(TODO_KEY)
        }
    }
}