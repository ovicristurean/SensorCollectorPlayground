package com.ovidiucristurean

import android.app.Activity.SENSOR_SERVICE
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.ovidiucristurean.domain.model.RotationModel
import com.ovidiucristurean.presentation.RotationSensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RotationSensorManagerWrapper(
    context: Context,
) : RotationSensorManager, SensorEventListener {
    private var rotationVectorSensor: Sensor? = null

    private var scope = CoroutineScope(Dispatchers.Main)
    private val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager

    init {
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
    }

    private val _roll = MutableStateFlow(
        RotationModel(
            azimuth = 0.toDouble(),
            pitch = 0.toDouble(),
            roll = 0.toDouble()
        )
    )

    override val rotation: StateFlow<RotationModel>
        get() = _roll

    override fun unregisterListener() {
        sensorManager.unregisterListener(this)
        scope.cancel()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            val orientationAngles = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            val azimuth = Math.toDegrees(orientationAngles[0].toDouble()) // Rotation around Z-axis
            val pitch = Math.toDegrees(orientationAngles[1].toDouble())   // Rotation around X-axis
            val roll = Math.toDegrees(orientationAngles[2].toDouble())    // Rotation around Y-axis

            scope.launch {
                _roll.emit(
                    RotationModel(
                        azimuth = azimuth,
                        pitch = pitch,
                        roll = roll
                    )
                )
            }

            println("OVI: Azimuth: $azimuth, Pitch: $pitch, Roll: $roll")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}