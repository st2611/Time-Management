package com.example.jetpackcompose.screen.pomodoro

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetpackcompose.R
import com.example.jetpackcompose.constval.FORMAT_TIME_POMODORO
import com.example.jetpackcompose.constval.URL_POMODORO
import com.example.jetpackcompose.viewcommon.CommonButton
import com.example.jetpackcompose.viewmodel.PomodoroViewModel

@SuppressLint("DefaultLocale")
@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = hiltViewModel()) {
    val pomodoroCount by viewModel.pomodoroCount.collectAsState()
    val selectedDays by viewModel.selectedDays.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    val daysOptions = listOf(1, 3, 7, 10)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        
        Spacer(modifier = Modifier.height(100.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(4.dp, Color.Black, shape = RoundedCornerShape(12.dp))
                .padding(4.dp)
                .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = String.format(FORMAT_TIME_POMODORO, timeLeft / 60, timeLeft % 60),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }

        CommonButton(
            text = stringResource(id = if (!isRunning) R.string.start_pomodoro else R.string.stop_pomodoro),
            onClick = {
                if (!isRunning) viewModel.startPomodoro() else viewModel.stopPomodoro()
            },
            modifier = Modifier.width(300.dp)
        )

        Text("${stringResource(id = R.string.completed_pomodoro)} $pomodoroCount", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)

        Box (
            modifier = Modifier.width(200.dp),
        ) {

            CommonButton(
                text = stringResource(id = R.string.last_day, selectedDays),
                onClick = { expanded = true },
            )

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false },modifier = Modifier.align(Alignment.BottomCenter)) {
                daysOptions.forEach { days ->
                    DropdownMenuItem(
                        text = { Text("$days ${stringResource(id = R.string.day)}") },
                        onClick = {
                            viewModel.onDaysSelected(days)
                            expanded = false
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(200.dp))

        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.what_is_pomodoro_technique)),
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline
            ),
            onClick = {
                val url = URL_POMODORO
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
    }
}



