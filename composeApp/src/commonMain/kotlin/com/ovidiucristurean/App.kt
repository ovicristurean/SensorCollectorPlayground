package com.ovidiucristurean

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.ovidiucristurean.presentation.GradientView
import com.ovidiucristurean.presentation.RotationAngles
import com.ovidiucristurean.presentation.RotationAxis
import com.ovidiucristurean.presentation.RotationSensorManager
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    rotationSensorManager: RotationSensorManager,
) {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val rotation by rotationSensorManager.rotation.collectAsState()
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${rotation.azimuth} ${rotation.pitch} ${rotation.roll}")

                    Text("Azimuth: ${rotation.azimuth}")
                    GradientView(
                        roll = rotation.azimuth.toFloat(),
                        rotationAngles = RotationAngles(
                            startRange = -180f,
                            endRange = 180f
                        ),
                        rotationAxis = RotationAxis.AZIMUTH,
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Pitch: ${rotation.pitch}")
                    GradientView(
                        roll = rotation.pitch.toFloat(),
                        rotationAngles = RotationAngles(
                            startRange = -90f,
                            endRange = 90f
                        ),
                        rotationAxis = RotationAxis.PITCH,
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Roll: ${rotation.roll}")
                    GradientView(
                        roll = rotation.roll.toFloat(),
                        rotationAngles = RotationAngles(
                            startRange = -90f,
                            endRange = 90f
                        ),
                        rotationAxis = RotationAxis.ROLL,
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                            .padding(horizontal = 20.dp).graphicsLayer {
                            rotationY = rotation.roll.toFloat()
                        }
                    )
                }
            }
        }
    }
}
