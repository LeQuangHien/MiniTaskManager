package com.hien.le.minitaskmanager.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.hien.le.minitaskmanager.data.datasource.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM taskentity")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM taskentity WHERE id = :id")
    suspend fun loadTaskById(id: Int): TaskEntity

    @Query("DELETE FROM taskentity WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    @Query("UPDATE taskentity SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateTaskIsCompleted(id: Int, isCompleted: Boolean)

    @Query("UPDATE taskentity SET title = :title, description = :description WHERE id = :id")
    suspend fun updateTask(id: Int, title: String, description: String)

    @Query("INSERT INTO taskentity (title, description) VALUES (:title, :description)")
    suspend fun insertTask(title: String, description: String)
}