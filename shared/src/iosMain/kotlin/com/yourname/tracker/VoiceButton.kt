package com.yourname.tracker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun VoiceRecordingButton(onCommandRecognized: (String) -> Unit) {
    Button(
        onClick = { /* TODO: Добавим позже через Apple Speech Framework */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("🎤 Голос (в разработке)")
    }
}