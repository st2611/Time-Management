package com.example.jetpackcompose.repository

import com.example.jetpackcompose.model.dao.PomodoroDao
import com.example.jetpackcompose.model.entity.PomodoroSessionEntity
import javax.inject.Inject

class PomodoroRepository @Inject constructor(private val pomodoroDao: PomodoroDao) {

    suspend fun insertPomodoro(pomodoroSessionEntity: PomodoroSessionEntity) =
        pomodoroDao.insertPomodoro(pomodoroSessionEntity)

    suspend fun getPomodoroCountSince(startDate: Long) =
        pomodoroDao.getPomodoroCountSince(startDate)

    suspend fun deleteOldPomodoros(threshold: Long) =
        pomodoroDao.deleteOldPomodoros(threshold)

}