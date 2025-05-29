package com.example.modernclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modernclock.components.AnalogClock
import com.example.modernclock.components.FrostedCard
import com.example.modernclock.components.GradientBackground
import com.example.modernclock.ui.theme.ModernClockTheme
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModernClockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ClockScreen()
                }
            }
        }
    }
}

@Composable
fun ClockScreen(
    viewModel: ClockViewModel = viewModel()
) {
    val currentTime by viewModel.currentTime.collectAsState()
    val countdownTime by viewModel.countdownTime.collectAsState()
    var showCountdownDialog by remember { mutableStateOf(false) }
    var countdownMinutes by remember { mutableStateOf("") }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FrostedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnalogClock(
                        time = currentTime.toLocalTime(),
                        modifier = Modifier.size(300.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text(
                        text = currentTime.format(DateTimeFormatter.ofPattern("a", Locale.ENGLISH)),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = currentTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text(
                        text = "星期${currentTime.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.CHINESE)}",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            FrostedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (countdownTime > 0) {
                            "倒计时: ${countdownTime / 60}:${String.format("%02d", countdownTime % 60)}"
                        } else {
                            "倒计时"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { showCountdownDialog = true },
                            enabled = countdownTime <= 0,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text("开始倒计时")
                        }
                        
                        if (countdownTime > 0) {
                            Button(
                                onClick = { viewModel.stopCountdown() },
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Text("停止倒计时")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCountdownDialog) {
        AlertDialog(
            onDismissRequest = { showCountdownDialog = false },
            title = { Text("设置倒计时") },
            text = {
                OutlinedTextField(
                    value = countdownMinutes,
                    onValueChange = { countdownMinutes = it },
                    label = { Text("分钟") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        countdownMinutes.toIntOrNull()?.let { minutes ->
                            viewModel.startCountdown(minutes)
                        }
                        showCountdownDialog = false
                        countdownMinutes = ""
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCountdownDialog = false
                        countdownMinutes = ""
                    }
                ) {
                    Text("取消")
                }
            }
        )
    }
}