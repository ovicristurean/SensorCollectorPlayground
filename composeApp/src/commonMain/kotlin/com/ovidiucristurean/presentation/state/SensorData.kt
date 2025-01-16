package com.ovidiucristurean.presentation.state

sealed interface SensorData {
    data class RotationData(
        val azimuth: Double,
        val pitch: Double,
        val roll: Double
    ) : SensorData

    data class AccelerometerData(
        val x: Float,
        val y: Float,
        val z: Float
    ) : SensorData
}