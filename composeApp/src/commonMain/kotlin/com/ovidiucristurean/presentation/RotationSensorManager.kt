package com.ovidiucristurean.presentation

import com.ovidiucristurean.domain.model.RotationModel
import kotlinx.coroutines.flow.StateFlow

interface RotationSensorManager {
    val rotation: StateFlow<RotationModel>

    fun unregisterListener()
}