package com.example.todolist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.todolist.components.Navigation
import com.example.todolist.repo.PreferencesRepository
import com.example.todolist.ui.theme.ToDoListTheme


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    private lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        preferencesRepository = PreferencesRepository(applicationContext)

        setContent {
            val isDarkMode by preferencesRepository.isDarkMode.collectAsState(initial = false)

            ToDoListTheme (darkTheme = isDarkMode) {
                Navigation()
            }
        }

    }
}