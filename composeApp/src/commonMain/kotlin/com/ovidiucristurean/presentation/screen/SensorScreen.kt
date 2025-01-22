package com.ovidiucristurean.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ovidiucristurean.presentation.view.SensorList

@Composable
fun SensorScreen(
    viewModel: SensorListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val lifecycleObserver = remember {
        object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                viewModel.pauseAll()
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                viewModel.resumeAll()
            }
        }
    }

    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    SensorList(sensorItems = uiState.sensors.values.toList(),
        onSensorToggled = { sensorName, isToggled ->
            viewModel.onSensorToggled(sensorName, isToggled)
        }
    )
}