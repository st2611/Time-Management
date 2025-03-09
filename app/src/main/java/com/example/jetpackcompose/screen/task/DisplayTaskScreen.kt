package com.example.jetpackcompose.screen.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.jetpackcompose.R
import com.example.jetpackcompose.constval.LIMIT_TASK
import com.example.jetpackcompose.toast.showCustomToast
import com.example.jetpackcompose.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTaskScreen(
    viewModel: TaskViewModel,
    onNavigateToAddTask: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val incompleteTaskCount by viewModel.incompleteTaskCount.collectAsState()
    val completedTaskCount by viewModel.completedTaskCount.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val context = LocalContext.current

    Column {
        TopAppBar(
            title = { Text(stringResource(id = R.string.task_list)) },
            actions = {
                IconButton(onClick = {
                    if (incompleteTaskCount == LIMIT_TASK) {
                        context.showCustomToast(
                            context.getString(R.string.the_number_of_task_reach_limit),
                            isLong = true
                        )
                    } else {
                        onNavigateToAddTask.invoke()
                    }
                }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_task)
                    )
                }
            }
        )

        LazyColumn {
            items(tasks) { task ->
                TaskItem(
                    task,
                    currentTime = currentTime,
                    onCompleted = { viewModel.markAsCompleted(task, completedTaskCount) },
                    onDelete = { viewModel.deleteTask(task) })
            }
        }
    }
}


