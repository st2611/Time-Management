package com.example.jetpackcompose.screen.task

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetpackcompose.const.ADD_TASK_SCREEN
import com.example.jetpackcompose.const.DISPLAY_TASK_SCREEN
import com.example.jetpackcompose.viewmodel.TaskViewModel

@Composable
fun TaskScreen(viewModel: TaskViewModel = hiltViewModel(), navController: NavHostController) {
    NavHost(navController, startDestination = DISPLAY_TASK_SCREEN) {
        composable(DISPLAY_TASK_SCREEN) {
            DisplayTaskScreen(
                viewModel = viewModel,
                onNavigateToAddTask = { navController.navigate(ADD_TASK_SCREEN) })
        }
        composable(ADD_TASK_SCREEN) {
            AddTaskScreen(
                viewModel = viewModel,
                onNavigateToDisplayTask = {navController.popBackStack()}
            )
        }
    }
}