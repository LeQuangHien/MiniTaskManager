package com.hien.le.minitaskmanager.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hien.le.minitaskmanager.data.datasource.database.dao.TaskDao
import com.hien.le.minitaskmanager.data.datasource.database.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}