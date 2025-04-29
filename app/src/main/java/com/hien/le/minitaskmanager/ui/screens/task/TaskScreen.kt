package com.hien.le.minitaskmanager.ui.screens.task

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskScreen(modifier: Modifier = Modifier, viewModel: TaskViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    TaskScreenScaffold(
        modifier = modifier,
        tasks = tasks,
        deletedTask = state.deletedTask,
        isUndoShown = state.isUndoShown,
        onCheckedChange = { taskId, checked -> viewModel.onTaskCheckedChange(taskId, checked) },
        onTaskAdd = { viewModel.onTaskAdd() },
        onTaskDelete = { viewModel.onDeleteTask(it) },
        onUndoDeleteTask = { viewModel.onUndoDeleteTask() },
        onUndoComplete = { viewModel.onUndoComplete() }
    )
}

@Composable
fun TaskScreenScaffold(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    deletedTask: Task?,
    isUndoShown: Boolean,
    onCheckedChange: (Int, Boolean) -> Unit,
    onTaskAdd: () -> Unit,
    onTaskDelete: (Int) -> Unit,
    onUndoDeleteTask: () -> Unit,
    onUndoComplete: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .height(50.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "My Tasks", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { onTaskAdd() }) {
                    Text(text = "Add Task")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        TaskList(
            modifier = Modifier.padding(innerPadding),
            tasks = tasks,
            onCheckedChange = onCheckedChange,
            onDelete = onTaskDelete
        )

        if (deletedTask != null && isUndoShown) {
            LaunchedEffect(deletedTask.taskId) {
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = "Task deleted",
                    actionLabel = "Undo",
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
                when (snackbarResult) {
                    SnackbarResult.Dismissed -> onUndoComplete()
                    SnackbarResult.ActionPerformed -> onUndoDeleteTask()
                }
            }
        }
    }
}

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    onCheckedChange: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = tasks, key = { it.taskId }) { task ->
            TaskItem(
                title = task.title,
                checked = task.checked,
                onCheckedChange = { checked -> onCheckedChange(task.taskId, checked) },
                onDelete = { onDelete(task.taskId) })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Column {
        Row(
            modifier = modifier
                .combinedClickable(
                    onClick = { onCheckedChange(!checked) },
                    onLongClick = onDelete
                )
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, modifier = Modifier.weight(1f))
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
    }

}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(title = "Task 1", onCheckedChange = {}, onDelete = {})
}

@Preview
@Composable
fun TaskListPreview() {
    val tasks = listOf(
        Task(taskId = 1, title = "Task 1", description = "Description 1", checked = false),
        Task(taskId = 2, title = "Task 2", description = "Description 2", checked = true)
    )
    TaskList(tasks = tasks, onCheckedChange = { _, _ -> }, onDelete = {})
}


@Preview
@Composable
fun TaskScreenScaffoldPreview() {
    val tasks = listOf(
        Task(taskId = 1, title = "Task 1", description = "Description 1", checked = false),
        Task(taskId = 2, title = "Task 2", description = "Description 2", checked = true)
    )
    TaskScreenScaffold(
        tasks = tasks,
        deletedTask = tasks[0],
        isUndoShown = true,
        onCheckedChange = { _, _ -> },
        onTaskAdd = {},
        onTaskDelete = { _ -> },
        onUndoDeleteTask = {},
        onUndoComplete = {})
}