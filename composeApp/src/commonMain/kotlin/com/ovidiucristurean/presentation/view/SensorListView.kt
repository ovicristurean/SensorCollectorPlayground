package com.ovidiucristurean.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ovidiucristurean.presentation.state.SensorCardInfo
import com.ovidiucristurean.presentation.state.SensorDataInfo

@Composable
fun SensorList(
    sensorItems: List<SensorCardInfo<SensorDataInfo>>,
    onSensorToggled: (String, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sensorItems.size) { index ->
            val sensorItem: SensorCardInfo<SensorDataInfo> = sensorItems[index]
            SensorCard(
                sensorCardInfo = sensorItem,
                onCheckedChanged = { isChecked ->
                    onSensorToggled(sensorItem.name, isChecked)
                },
                demoView = if (sensorItem.hasDemo) {
                    when (sensorItem) {
                        is SensorCardInfo.RotationCardInfo -> {
                            {
                                GradientView(
                                    pitch = sensorItem.data?.pitch?.toFloat() ?: 0.0f,
                                    roll = sensorItem.data?.roll?.toFloat() ?: 0.0f,
                                    rotationAngles = RotationAngles(
                                        startRange = -90f,
                                        endRange = 90f
                                    ),
                                    rotationAxis = RotationAxis.ROLL,
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(100.dp)
                                )
                            }
                        }

                        else -> null
                    }
                } else null
            )
        }
    }
}
