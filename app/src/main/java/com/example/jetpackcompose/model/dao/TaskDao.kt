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

    @Query("SELECT COUNT (*) FROM tasks WHERE isCompleted = $INCOMPLETE_TASK")
    fun getInCompletedTaskCount(): Flow<Int>

    @Query("DELETE FROM tasks WHERE dateCompleted < :threshold")
    suspend fun deleteOldCompletedTasks(threshold: Long)

    @Query("""
    SELECT * FROM tasks 
    WHERE isCompleted = 1 
    AND dateCompleted >= (:currentTime - (:days * 24 * 60 * 60 * 1000))
""")
    fun getCompletedTasksInDays(currentTime: Long, days: Int): Flow<List<TaskEntity>>

}