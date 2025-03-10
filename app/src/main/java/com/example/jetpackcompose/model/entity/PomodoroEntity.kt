package com.example.jetpackcompose.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro_sessions")
data class PomodoroSessionEntity(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val date: Long,
    val isCompleted: Boolean = false
)