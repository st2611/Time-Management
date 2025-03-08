package com.example.jetpackcompose.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.jetpackcompose.screen.pomodoro.PomodoroScreen
import com.example.jetpackcompose.screen.statscreen.StatsScreen
import com.example.jetpackcompose.screen.task.TaskScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val tabs = MainTab.entries.toTypedArray()
    val pagerState = rememberPagerState(pageCount = {tabs.size})
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(WindowInsets.statusBars.asPaddingValues())) {
        //Tab Layout
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { Text(text = stringResource(id = tab.title)) },
                    icon = { Icon(tab.icon, contentDescription = stringResource(id = tab.title)) }
                )
            }
        }

        // Content of each tab
        HorizontalPager(state = pagerState) { page ->
            when(tabs[page]) {
                MainTab.TASKS -> TaskScreen(navController = navController)
                MainTab.POMODORO -> PomodoroScreen()
                MainTab.STATS -> StatsScreen()
            }
        }
    }
}