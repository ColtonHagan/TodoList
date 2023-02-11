package com.example.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class ToDo(
    var task : String,
    var isComplete : Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0)
