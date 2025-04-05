package com.example.todolist.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.TodoItemModel
import com.example.todolist.repo.DBRepository
import com.example.todolist.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DBViewModel(application: Application): AndroidViewModel(application){
    private val repository = DBRepository.getInstance(application)
    private val dateUtils = DateUtils()

    private val _groupedItems = MutableStateFlow<Map<String, List<TodoItemModel>>>(emptyMap())
    val groupedItems: StateFlow<Map<String, List<TodoItemModel>>> = _groupedItems

    init {
        loadGroupedItems()
    }

    private fun loadGroupedItems() {
        viewModelScope.launch {
            val groupedItemsMap = mutableMapOf<String, List<TodoItemModel>>()
            val last7Days = dateUtils.getLast7DaysTimestamps()

            last7Days.forEach { date ->
                repository.getTodoItemsByDate(date).collect { items ->
                    if (items.isNotEmpty()) {
                        val currentDate = dateUtils.fromTimestampToString(date)
                        groupedItemsMap[currentDate] = items.map { item ->
                            TodoItemModel(
                                id = item.id,
                                label = item.label,
                                isDone = item.isDone
                            )
                        }
                        _groupedItems.value = groupedItemsMap
                    }
                }
            }
        }
    }
}