package com.example.jetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.constval.COMPLETED_TASK
import com.example.jetpackcompose.model.entity.TaskEntity
import com.example.jetpackcompose.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentTime = MutableStateFlow(System.currentTimeMillis())
    val currentTime: StateFlow<Long> = _currentTime.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(60000L)
                _currentTime.value = System.currentTimeMillis()
                deleteOldCompletedTasks()
            }
        }

        viewModelScope.launch {
            deleteOldCompletedTasks()
        }
    }

    private suspend fun deleteOldCompletedTasks() {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_MONTH, -10)
        }
        val threshold = calendar.timeInMillis

        repository.deleteOldCompletedTasks(threshold)
    }


    fun addTask(
        title: String,
        description: String?,
        priority: Int,
        dueDate: Long?,
        dateCompleted: Long?
    ) =
        viewModelScope.launch {
            val newTask = TaskEntity(
                title = title,
                description = description,
                priority = priority,
                dueDate = dueDate,
                dateCompleted = dateCompleted
            )
            repository.insertTask(newTask)
        }

    fun markAsCompleted(task: TaskEntity) = viewModelScope.launch {
        repository.updateTask(
            task.copy(
                isCompleted = COMPLETED_TASK,
                dateCompleted = Calendar.getInstance().timeInMillis
            )
        )
    }

    fun deleteTask(task: TaskEntity) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    val incompleteTaskCount: StateFlow<Int> = repository.getInCompletedTaskCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)
}