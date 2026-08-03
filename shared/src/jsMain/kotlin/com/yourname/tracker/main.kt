package com.yourname.tracker

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        // 1. Строгая инициализация Firebase до запуска интерфейса
        try {
            val options = FirebaseOptions(
                applicationId = "1:435167258859:web:3686b11ce3278857c2b6ae",
                apiKey = "AIzaSyC0oL9k5l1v_psRhIhtTm5x_FTvZvUr-Jo",
                projectId = "kotlin-construction-tracker"
            )
            // Сохраняем в переменную и выводим в лог (защита от вырезания компилятором)
            val app = Firebase.initialize(options)
            println("✅ Firebase успешно инициализирован: $app")
        } catch (e: Exception) {
            println("❌ Ошибка инициализации Firebase: ${e.message}")
        }

        // 2. Только после этого запускаем Compose
        val canvasElement = document.getElementById("ComposeTarget") as HTMLCanvasElement
        ComposeViewport(canvasElement) {
            App()
        }
    }
}