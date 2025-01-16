package com.ovidiucristurean.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.ovidiucristurean.kmpsensorcollector.PhoneSensorManager
import com.ovidiucristurean.kmpsensorcollector.model.SensorType
import com.ovidiucristurean.presentation.state.AppScreenUiState
import com.ovidiucristurean.presentation.state.SensorData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class SensorListViewModel(
    private val phoneSensorManager: PhoneSensorManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppScreenUiState())
    val uiState: StateFlow<AppScreenUiState> = _uiState

    fun register(sensorName: String) {
        when (sensorName) {
            "rotation" -> {
                viewModelScope.launch {
                    phoneSensorManager.rotationData.collectLatest { rotationData ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                sensors = currentState.sensors.toMutableMap().apply {
                                    this[sensorName] = SensorData.RotationData(
                                        azimuth = rotationData.azimuth,
                                        pitch = rotationData.pitch,
                                        roll = rotationData.roll
                                    )
                                }
                            )
                        }
                    }
                }
                phoneSensorManager.registerSensor(SensorType.ROTATION_VECTOR)
            }

            "accelerometer" -> {
                viewModelScope.launch {
                    phoneSensorManager.accelerometerData.collectLatest { accelerometerData ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                sensors = currentState.sensors.toMutableMap().apply {
                                    this[sensorName] = SensorData.AccelerometerData(
                                        x = accelerometerData.accelerationX,
                                        y = accelerometerData.accelerationY,
                                        z = accelerometerData.accelerationZ
                                    )
                                }
                            )

                        }
                    }
                }
                phoneSensorManager.registerSensor(SensorType.ACCELEROMETER)
            }
        }
    }

    fun unregister(sensorName: String) {
        val sensorType = when (sensorName) {
            "rotation" -> {
                SensorType.ROTATION_VECTOR
            }

            "accelerometer" -> {
                SensorType.ACCELEROMETER
            }

            else -> null
        }

        sensorType?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    sensors = currentState.sensors.toMutableMap().apply {
                        this[sensorName] = null
                    }
                )
            }
            phoneSensorManager.unregisterSensor(sensorType)
        }
    }
}

class SensorListViewModelFactory(
    private val phoneSensorManager: PhoneSensorManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return SensorListViewModel(phoneSensorManager) as T
    }
}
