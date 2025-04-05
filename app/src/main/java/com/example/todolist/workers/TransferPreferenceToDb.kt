package com.example.todolist.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todolist.db.entity.ToDoItemEntity
import com.example.todolist.repo.DBRepository
import com.example.todolist.repo.PreferencesRepository
import com.example.todolist.utils.DateUtils
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.concurrent.TimeUnit


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

            scheduleWorkerForNight()
            return Result.success()
        } catch (e: Exception) {
            scheduleWorkerForNight()
            return Result.failure()
        }
    }

    private fun scheduleWorkerForNight() {
        val now = Calendar.getInstance()

        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Se a hora atual já passou de 23:59, agendar para amanhã
            if (before(now)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val delayInMillis = targetTime.timeInMillis - now.timeInMillis

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()

        val request = OneTimeWorkRequestBuilder<TransferPreferenceToDbWorker>()
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "NightlyWorker",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}