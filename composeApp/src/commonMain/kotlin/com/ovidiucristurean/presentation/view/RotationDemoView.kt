package com.ovidiucristurean.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ovidiucristurean.kmpsensorcollector.model.RotationData

@Composable
fun RotationDemoView(
    modifier: Modifier = Modifier,
    isRotationCollected: Boolean,
    rotation: RotationData?,
    onRegisterClicked: () -> Unit,
    onUnregisterClicked: () -> Unit,
    onPauseCollection: () -> Unit,
    onResumeCollection: () -> Unit
) {
    LocalLifecycleOwner.current.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
            if (isRotationCollected) {
                println("OVI: on pause; unregister")
                onPauseCollection()
            }
        }

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            if (isRotationCollected) {
                println("OVI: onResume; register")
                onResumeCollection()
            }
        }
    })

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onRegisterClicked
            ) {
                Text("Register")
            }

            Button(
                onClick = onUnregisterClicked
            ) {
                Text("Unregister")
            }
        }
        Text("${rotation?.azimuth} ${rotation?.pitch} ${rotation?.roll}")

        Text("Azimuth: ${rotation?.azimuth}")
        GradientView(
            roll = rotation?.azimuth?.toFloat() ?: 0.0f,
            rotationAngles = RotationAngles(
                startRange = -180f,
                endRange = 180f
            ),
            rotationAxis = RotationAxis.AZIMUTH,
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Pitch: ${rotation?.pitch}")
        GradientView(
            roll = rotation?.pitch?.toFloat() ?: 0.0f,
            rotationAngles = RotationAngles(
                startRange = -90f,
                endRange = 90f
            ),
            rotationAxis = RotationAxis.PITCH,
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Roll: ${rotation?.roll}")
        GradientView(
            roll = rotation?.roll?.toFloat() ?: 0.0f,
            rotationAngles = RotationAngles(
                startRange = -90f,
                endRange = 90f
            ),
            rotationAxis = RotationAxis.ROLL,
            modifier = Modifier.fillMaxWidth().height(100.dp)
                .padding(horizontal = 20.dp).graphicsLayer {
                    rotationY = rotation?.roll?.toFloat() ?: 0.0f
                }
        )
    }
}
