package com.ovidiucristurean

import com.ovidiucristurean.domain.model.SensorData
import com.ovidiucristurean.domain.model.SensorType
import com.ovidiucristurean.presentation.PhoneSensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue
import kotlin.math.PI

class IosPhoneSensorManager : PhoneSensorManager {

    private val motionManager = CMMotionManager()
    private var scope: CoroutineScope? = null

    private val _roll = MutableStateFlow(
        SensorData.RotationData(
            azimuth = 0.toDouble(),
            pitch = 0.toDouble(),
            roll = 0.toDouble()
        )
    )

    override val sensorData: StateFlow<SensorData>
        get() = _roll

    override fun registerSensor(sensorType: SensorType) {
        if (motionManager.isDeviceMotionAvailable()) {
            motionManager.deviceMotionUpdateInterval = 1.0 / 60.0 // 60 Hz
            motionManager.startDeviceMotionUpdatesToQueue(NSOperationQueue.mainQueue) { motion, error ->
                motion?.let {
                    val attitude = it.attitude
                    val azimuth = convertLongToDegrees(attitude.yaw)   // Rotation around Z-axis
                    val pitch = convertLongToDegrees(attitude.pitch)  // Rotation around X-axis
                    val roll = convertLongToDegrees(attitude.roll)    // Rotation around Y-axis

                    scope?.launch {
                        _roll.emit(
                            SensorData.RotationData(
                                azimuth = azimuth,
                                pitch = pitch,
                                roll = roll
                            )
                        )
                    }

                    println("iOS: Azimuth: $azimuth, Pitch: $pitch, Roll: $roll")
                }

                error?.let {
                    println("Error: ${it.localizedDescription}")
                }
            }
        } else {
            println("Device motion is not available")
        }
    }

    override fun unregisterSensor(sensorType: SensorType) {
        motionManager.stopDeviceMotionUpdates()
        scope?.cancel()
        scope = null
    }


    private fun convertLongToDegrees(radians: Double): Double {
        return radians * (180.0 / PI)
    }

}