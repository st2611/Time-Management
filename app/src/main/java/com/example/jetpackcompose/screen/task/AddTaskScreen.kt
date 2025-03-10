package com.example.jetpackcompose.screen.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.constval.FORMAT_DATE
import com.example.jetpackcompose.toast.showCustomToast
import com.example.jetpackcompose.viewcommon.CommonButton
import com.example.jetpackcompose.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddTaskScreen(
    viewModel: TaskViewModel,
    onNavigateToDisplayTask: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableIntStateOf(1) }
    var dueDate by remember { mutableStateOf<Long?>(null) }

    val calendar = Calendar.getInstance()
    val formattedDueDate = dueDate?.let {
        SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).format(Date(it))
    } ?: stringResource(id = R.string.select_date_time)
    val isFormValid = title.isNotBlank() && description.isNotBlank() && dueDate != null
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(id = R.string.title)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(id = R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(id = R.string.priority))
            Spacer(modifier = Modifier.width(8.dp))
            DropdownMenuComponent(
                selectedPriority = priority,
                onPrioritySelected = { priority = it }
            )
        }

        CommonButton(
            text = formattedDueDate,
            onClick = {
                showDatePicker(context, calendar) { selectedTime ->
                    dueDate = selectedTime
                }
            }
        )

        CommonButton(
            text = stringResource(id = R.string.add_task),
            onClick = {
                if (isFormValid) {
                    viewModel.addTask(title, description.ifBlank { null }, priority, dueDate)
                    onNavigateToDisplayTask()
                } else {
                    context.showCustomToast(context.getString(R.string.complete_all_the_information), isLong = true)
                }
            }
        )

        CommonButton(
            text = stringResource(id = R.string.back),
            onClick = { onNavigateToDisplayTask() }
        )
    }
}

@Composable
fun DropdownMenuComponent(selectedPriority: Int, onPrioritySelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val priorities = listOf(1, 2, 3)

    Box {
        Button(onClick = { expanded = true },  colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White
        )) {
            Text("${stringResource(id = R.string.level)} $selectedPriority")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            priorities.forEach { priority ->
                DropdownMenuItem(text = { Text("${stringResource(id = R.string.level)} $priority") }, onClick = {
                    onPrioritySelected(priority)
                    expanded = false
                })
            }
        }
    }
}

fun showDatePicker(context: Context, calendar: Calendar, onDateTimeSelected: (Long) -> Unit) {
    DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day)
            showTimePicker(context, calendar, onDateTimeSelected)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun showTimePicker(context: Context, calendar: Calendar, onDateTimeSelected: (Long) -> Unit) {
    TimePickerDialog(
        context,
        { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            onDateTimeSelected(calendar.timeInMillis)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
}


