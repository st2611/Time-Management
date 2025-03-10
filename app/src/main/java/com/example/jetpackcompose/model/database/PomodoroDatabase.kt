package com.example.jetpackcompose.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jetpackcompose.model.dao.PomodoroDao
import com.example.jetpackcompose.model.entity.PomodoroSessionEntity

@Database(entities = [PomodoroSessionEntity::class], version = 1, exportSchema = false)
abstract class PomodoroDatabase : RoomDatabase() {
    abstract fun pomodoroDao(): PomodoroDao

    companion object {
        @Volatile
        private var INSTANCE: PomodoroDatabase? = null

        fun getDatabase(context: Context): PomodoroDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PomodoroDatabase::class.java,
                    "pomodoro_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
