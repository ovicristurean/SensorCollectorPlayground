package com.ovidiucristurean

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.ovidiucristurean.domain.model.RotationModel
import com.ovidiucristurean.presentation.RotationSensorManager

class MainActivity : ComponentActivity() {

    private lateinit var rotationSensorManager: RotationSensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rotationSensorManager = RotationSensorManagerWrapper(this)

        setContent {
            App(
                rotationSensorManager = rotationSensorManager
            )
        }
    }

    override fun onResume() {
        super.onResume()
//        rotationVectorSensor?.let {
//            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
//        }
    }

    override fun onPause() {
        super.onPause()
        //sensorManager.unregisterListener(this)
        //rotationSensorManager.unregisterListener()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    //App()
}