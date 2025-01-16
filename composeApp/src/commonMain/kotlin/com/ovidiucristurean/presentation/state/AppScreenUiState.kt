package com.ovidiucristurean.presentation.state

data class AppScreenUiState(
    val sensors: Map<String, SensorData?> = mapOf(
        "rotation" to null,
        "accelerometer" to null
    )
)
