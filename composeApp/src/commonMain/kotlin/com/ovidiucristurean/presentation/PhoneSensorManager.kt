package com.ovidiucristurean.presentation

import com.ovidiucristurean.domain.model.SensorData
import com.ovidiucristurean.domain.model.SensorType
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface PhoneSensorManager {
    val sensorData: SharedFlow<SensorData>

    fun registerSensor(sensorType: SensorType)
    fun unregisterSensor(sensorType: SensorType)
}
