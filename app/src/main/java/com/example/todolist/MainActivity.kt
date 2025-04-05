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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.example.todolist.components.Navigation
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.viewModel.PreferencesViewModel
import com.example.todolist.workers.TransferPreferenceToDbWorker

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val workRequest = OneTimeWorkRequestBuilder<TransferPreferenceToDbWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "PreferencesToDatabaseWorker",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )

        setContent {
            val preferencesViewModel: PreferencesViewModel = viewModel()
            val isDarkMode by preferencesViewModel.isDarkMode.collectAsState(initial = false)

            ToDoListTheme (darkTheme = isDarkMode) {
                Navigation()
            }
        }
    }

}