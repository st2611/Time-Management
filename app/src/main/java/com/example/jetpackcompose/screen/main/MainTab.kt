package com.example.jetpackcompose.screen.main

import androidx.annotation.StringRes
import com.example.jetpackcompose.R

enum class MainTab(@StringRes val title: Int, val iconRes: Int) {
    POMODORO(R.string.pomodoro, R.drawable.baseline_access_time_24),
    TASKS(R.string.tasks, R.drawable.baseline_task_alt_24),
    STATS(R.string.stats, R.drawable.baseline_pie_chart_24)
}