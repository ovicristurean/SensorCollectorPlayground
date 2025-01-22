package com.ovidiucristurean.presentation.state

data class AppScreenUiState(
    val sensors: Map<String, SensorCardInfo<SensorDataInfo>> = mapOf(
        ROTATION_ITEM_NAME to SensorCardInfo.RotationCardInfo(
            name = ROTATION_ITEM_NAME,
            isToggledOn = false,
            hasDemo = true,
            data = null,
            showDemo = false
        ),
        ACCELEROMETER_ITEM_NAME to SensorCardInfo.AccelerometerCardInfo(
            name = ACCELEROMETER_ITEM_NAME,
            isToggledOn = false,
            hasDemo = false,
            data = null
        )
    )
)

const val ROTATION_ITEM_NAME = "Rotation"
const val ACCELEROMETER_ITEM_NAME = "Accelerometer"