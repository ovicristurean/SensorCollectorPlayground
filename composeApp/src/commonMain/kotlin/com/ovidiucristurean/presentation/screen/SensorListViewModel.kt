package com.ovidiucristurean.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.ovidiucristurean.kmpsensorcollector.PhoneSensorManager
import com.ovidiucristurean.kmpsensorcollector.model.SensorType
import com.ovidiucristurean.presentation.state.ACCELEROMETER_NAME
import com.ovidiucristurean.presentation.state.ROTATION_NAME
import com.ovidiucristurean.presentation.state.SensorCardInfo
import com.ovidiucristurean.presentation.state.SensorDataInfo
import com.ovidiucristurean.presentation.state.SensorScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class SensorListViewModel(
    private val phoneSensorManager: PhoneSensorManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SensorScreenUiState())
    val uiState: StateFlow<SensorScreenUiState> = _uiState

    init {
        _uiState.update { currentState ->
            val updatedSensors = currentState.sensors.mapValues { (sensorName, sensorCardInfo) ->
                val sensorType = SensorType.valueOf(sensorName)
                if (!phoneSensorManager.isSensorAvailable(sensorType)) {
                    when (sensorCardInfo) {
                        is SensorCardInfo.RotationCardInfo -> sensorCardInfo.copy(isAvailable = false)
                        is SensorCardInfo.AccelerometerCardInfo -> sensorCardInfo.copy(isAvailable = false)
                    }
                } else {
                    sensorCardInfo
                }
            }

            currentState.copy(sensors = updatedSensors)
        }
    }

    fun onSensorToggled(sensorName: String, isToggledOn: Boolean) {
        if (isToggledOn) {
            toggleSensor(sensorName, true)
            register(sensorName)
        } else {
            toggleSensor(sensorName, false)
            unregister(sensorName)
        }
    }

    fun pauseAll() {
        uiState.value.sensors.forEach {
            if (it.value.isToggledOn) {
                unregister(it.value.name)
            }
        }
    }

    fun resumeAll() {
        uiState.value.sensors.forEach {
            if (it.value.isToggledOn) {
                register(it.value.name)
            }
        }
    }

    private fun register(sensorName: String) {
        toggleSensor(sensorName, true)
        when (sensorName) {
            ROTATION_NAME -> {
                viewModelScope.launch {
                    phoneSensorManager.rotationData.collectLatest { rotationData ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                sensors = currentState.sensors.toMutableMap().apply {
                                    this[SensorType.ROTATION_VECTOR.name] =
                                        (this[SensorType.ROTATION_VECTOR.name] as SensorCardInfo.RotationCardInfo).copy(
                                            data = SensorDataInfo.RotationDataInfo(
                                                azimuth = rotationData.azimuth,
                                                pitch = rotationData.pitch,
                                                roll = rotationData.roll
                                            )
                                        )
                                }
                            )
                        }
                    }
                }
                phoneSensorManager.registerSensor(SensorType.ROTATION_VECTOR)
            }

            ACCELEROMETER_NAME -> {
                viewModelScope.launch {
                    phoneSensorManager.accelerometerData.collectLatest { accelerometerData ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                sensors = currentState.sensors.toMutableMap().apply {
                                    this[SensorType.ACCELEROMETER.name] =
                                        (this[SensorType.ACCELEROMETER.name] as SensorCardInfo.AccelerometerCardInfo)
                                            .copy(
                                                data = SensorDataInfo.AccelerometerDataInfo(
                                                    x = accelerometerData.accelerationX,
                                                    y = accelerometerData.accelerationY,
                                                    z = accelerometerData.accelerationZ
                                                )
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

    private fun unregister(sensorName: String) {
        val sensorType = when (sensorName) {
            ROTATION_NAME -> {
                SensorType.ROTATION_VECTOR
            }

            ACCELEROMETER_NAME -> {
                SensorType.ACCELEROMETER
            }

            else -> null
        }

        sensorType?.let {
            _uiState.update { currentState ->
                val updatedSensors = currentState.sensors.mapValues { (sensorKey, sensorInfo) ->
                    if (sensorKey == sensorType.name) {
                        sensorInfo.clearData()
                    } else {
                        sensorInfo
                    }
                }
                currentState.copy(sensors = updatedSensors)
            }
            phoneSensorManager.unregisterSensor(sensorType)
        }

    }

    private fun toggleSensor(sensorName: String, isToggledOn: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                sensors = currentState.sensors.toMutableMap().apply {
                    when (sensorName) {
                        ROTATION_NAME ->
                            this[SensorType.ROTATION_VECTOR.name] =
                                (this[SensorType.ROTATION_VECTOR.name] as SensorCardInfo.RotationCardInfo).copy(
                                    isToggledOn = isToggledOn
                                )

                        ACCELEROMETER_NAME -> {
                            this[SensorType.ACCELEROMETER.name] =
                                (this[SensorType.ACCELEROMETER.name] as SensorCardInfo.AccelerometerCardInfo).copy(
                                    isToggledOn = isToggledOn
                                )
                        }
                    }
                }
            )
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
