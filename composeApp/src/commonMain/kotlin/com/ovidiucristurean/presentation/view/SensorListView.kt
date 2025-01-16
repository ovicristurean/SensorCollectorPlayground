package com.ovidiucristurean.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ovidiucristurean.presentation.state.SensorData

@Composable
fun SensorList(
    sensorItems: Map<String, SensorData?>,
    onRegisterClicked: (String) -> Unit,
    onUnregisterClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sensorItems.size) { index ->
            val sensorName = sensorItems.keys.elementAt(index)
            val sensorItem: SensorData? = sensorItems.values.elementAt(index)
            SensorCard(
                sensorName = sensorName,
                sensorData = sensorItem,
                onRegisterClicked = {
                    onRegisterClicked(sensorName)
                },
                onUnregisterClicked = {
                    onUnregisterClicked(sensorName)
                })
        }
    }
}

@Composable
fun SensorCard(
    sensorName: String,
    sensorData: SensorData?,
    onRegisterClicked: () -> Unit,
    onUnregisterClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        //elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = sensorName,
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (sensorData != null) {
                when (sensorData) {
                    is SensorData.RotationData -> {
                        Text(text = "Azimuth: ${sensorData.azimuth}")
                        Text(text = "Pitch: ${sensorData.pitch}")
                        Text(text = "Roll: ${sensorData.roll}")
                    }

                    is SensorData.AccelerometerData -> {
                        Text(text = "X: ${sensorData.x}")
                        Text(text = "Y: ${sensorData.y}")
                        Text(text = "Z: ${sensorData.z}")
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                Button(onClick = {
                    onRegisterClicked()
                }) {
                    Text("Register")
                }

                Button(onClick = {
                    onUnregisterClicked()
                }) {
                    Text("Unregister")
                }
            }
        }
    }
}
