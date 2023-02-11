package com.example.todo

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputLayout as TextInputLayout1

class PopUpWindow(private val context: Context, private val todoList: MutableList<ToDo>, private val database: ToDoDatabase) {
    private lateinit var addSubmitButton: ImageButton
    private lateinit var cancelButton: ImageButton
    private lateinit var popUpWindow: PopupWindow

    fun showPopUpWindow(index: Int, adapter: TodoAdapter) {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.createtask,null)
        val textInput = view.findViewById<TextInputLayout1>(R.id.task_input_layout)
        popUpWindow = PopupWindow(view, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true)

        if(index < 0) {
            textInput.hint = "Enter task"
        } else {
            textInput.hint = "Update task"
            textInput.editText!!.setText(todoList[index].task)
        }

        cancelButton = view.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener {
            popUpWindow.dismiss()
        }

        addSubmitButton = view.findViewById(R.id.add_task_button)
        addSubmitButton.setOnClickListener {
            if(index < 0) {
                addTask(textInput)
                adapter.notifyItemInserted(todoList.size-1)
            } else {
                updateTask(textInput,index)
                adapter.notifyItemChanged(index)
            }
        }
        popUpWindow.showAtLocation( (context as MainActivity).findViewById(R.id.main_activity), Gravity.CENTER,0,0)
    }

    private fun addTask(input: TextInputLayout1) {
        if (input.editText?.text.isNullOrBlank()) {
            input.error = "Please enter task"
        } else {
            val todo = ToDo(input.editText!!.text.toString());
            todoList.add(todo)
            GlobalScope.launch(Dispatchers.IO) {
                database.toDoDao().insertTask(todo)
            }
            popUpWindow.dismiss()
        }
    }

    private fun updateTask(input: TextInputLayout1, index : Int) {
        if (input.editText?.text.isNullOrBlank()) {
            input.error = "Task can not be blank"
        } else {
            val todo = todoList[index]
            val text = input.editText!!.text.toString()
            todo.task = text
            updateTask(todo)
            popUpWindow.dismiss()
        }
    }

    fun removeTask(todo: ToDo) {
        GlobalScope.launch(Dispatchers.IO) {
            database.toDoDao().deleteTask(todo)
        }
    }

    fun updateTask(todo: ToDo) {
        GlobalScope.launch(Dispatchers.IO) {
            database.toDoDao().updateTask(todo)
        }
    }
}