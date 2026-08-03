package com.yourname.tracker

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*

@Composable
actual fun VoiceRecordingButton(onCommandRecognized: (String) -> Unit) {
    var isRecording by remember { mutableStateOf(false) }

    Button(onClick = {
        if (!isRecording) {
            // Получаем доступ к браузерному API распознавания речи
            val speechRecognition = js("window.SpeechRecognition || window.webkitSpeechRecognition")

            if (speechRecognition != null) {
                val recognition = js("new (window.SpeechRecognition || window.webkitSpeechRecognition)()")
                recognition.lang = "ru-RU" // Устанавливаем русский язык

                // Обработка успешного распознавания
                recognition.onresult = { event: dynamic ->
                    val transcript = event.results[0][0].transcript as String
                    onCommandRecognized(transcript)
                    isRecording = false
                }

                // Обработка ошибок и завершения
                recognition.onerror = {
                    println("Ошибка распознавания речи")
                    isRecording = false
                }
                recognition.onend = {
                    isRecording = false
                }

                recognition.start()
                isRecording = true
            } else {
                println("Web Speech API не поддерживается в этом браузере.")
            }
        }
    }) {
        Text(if (isRecording) "Слушаю..." else "Голосовая команда")
    }
}