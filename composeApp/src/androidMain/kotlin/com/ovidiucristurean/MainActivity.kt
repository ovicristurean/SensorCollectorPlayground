package com.ovidiucristurean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ovidiucristurean.presentation.PhoneSensorManager

class MainActivity : ComponentActivity() {

    private lateinit var phoneSensorManager: PhoneSensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneSensorManager = AndroidPhoneSensorManager(this)

        setContent {
            App(
                phoneSensorManager = phoneSensorManager
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