package com.ovidiucristurean

import com.ovidiucristurean.domain.model.RotationModel
import com.ovidiucristurean.presentation.RotationSensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue
import kotlin.math.PI

class RotationSensorManagerWrapper : RotationSensorManager {

    private val motionManager = CMMotionManager()
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _roll = MutableStateFlow(
        RotationModel(
            azimuth = 0.toDouble(),
            pitch = 0.toDouble(),
            roll = 0.toDouble()
        )
    )

    init {
        if (motionManager.isDeviceMotionAvailable()) {
            motionManager.deviceMotionUpdateInterval = 1.0 / 60.0 // 60 Hz
            motionManager.startDeviceMotionUpdatesToQueue(NSOperationQueue.mainQueue) { motion, error ->
                motion?.let {
                    val attitude = it.attitude
                    val azimuth = convertLongToDegrees(attitude.yaw)   // Rotation around Z-axis
                    val pitch = convertLongToDegrees(attitude.pitch)  // Rotation around X-axis
                    val roll = convertLongToDegrees(attitude.roll)    // Rotation around Y-axis

                    scope.launch {
                        _roll.emit(
                            RotationModel(
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

    override val rotation: StateFlow<RotationModel>
        get() = _roll

    override fun unregisterListener() {
        motionManager.stopDeviceMotionUpdates()
        scope.cancel()
    }


    private fun convertLongToDegrees(radians: Double): Double {
        return radians * (180.0 / PI)
    }

}