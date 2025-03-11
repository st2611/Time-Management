package com.example.jetpackcompose.screen.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.constval.FORMAT_DATE
import com.example.jetpackcompose.model.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(task: TaskEntity, currentTime: Long, onCompleted: () -> Unit, onDelete: () -> Unit) {

    val observedCurrentTime by remember {
        derivedStateOf { if (task.isCompleted == 0) currentTime else 0L}
    }

    val isOverdue = task.dueDate != null && task.dueDate < observedCurrentTime && task.isCompleted == 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(enabled = !isOverdue) { },
        colors = CardDefaults.cardColors(
            containerColor = when {
                task.isCompleted == 1 -> Color(0xFFA5D6A7)
                isOverdue -> Color(0xFFFFCDD2)
                else -> Color.White
            }
        )
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier
                    .weight(0.75f)
                    .padding(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${task.priority}",
                        color = if (isOverdue) Color.Red else Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(0.2f)
                    )

                    Column(modifier = Modifier.weight(0.8f)) {
                        Text(
                            text = "${stringResource(id = R.string.title)}: ${task.title}",
                            fontWeight = FontWeight.Bold
                        )

                        task.dueDate?.let {
                            val formattedDate =
                                SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).format(Date(it))
                            Text(
                                text = "${stringResource(id = R.string.deadline)} $formattedDate",
                                color = if (isOverdue) Color.Red else Color.Black
                            )
                        }
                    }
                }

                task.description?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.25f)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(

                    onClick = onCompleted,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isOverdue,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = if (task.isCompleted == 1) Color.White else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = task.isCompleted == 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}