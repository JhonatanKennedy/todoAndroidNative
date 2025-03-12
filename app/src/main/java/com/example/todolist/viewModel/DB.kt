package com.example.todolist.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.todolist.model.TodoItemModel
import com.example.todolist.repo.DBRepository
import com.example.todolist.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class DBViewModel(application: Application): AndroidViewModel(application){
    private val repository = DBRepository.getInstance(application)
    private val dateUtils = DateUtils()

    fun getItemsGroupedByDay(): Flow<Map<String, List<TodoItemModel>>> = flow {
        val groupedItems = mutableMapOf<String, List<TodoItemModel>>()
        val last7Days = dateUtils.getLast7DaysTimestamps()

        last7Days.forEach { date ->
            repository.getTodoItemsByDate(date).collect { items ->
                if (items.isNotEmpty()) {
                    val currentDate = dateUtils.fromTimestampToString(date)
                    groupedItems[currentDate] = items.map{ item->
                        TodoItemModel(id = item.id, label = item.label, isDone = item.isDone)
                    }
                    emit(groupedItems)
                }

            }
        }
    }
}