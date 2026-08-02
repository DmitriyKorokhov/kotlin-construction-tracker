package com.yourname.tracker

import androidx.compose.runtime.Composable

// реализация будет написана отдельно для каждой платформы
@Composable
expect fun VoiceRecordingButton(onCommandRecognized: (String) -> Unit)