package com.yourname.tracker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "kotline-construction-tracker",
    ) {
        App()
    }
}