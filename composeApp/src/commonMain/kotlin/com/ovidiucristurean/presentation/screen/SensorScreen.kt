package com.ovidiucristurean.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ovidiucristurean.presentation.view.SensorList

@Composable
fun SensorScreen(
    viewModel: SensorListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    SensorList(sensorItems = uiState.sensors,
        onRegisterClicked = { sensorItem ->
            viewModel.register(sensorItem)
        },
        onUnregisterClicked = { sensorItem ->
            viewModel.unregister(sensorItem)
        })
}
