package com.example.todolist.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todolist.db.entity.ToDoItemEntity
import com.example.todolist.repo.DBRepository
import com.example.todolist.repo.PreferencesRepository
import com.example.todolist.utils.DateUtils
import kotlinx.coroutines.flow.first

class TransferPreferenceToDbWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    private val dateUtils = DateUtils()
    private val dbRepository = DBRepository.getInstance(context)
    private val preferencesRepository = PreferencesRepository.getInstance(context)

    override suspend fun doWork(): Result {
        try {
            val todoItems = preferencesRepository.todoList.first()

            if(todoItems.isEmpty()){
                return Result.success()
            }

            todoItems.forEach { item ->
                val entity = ToDoItemEntity(
                    id = item.id,
                    label = item.label,
                    isDone = item.isDone,
                    date = dateUtils.getStartOfDayTimestamp()
                )
                dbRepository.addTodoItem(entity)
            }

            preferencesRepository.clearTodoList()

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}

fun workerTestFunction(context: Context) {
    val workRequest = OneTimeWorkRequestBuilder<TransferPreferenceToDbWorker>()
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "PreferencesToDatabaseWorker",
        ExistingWorkPolicy.REPLACE,
        workRequest
    )

}