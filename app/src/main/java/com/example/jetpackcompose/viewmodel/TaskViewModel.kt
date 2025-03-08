package com.example.jetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.model.entity.TaskEntity
import com.example.jetpackcompose.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTask(title: String, description: String?, priority: Int, dueDate: Long?) =
        viewModelScope.launch {
            val newTask = TaskEntity(
                title = title,
                description = description,
                priority = priority,
                dueDate = dueDate
            )
            repository.insertTask(newTask)
        }

    fun markAsCompleted(task: TaskEntity, completedCount: Int) = viewModelScope.launch {
        if (completedCount >= 3) {
            val oldestCompletedTask = repository.getOldestCompletedTask()
            oldestCompletedTask?.let {
                repository.deleteTask(it)
            }
        }
        repository.updateTask(task.copy(isCompleted = 1))
    }

    fun deleteTask(task: TaskEntity) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    val completedTaskCount: StateFlow<Int> = repository.getCompletedTaskCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val incompleteTaskCount: StateFlow<Int> = repository.getInCompletedTaskCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)
}