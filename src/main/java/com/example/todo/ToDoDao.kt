package com.example.todo

import androidx.room.*
import kotlin.collections.List

@Dao
interface ToDoDao {
    @Insert
    suspend fun insertTask(todo: ToDo)

    @Update
    suspend fun updateTask(todo: ToDo)

    @Delete
    suspend fun deleteTask(todo: ToDo)

    @Query("SELECT * FROM todo_table")
    suspend fun getAllTasks(): List<ToDo>
}