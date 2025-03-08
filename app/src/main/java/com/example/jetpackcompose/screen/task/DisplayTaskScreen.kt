package com.example.jetpackcompose.screen.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.const.FORMAT_DATE
import com.example.jetpackcompose.model.entity.TaskEntity
import com.example.jetpackcompose.toast.showCustomToast
import com.example.jetpackcompose.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTaskScreen(
    viewModel: TaskViewModel,
    onNavigateToAddTask: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val incompleteTaskCount by viewModel.incompleteTaskCount.collectAsState()
    val completedTaskCount by viewModel.completedTaskCount.collectAsState()
    val context = LocalContext.current

    Column {
        TopAppBar(
            title = { Text(stringResource(id = R.string.task_list)) },
            actions = {
                IconButton(onClick = {
                    if (incompleteTaskCount == 20) {
                        context.showCustomToast(context.getString(R.string.the_number_of_task_reach_limit), isLong = true)
                    } else {
                        onNavigateToAddTask.invoke()
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_task))
                }
            }
        )

        LazyColumn {
            items(tasks) { task ->
                TaskItem(task, onCompleted = { viewModel.markAsCompleted(task, completedTaskCount) }, onDelete = {viewModel.deleteTask(task)})
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskEntity, onCompleted: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted == 1) Color.LightGray else Color.White
        )
    ) {
        Row {
            Column(modifier = Modifier
                .padding(16.dp)
                .weight(1f)) {
                Text(task.title, fontWeight = FontWeight.Bold)
                Text("${stringResource(id = R.string.priority)} ${task.priority}")
                task.dueDate?.let {
                    val formattedDate = SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).format(Date(it))
                    Text("${stringResource(id = R.string.deadline)} $formattedDate")
                }
                task.description?.let { Text(it, fontSize = 14.sp, color = Color.Gray) }
            }
            Button(
                onClick = { onCompleted() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = if (task.isCompleted == 1) Color(0xFF4CAF50) else Color.Gray
                )
            }
            Button(
                onClick = { onDelete.invoke() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}