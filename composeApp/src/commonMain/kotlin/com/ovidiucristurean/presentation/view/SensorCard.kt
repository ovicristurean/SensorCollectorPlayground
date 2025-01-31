package com.ovidiucristurean.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.ovidiucristurean.presentation.state.SensorCardInfo
import com.ovidiucristurean.presentation.state.SensorDataInfo

@Composable
fun SensorCard(
    sensorCardInfo: SensorCardInfo<SensorDataInfo>,
    onCheckedChanged: (Boolean) -> Unit,
    demoView: (@Composable () -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .alpha(if (sensorCardInfo.isAvailable) 1f else 0.3f),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = sensorCardInfo.name,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (sensorCardInfo.data != null) {
                when (sensorCardInfo) {
                    is SensorCardInfo.RotationCardInfo -> {
                        Text(text = "Azimuth: ${sensorCardInfo.data!!.azimuth}")
                        Text(text = "Pitch: ${sensorCardInfo.data.pitch}")
                        Text(text = "Roll: ${sensorCardInfo.data.roll}")
                    }

                    is SensorCardInfo.AccelerometerCardInfo -> {
                        Text(text = "X: ${sensorCardInfo.data!!.x}")
                        Text(text = "Y: ${sensorCardInfo.data.y}")
                        Text(text = "Z: ${sensorCardInfo.data.z}")
                    }
                }
            }

            Switch(
                checked = sensorCardInfo.isToggledOn,
                onCheckedChange = { isChecked ->
                    if (sensorCardInfo.isAvailable) {
                        onCheckedChanged(isChecked)
                    }
                }
            )

            if (demoView != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (sensorCardInfo.isAvailable) {
                                isExpanded = !isExpanded
                            }
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isExpanded) "Hide Demo" else "Show Demo",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                    )
                }

                AnimatedVisibility(visible = isExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        demoView()
                    }
                }
            }
        }
    }
}
