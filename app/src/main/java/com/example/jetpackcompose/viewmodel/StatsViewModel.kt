package com.example.jetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.model.entity.TaskEntity
import com.example.jetpackcompose.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(taskRepository: TaskRepository) :ViewModel() {

    private val completedTasksInLast10Days: StateFlow<List<TaskEntity>> =
        taskRepository.getCompletedTasksInDays(10)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val priorityDistribution: StateFlow<Map<Int, Int>> = completedTasksInLast10Days
        .map { tasks ->
            tasks.groupingBy { it.priority }.eachCount()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    val dailyCompletionStats: StateFlow<Map<String, Int>> = completedTasksInLast10Days
        .map { tasks ->
            val calendar = Calendar.getInstance()

            (0..9).associate { i ->
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.DAY_OF_YEAR, -i)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val startOfDay = calendar.timeInMillis
                val endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1
                val dateKey = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date(startOfDay))

                dateKey to tasks.count { task ->
                    task.dateCompleted != null &&
                            task.dateCompleted >= startOfDay &&
                            task.dateCompleted <= endOfDay
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())
}