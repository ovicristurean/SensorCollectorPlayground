package com.ovidiucristurean.presentation.state

import com.ovidiucristurean.kmpsensorcollector.model.SensorType

data class SensorScreenUiState(
    val sensors: Map<String, SensorCardInfo<SensorDataInfo>> = mapOf(
        SensorType.ROTATION_VECTOR.name to SensorCardInfo.RotationCardInfo(
            isAvailable = true,
            name = ROTATION_NAME,
            isToggledOn = false,
            hasDemo = true,
            data = null,
            showDemo = false
        ),
        SensorType.ACCELEROMETER.name to SensorCardInfo.AccelerometerCardInfo(
            isAvailable = true,
            name = ACCELEROMETER_NAME,
            isToggledOn = false,
            hasDemo = false,
            data = null
        )
    )
)

const val ROTATION_NAME = "Rotation"
const val ACCELEROMETER_NAME = "Accelerometer"
