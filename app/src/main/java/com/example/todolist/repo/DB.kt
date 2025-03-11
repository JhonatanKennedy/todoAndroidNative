package com.example.todolist.repo

import android.content.Context
import androidx.room.Room
import com.example.todolist.db.ToDoItemDB
import com.example.todolist.db.entity.ToDoItemEntity
import kotlinx.coroutines.flow.Flow

class DBRepository(private val context: Context){
    val db = Room.databaseBuilder(
        context.applicationContext,
        ToDoItemDB::class.java, "todo_item_db"
    ).build()

    private val todoItemDao = db.toDoItemDao()

    fun getTodoItemsByDate(date: Long): Flow<List<ToDoItemEntity>> {
        return todoItemDao.getTodoItemsByDate(date)
    }

    suspend fun addTodoItem(newItem: ToDoItemEntity) {
        todoItemDao.insert(newItem)
    }
}