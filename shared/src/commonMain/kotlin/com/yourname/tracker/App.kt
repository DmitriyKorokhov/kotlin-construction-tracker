package com.yourname.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue // 2. Обязательный импорт для делегата 'by'
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun App() {
    MaterialTheme {
        val viewModel: TaskViewModel = viewModel { TaskViewModel() }
        val tasks by viewModel.tasks.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Text(
                text = "Задачи на стройке",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.addTask("Установить опалубку") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить тестовую задачу")
            }

            // ДОБАВЛЯЕМ ГОЛОСОВУЮ КНОПКУ:
            VoiceRecordingButton(
                onCommandRecognized = { text ->
                    viewModel.processVoiceCommand(text)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(task)
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Задача #${task.taskNumber}: ${task.description}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Статус: ${task.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (task.status == "В процессе") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        }
    }
}