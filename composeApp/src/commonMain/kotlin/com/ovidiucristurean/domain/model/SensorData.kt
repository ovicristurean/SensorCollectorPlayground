package com.ovidiucristurean.domain.model

sealed interface SensorData {

    data class RotationData(
        val azimuth: Double,
        val pitch: Double,
        val roll: Double
    ) : SensorData

    data class AccelerometerData(
        val acceleration: Float
    ) : SensorData
}