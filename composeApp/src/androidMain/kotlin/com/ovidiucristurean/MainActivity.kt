package com.ovidiucristurean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ovidiucristurean.kmpsensorcollector.AndroidPhoneSensorManager

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val androidSensorManager = AndroidPhoneSensorManager(this)
            App(
                phoneSensorManager = androidSensorManager
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(phoneSensorManager = AndroidPhoneSensorManager(LocalContext.current))
}