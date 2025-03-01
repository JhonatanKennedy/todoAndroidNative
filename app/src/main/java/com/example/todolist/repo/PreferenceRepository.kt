package com.example.todolist.repo

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.todolist.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepository(private val context: Context) {

    private val THEME_KEY = booleanPreferencesKey("dark_mode")
    private val TODO_KEY = stringSetPreferencesKey("todo_list")

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: false
        }

    val todoList: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            preferences[TODO_KEY]?.toList() ?: emptyList()
        }

    suspend fun updateDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    suspend fun addTodoItem(newItem: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[TODO_KEY]?.toMutableSet() ?: mutableSetOf()
            currentList.add(newItem)
            preferences[TODO_KEY] = currentList
        }
    }
}