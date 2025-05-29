package com.example.modernclock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TimeInfo(
    val time: String,
    val date: String,
    val dayOfWeek: String,
    val amPm: String
)

class ClockViewModel : ViewModel() {
    private val _currentTime = MutableStateFlow(LocalDateTime.now())
    val currentTime: StateFlow<LocalDateTime> = _currentTime.asStateFlow()
    
    private val _countdownTime = MutableStateFlow(0L)
    val countdownTime: StateFlow<Long> = _countdownTime.asStateFlow()
    
    var isCountdownActive by mutableStateOf(false)
        private set
    
    var timeInfo by mutableStateOf(getCurrentTimeInfo())
        private set
    
    init {
        startClock()
    }

    private fun getCurrentTimeInfo(): TimeInfo {
        val now = LocalDateTime.now()
        return TimeInfo(
            time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            dayOfWeek = now.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            amPm = now.format(DateTimeFormatter.ofPattern("a"))
        )
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                _currentTime.value = LocalDateTime.now()
                timeInfo = getCurrentTimeInfo()
                if (isCountdownActive && _countdownTime.value > 0) {
                    _countdownTime.value--
                }
                delay(1000)
            }
        }
    }

    fun startCountdown(minutes: Int) {
        _countdownTime.value = minutes * 60L
        isCountdownActive = true
    }

    fun stopCountdown() {
        isCountdownActive = false
        _countdownTime.value = 0
    }

    fun formatCountdownTime(): String {
        val hours = _countdownTime.value / 3600
        val minutes = (_countdownTime.value % 3600) / 60
        val seconds = _countdownTime.value % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
} 