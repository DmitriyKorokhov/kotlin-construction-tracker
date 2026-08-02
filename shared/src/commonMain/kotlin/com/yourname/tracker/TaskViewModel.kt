package com.yourname.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()

    // ВАЖНО: Убедитесь, что скобки <List<Task>> написаны слитно, без пробелов!
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun addTask(description: String) {
        viewModelScope.launch {
            val newTask = Task(
                taskNumber = _tasks.value.size + 1,
                description = description,
                status = "В процессе"
            )
            repository.saveTask(newTask)
        }
    }

    fun processVoiceCommand(command: String) {
        val text = command.lowercase()

        // ВАЖНО: Выводим услышанный текст в консоль для отладки
        println("РАСПОЗНАННЫЙ ТЕКСТ: $text")

        // Словарь для перевода текстовых чисел в цифры
        val wordToNumber = mapOf(
            "один" to 1, "одна" to 1, "первая" to 1, "первую" to 1,
            "два" to 2, "две" to 2, "вторая" to 2, "вторую" to 2,
            "три" to 3, "третья" to 3, "третью" to 3,
            "четыре" to 4, "четвертая" to 4, "четвертую" to 4,
            "пять" to 5, "пятая" to 5, "пятую" to 5
        )

        var taskNum: Int? = null

        // 1. Сначала пытаемся найти обычную цифру (1, 2, 3...)
        val numberMatch = Regex("\\d+").find(text)
        if (numberMatch != null) {
            taskNum = numberMatch.value.toInt()
        } else {
            // 2. Если цифр нет, ищем текстовые слова ("один", "вторая"...)
            for ((word, number) in wordToNumber) {
                if (text.contains(word)) {
                    taskNum = number
                    break
                }
            }
        }

        // Если удалось найти номер задачи (хоть цифрой, хоть словом)
        if (taskNum != null) {
            val newStatus = when {
                text.contains("выполнен") || text.contains("готов") || text.contains("сделан") -> "Выполнена"
                text.contains("корректировк") || text.contains("ошибк") -> "Требует корректировки"
                text.contains("процесс") || text.contains("работ") -> "В процессе"
                else -> null
            }

            println("ПОНЯЛ КОМАНДУ: Задача $taskNum -> Статус: $newStatus")

            if (newStatus != null) {
                // Ищем задачу и обновляем
                val task = _tasks.value.find { it.taskNumber == taskNum }
                if (task != null) {
                    viewModelScope.launch {
                        repository.saveTask(task.copy(status = newStatus))
                        println("УСПЕХ: Задача обновлена в базе!")
                    }
                } else {
                    println("ОШИБКА: Задача с номером $taskNum не найдена в списке.")
                }
            }
        }
    }
}