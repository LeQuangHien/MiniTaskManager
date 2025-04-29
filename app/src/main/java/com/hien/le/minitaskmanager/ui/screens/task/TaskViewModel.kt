package com.hien.le.minitaskmanager.ui.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hien.le.minitaskmanager.data.datasource.database.entity.TaskEntity
import com.hien.le.minitaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskScreenState(
    val deletedTask: Task? = null,
    val isUndoShown: Boolean = false,
)

data class Task(
    val taskId: Int,
    val title: String,
    val description: String,
    val checked: Boolean
)

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val tasks: StateFlow<List<Task>> = taskRepository.getAllTasks()
        .distinctUntilChanged()
        .map { taskEntities ->
            taskEntities.map { taskEntity ->
                taskEntity.toTask()
            }
        }
        .flowOn(ioDispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow(TaskScreenState())
    val state = _state.asStateFlow()

    private var taskIdCounter = 0

    fun onTaskCheckedChange(taskId: Int, checked: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            taskRepository.updateTaskIsCompleted(taskId, checked)
        }
    }

    fun onTaskAdd() {
        viewModelScope.launch(ioDispatcher) {
            val taskId = taskIdCounter++
            taskRepository.insertTask("Task $taskId", "Description $taskId")
        }
    }

    fun onDeleteTask(taskId: Int) {
        viewModelScope.launch(ioDispatcher) {
            val taskToDelete = taskRepository.getTaskById(taskId)
            taskRepository.deleteTaskById(taskId)
            _state.update {
                it.copy(
                    deletedTask = taskToDelete.toTask(),
                    isUndoShown = true
                )
            }
        }
    }

    fun TaskEntity.toTask() = Task(
        taskId = id,
        title = title,
        description = description,
        checked = isCompleted
    )


    fun onUndoDeleteTask() {
        viewModelScope.launch(ioDispatcher) {
            val deletedTask = _state.value.deletedTask ?: return@launch
            taskRepository.insertTask(deletedTask.title, deletedTask.description)
        }
    }

    fun onUndoComplete() {
        _state.update { it.copy(deletedTask = null, isUndoShown = false) }
    }
}