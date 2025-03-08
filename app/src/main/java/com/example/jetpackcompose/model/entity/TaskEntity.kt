package com.example.jetpackcompose.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val priority: Int, // 1 = High, 2 = Medium, 3 = Low
    val isCompleted: Int = 0, // 0: working, 1: completed
    val dueDate: Long? // Timestamp
)