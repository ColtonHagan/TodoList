package com.example.todo

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val todoList = mutableListOf<ToDo>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TodoAdapter
    private lateinit var addTaskButton: ImageButton
    private lateinit var database: ToDoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(
            this,
            ToDoDatabase::class.java,
            "todo_database"
        ).fallbackToDestructiveMigration().build()

        GlobalScope.launch(Dispatchers.IO) {
            todoList.addAll(database.toDoDao().getAllTasks())
        }

        val popUpWindow = PopUpWindow(this, todoList, database)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapter(todoList, popUpWindow)
        recyclerView.adapter = adapter

        addTaskButton = findViewById(R.id.add)
        addTaskButton.setOnClickListener {
            popUpWindow.showPopUpWindow(-1, adapter)
        }
    }

}
