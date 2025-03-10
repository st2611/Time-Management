package com.example.jetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.constval.TIME_OF_EACH_POMODORO
import com.example.jetpackcompose.constval.TIME_OF_ONE_DAY
import com.example.jetpackcompose.constval.TIME_OF_TEN_DAYS
import com.example.jetpackcompose.manager.DataStoreManager
import com.example.jetpackcompose.model.entity.PomodoroSessionEntity
import com.example.jetpackcompose.repository.PomodoroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val pomodoroRepository: PomodoroRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _pomodoroCount = MutableStateFlow(0)
    val pomodoroCount: StateFlow<Int> = _pomodoroCount.asStateFlow()

    private val _selectedDays = MutableStateFlow(1)
    val selectedDays: StateFlow<Int> = _selectedDays.asStateFlow()

    private val _timeLeft = MutableStateFlow(TIME_OF_EACH_POMODORO)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private var timerJob: Job? = null
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    init {
        loadStoredData()
    }

    private fun loadStoredData() {
        viewModelScope.launch {
            dataStoreManager.selectedDays.collect { days ->
                _selectedDays.value = days
                refreshPomodoroCount()
            }
        }

        viewModelScope.launch {
            dataStoreManager.pomodoroCount.collect { count ->
                _pomodoroCount.value = count
            }
        }
    }


    fun onDaysSelected(days: Int) {
        viewModelScope.launch {
            _selectedDays.value = days
            dataStoreManager.saveSelectedDays(days)
            refreshPomodoroCount()
        }
    }

    private fun refreshPomodoroCount() {
        viewModelScope.launch {
            val startDate = System.currentTimeMillis() - selectedDays.value * TIME_OF_ONE_DAY
            val count = pomodoroRepository.getPomodoroCountSince(startDate)
            _pomodoroCount.value = count
            dataStoreManager.savePomodoroCount(count)
        }
    }

    fun startPomodoro() {
        if (_isRunning.value) return

        _isRunning.value = true
        _timeLeft.value = TIME_OF_EACH_POMODORO

        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }
            completePomodoro()
        }
    }

    fun stopPomodoro() {
        timerJob?.cancel()
        _isRunning.value = false
        _timeLeft.value = TIME_OF_EACH_POMODORO
    }

    private fun completePomodoro() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            pomodoroRepository.insertPomodoro(PomodoroSessionEntity(date = currentTime))

            val threshold = currentTime - TIME_OF_TEN_DAYS
            pomodoroRepository.deleteOldPomodoros(threshold)

            refreshPomodoroCount()
            _isRunning.value = false
        }
    }
}
