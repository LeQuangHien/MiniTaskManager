package com.hien.le.minitaskmanager.ui.screens.task

import androidx.activity.result.launch
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskScreenState(
    val tasks: List<Task> = emptyList(),
    val deletedTask: Task? = null,
    val isUndoShown: Boolean = false,
)

data class Task(
    val taskId: Int,
    val title: String,
    val checked: Boolean
)

class TaskViewModel : ViewModel() {
    private val _state = MutableStateFlow(TaskScreenState())
    val state = _state.asStateFlow()

    private var taskIdCounter = 0

    fun onTaskCheckedChange(taskId: Int, checked: Boolean) {
        _state.value = _state.value.copy(
            tasks = _state.value.tasks.map {
                if (it.taskId == taskId) {
                    it.copy(checked = checked)
                } else {
                    it
                }
            })
    }

    fun onTaskAdd() {
        viewModelScope.launch {
            taskIdCounter++
            val newTask = Task(taskId = taskIdCounter, title = "Task $taskIdCounter", checked = false)
            _state.update { it.copy(tasks = it.tasks + newTask) }
        }
    }

    fun onDeleteTask(taskId: Int) {
        viewModelScope.launch {
            val taskToDelete = _state.value.tasks.first { it.taskId == taskId }
            _state.update {
                it.copy(
                    tasks = it.tasks.filter { task -> task.taskId != taskId },
                    deletedTask = taskToDelete,
                    isUndoShown = true
                )
            }
        }
    }

    fun onUndoDeleteTask() {
        viewModelScope.launch {
            val deletedTask = _state.value.deletedTask ?: return@launch
            _state.update {
                it.copy(tasks = it.tasks + deletedTask, deletedTask = null, isUndoShown = false)
            }
        }
    }

    fun onUndoComplete() {
        _state.update { it.copy(deletedTask = null, isUndoShown = false) }
    }
}