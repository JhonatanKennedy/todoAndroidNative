package com.example.todolist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.db.dao.ToDoItemDao
import com.example.todolist.db.entity.ToDoItemEntity

@Database(entities = [ToDoItemEntity::class], version = 1, exportSchema = false)
abstract class ToDoItemDB: RoomDatabase() {
    abstract fun toDoItemDao(): ToDoItemDao

}