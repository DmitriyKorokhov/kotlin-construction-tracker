package com.yourname.tracker

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun VoiceRecordingButton(onCommandRecognized: (String) -> Unit) {
    val context = LocalContext.current
    var isListening by remember { mutableStateOf(false) }
    var buttonText by remember { mutableStateOf("🎤 Сказать команду") }

    // Создаем фоновый распознаватель речи
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    // Настраиваем логику того, что происходит во время прослушивания
    DisposableEffect(Unit) {
        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { buttonText = "Слушаю..." }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                isListening = false
                buttonText = "Обработка..."
            }
            override fun onError(error: Int) {
                isListening = false
                buttonText = "Ошибка (нажмите снова)"
            }
            override fun onResults(results: Bundle?) {
                isListening = false
                buttonText = "🎤 Сказать команду"
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    onCommandRecognized(matches[0])
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
        speechRecognizer.setRecognitionListener(listener)
        onDispose { speechRecognizer.destroy() }
    }

    // Инструмент для запроса разрешения на микрофон при первом нажатии
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isListening = true
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU")
            }
            speechRecognizer.startListening(intent)
        } else {
            buttonText = "Нет доступа к микрофону"
        }
    }

    // Сама кнопка
    Button(
        onClick = {
            if (!isListening) {
                // Запрашиваем разрешение и запускаем прослушивание
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                // Если уже слушаем - останавливаем
                speechRecognizer.stopListening()
                isListening = false
                buttonText = "🎤 Сказать команду"
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(buttonText)
    }
}