package com.example.jetpackcompose.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetpackcompose.model.entity.PomodoroSessionEntity


@Dao
interface PomodoroDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPomodoro(pomodoroSessionEntity: PomodoroSessionEntity)

    @Query("SELECT COUNT(*) FROM pomodoro_sessions WHERE date >= :startDate")
    suspend fun getPomodoroCountSince(startDate: Long): Int

    @Query("DELETE FROM pomodoro_sessions WHERE date < :threshold")
    suspend fun deleteOldPomodoros(threshold: Long)

}






























































































































