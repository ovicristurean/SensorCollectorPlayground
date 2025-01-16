package com.ovidiucristurean

import androidx.compose.ui.window.ComposeUIViewController
import com.ovidiucristurean.kmpsensorcollector.IosPhoneSensorManager

fun MainViewController() = ComposeUIViewController {
    App(
        phoneSensorManager = IosPhoneSensorManager()
    )
}
