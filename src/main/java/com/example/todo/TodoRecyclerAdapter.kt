package com.example.todo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(private val todoList: MutableList<ToDo>, private val popUpWindow: PopUpWindow) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkbox: CheckBox = itemView.findViewById(R.id.task_completed)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.task_delete)
        val editBtn: ImageButton = itemView.findViewById(R.id.task_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todoitem, parent, false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todoList[position]
        holder.checkbox.text = todo.task
        holder.checkbox.isChecked = todo.isComplete
        holder.checkbox.setOnCheckedChangeListener { _, isChecked  ->
            Log.d("checked", ""+isChecked)
            todo.isComplete = isChecked
            popUpWindow.updateTask(todo)
            notifyItemChanged(position)
        }
        holder.deleteBtn.setOnClickListener {
            Log.d("Removing", "pos $position")
            todoList.removeAt(position)
            popUpWindow.removeTask(todo)
            notifyItemRemoved(position)
        }
        holder.editBtn.setOnClickListener {
            popUpWindow.showPopUpWindow(position, this)
            notifyItemChanged(position)
        }
    }
}
