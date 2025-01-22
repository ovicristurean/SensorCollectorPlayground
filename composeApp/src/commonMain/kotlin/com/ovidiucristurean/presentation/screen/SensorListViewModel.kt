package com.ovidiucristurean.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.ovidiucristurean.kmpsensorcollector.PhoneSensorManager
import com.ovidiucristurean.kmpsensorcollector.model.SensorType
import com.ovidiucristurean.presentation.state.ACCELEROMETER_ITEM_NAME
import com.ovidiucristurean.presentation.state.AppScreenUiState
import com.ovidiucristurean.presentation.state.ROTATION_ITEM_NAME
import com.ovidiucristurean.presentation.state.SensorCardInfo
import com.ovidiucristurean.presentation.state.SensorDataInfo
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
            ROTATION_ITEM_NAME -> {
                viewModelScope.launch {
                    phoneSensorManager.rotationData.collectLatest { rotationData ->
                        println("OVI: rotation ${rotationData.roll}")
                        _uiState.update { currentState ->
                            currentState.copy(
                                sensors = currentState.sensors.toMutableMap().apply {
                                    this[sensorName] =
                                        (this[sensorName] as SensorCardInfo.RotationCardInfo).copy(
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

            ACCELEROMETER_ITEM_NAME -> {
                viewModelScope.launch {
                    phoneSensorManager.accelerometerData.collectLatest { accelerometerData ->
                        println("OVI: accelerometer ${accelerometerData.accelerationX}")
                        _uiState.update { currentState ->
                            currentState.copy(
                                sensors = currentState.sensors.toMutableMap().apply {
                                    this[sensorName] =
                                        (this[sensorName] as SensorCardInfo.AccelerometerCardInfo)
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
            ROTATION_ITEM_NAME -> {
                SensorType.ROTATION_VECTOR
            }

            ACCELEROMETER_ITEM_NAME -> {
                SensorType.ACCELEROMETER
            }

            else -> null
        }

        sensorType?.let {
            _uiState.update { currentState ->
                val updatedSensors = currentState.sensors.mapValues { (sensorKey, sensorInfo) ->
                    if (sensorKey == sensorName) {
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
                        ROTATION_ITEM_NAME ->
                            this[sensorName] =
                                (this[sensorName] as SensorCardInfo.RotationCardInfo).copy(
                                    isToggledOn = isToggledOn
                                )

                        ACCELEROMETER_ITEM_NAME -> {
                            this[sensorName] =
                                (this[sensorName] as SensorCardInfo.AccelerometerCardInfo).copy(
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
