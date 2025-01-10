package com.ovidiucristurean

import android.app.Activity.SENSOR_SERVICE
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.ovidiucristurean.domain.model.SensorData
import com.ovidiucristurean.domain.model.SensorType
import com.ovidiucristurean.presentation.PhoneSensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AndroidPhoneSensorManager(
    context: Context,
) : PhoneSensorManager, SensorEventListener {

    private val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    private var rotationVectorSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private var scope: CoroutineScope? = null

    private val _sensorData = MutableSharedFlow<SensorData>()
    override val sensorData: SharedFlow<SensorData>
        get() = _sensorData

    override fun registerSensor(sensorType: SensorType) {
        scope = CoroutineScope(Dispatchers.Main)
        when (sensorType) {
            SensorType.ROTATION_VECTOR -> {
                sensorManager.registerListener(
                    this,
                    rotationVectorSensor,
                    SensorManager.SENSOR_DELAY_UI
                )
            }

            SensorType.ACCELEROMETER -> {

            }
        }
    }

    override fun unregisterSensor(sensorType: SensorType) {
        when (sensorType) {
            SensorType.ROTATION_VECTOR -> {
                sensorManager.unregisterListener(this, rotationVectorSensor)
            }

            SensorType.ACCELEROMETER -> {

            }
        }
        scope?.cancel()
        scope = null
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

            scope?.launch {
                _sensorData.emit(
                    SensorData.RotationData(
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