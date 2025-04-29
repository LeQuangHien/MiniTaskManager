package com.hien.le.minitaskmanager.data.repository

import com.hien.le.minitaskmanager.data.datasource.database.dao.TaskDao

class TaskRepository(
    private val taskDao: TaskDao,
) {
    fun getAllTasks() = taskDao.getAllTasks()

    suspend fun getTaskById(id: Int) = taskDao.loadTaskById(id)

    suspend fun deleteTaskById(id: Int) = taskDao.deleteTaskById(id)

    suspend fun updateTaskIsCompleted(id: Int, isCompleted: Boolean) =
        taskDao.updateTaskIsCompleted(id, isCompleted)

    suspend fun updateTask(id: Int, title: String, description: String) =
        taskDao.updateTask(id, title, description)

    suspend fun insertTask(title: String, description: String) = taskDao.insertTask(title, description)
}