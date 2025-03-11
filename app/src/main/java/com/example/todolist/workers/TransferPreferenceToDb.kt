package com.example.todolist.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todolist.db.entity.ToDoItemEntity
import com.example.todolist.repo.DBRepository
import com.example.todolist.repo.PreferencesRepository
import com.example.todolist.utils.DateUtils
import kotlinx.coroutines.flow.first

class TransferPreferenceToDbWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    private val dateUtils = DateUtils()
    private val dbRepository = DBRepository(context)
    private val preferencesRepository = PreferencesRepository(context)

    override suspend fun doWork(): Result {
        try {
            val todoItems = preferencesRepository.todoList.first()

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
            return Result.retry()
        }
    }
}