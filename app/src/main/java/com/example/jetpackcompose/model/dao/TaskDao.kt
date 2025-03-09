package com.example.jetpackcompose.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.jetpackcompose.constval.COMPLETED_TASK
import com.example.jetpackcompose.constval.INCOMPLETE_TASK
import com.example.jetpackcompose.model.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query(
        """
    SELECT * FROM tasks 
    ORDER BY 
        isCompleted ASC, 
        CASE 
            WHEN isCompleted = 0 AND dueDate < strftime('%s', 'now') * 1000 THEN 1 
            ELSE 0 
        END ASC,
        priority ASC,
        dueDate ASC
        
"""
    )
    fun getAllTasks(): Flow<List<TaskEntity>>


    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): TaskEntity?

    @Query("SELECT COUNT (*) FROM tasks WHERE isCompleted = $INCOMPLETE_TASK")
    fun getInCompletedTaskCount(): Flow<Int>

    @Query("SELECT COUNT (*) FROM tasks WHERE isCompleted = $COMPLETED_TASK")
    fun getCompletedTaskCount(): Flow<Int>

    @Query("SELECT * FROM tasks WHERE isCompleted = $COMPLETED_TASK ORDER BY dueDate ASC LIMIT 1")
    suspend fun getOldestCompletedTask(): TaskEntity?
}