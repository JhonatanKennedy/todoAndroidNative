package com.example.todolist.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todolist.db.entity.ToDoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemDao {
    @Insert
    suspend fun insert(item: ToDoItemEntity)

    @Query("SELECT * FROM todo_items WHERE date = :date")
    fun getTodoItemsByDate(date: Long): Flow<List<ToDoItemEntity>>
}