package com.example.jetpackcompose.screen.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jetpackcompose.R

enum class MainTab (@StringRes val title:Int, val icon: ImageVector) {
    TASKS(R.string.tasks, Icons.Default.Menu),
    POMODORO(R.string.pomodoro, Icons.Default.DateRange),
    STATS(R.string.stats, Icons.Default.Build)
}