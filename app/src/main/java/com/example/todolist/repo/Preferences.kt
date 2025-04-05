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

class PreferencesRepository private constructor(context: Context) {
    private val appContext: Context = context.applicationContext

    companion object {
        @Volatile
        private var instance: PreferencesRepository? = null

        fun getInstance(context: Context): PreferencesRepository {
            return instance ?: synchronized(this) {
                instance ?: PreferencesRepository(context.applicationContext).also { instance = it }
            }
        }
    }

    private val gson = Gson()

    private val themeKey = booleanPreferencesKey("dark_mode")
    private val todoKey = stringSetPreferencesKey("todo_list")

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[themeKey] ?: false
        }

    val todoList: Flow<List<TodoItemModel>> = context.dataStore.data
        .map { preferences ->
            val todoListJson = preferences[todoKey] ?: emptySet()
            todoListJson.map { json ->
                gson.fromJson(json, TodoItemModel::class.java)
            }
        }


    // Theme
    suspend fun updateDarkMode(isDark: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[themeKey] = isDark
        }
    }

    //List
    suspend fun addTodoItem(newItem: TodoItemModel) {
        appContext.dataStore.edit { preferences ->
            val currentList = preferences[todoKey]?.toMutableSet() ?: mutableSetOf()
            val newItemJson = gson.toJson(newItem)
            currentList.add(newItemJson)
            preferences[todoKey] = currentList
        }
    }

    suspend fun updateTodoItem(updatedItem: TodoItemModel) {
        appContext.dataStore.edit { preferences ->
            val currentList = preferences[todoKey]?.toMutableSet() ?: mutableSetOf()
            val updatedList = currentList.map { json ->
                gson.fromJson(json, TodoItemModel::class.java)
            }.map { item ->
                if (item.id == updatedItem.id) {
                    updatedItem.copy(isDone = !updatedItem.isDone)
                } else {
                    item
                }
            }
            preferences[todoKey] = updatedList.map { gson.toJson(it) }.toSet()
        }
    }

    suspend fun removeTodoItem(itemId: String) {
        appContext.dataStore.edit { preferences ->
            val currentList = preferences[todoKey]?.toMutableSet() ?: mutableSetOf()
            val updatedList = currentList.map { json ->
                gson.fromJson(json, TodoItemModel::class.java)
            }.filter { item ->
                item.id != itemId
            }
            preferences[todoKey] = updatedList.map { gson.toJson(it) }.toSet()
        }
    }

    suspend fun clearTodoList() {
        appContext.dataStore.edit { preferences ->
            preferences.remove(todoKey)
        }
    }
}