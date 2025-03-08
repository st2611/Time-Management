package com.example.jetpackcompose.repository

import com.example.jetpackcompose.model.dao.TaskDao
import com.example.jetpackcompose.model.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    suspend fun insertTask(task: TaskEntity) = taskDao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)

    fun getInCompletedTaskCount(): Flow<Int> = taskDao.getInCompletedTaskCount()

    fun getCompletedTaskCount(): Flow<Int> = taskDao.getCompletedTaskCount()

    suspend fun getOldestCompletedTask(): TaskEntity? {
        return taskDao.getOldestCompletedTask()
    }


}