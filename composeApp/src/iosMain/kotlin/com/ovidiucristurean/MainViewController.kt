package com.ovidiucristurean

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    App(
        phoneSensorManager = IosPhoneSensorManager()
    )
}
