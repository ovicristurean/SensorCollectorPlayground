package com.ovidiucristurean.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * properties:
 * angle range for gradient
 *
 */
@Composable
fun GradientView(
    pitch: Float,
    roll: Float,
    rotationAngles: RotationAngles,
    rotationAxis: RotationAxis,
    modifier: Modifier = Modifier
) {
    val brush = when (rotationAxis) {
        RotationAxis.AZIMUTH -> {
            Brush.horizontalGradient(
                colorStops = arrayOf(
                    0f to MaterialTheme.colors.secondary,
                    mapTo01(
                        roll,
                        rotationAngles.startRange,
                        rotationAngles.endRange
                    ) to Color.Transparent,
                    1f to MaterialTheme.colors.secondary
                )
            )
        }

        RotationAxis.PITCH -> {
            Brush.verticalGradient(
                colorStops = arrayOf(
                    0f to MaterialTheme.colors.secondary,
                    mapTo01(
                        roll,
                        rotationAngles.startRange,
                        rotationAngles.endRange
                    ) to Color.Transparent,
                    1f to MaterialTheme.colors.secondary
                )
            )
        }

        RotationAxis.ROLL -> {
            Brush.linearGradient(
                colorStops = arrayOf(
                    0f to MaterialTheme.colors.secondary,
                    mapTo01(
                        roll,
                        rotationAngles.startRange,
                        rotationAngles.endRange
                    ) to Color.Transparent,
                    1f to MaterialTheme.colors.secondary
                )
            )
        }
    }

    Card(
        modifier = modifier.padding(horizontal = 20.dp).graphicsLayer {
            rotationX = pitch
            rotationY = roll
        },
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier.background(brush = brush)
        )
    }
}

private fun mapTo01(value: Float, minValue: Float, maxValue: Float): Float {
    return (value - minValue) / (maxValue - minValue)
}

data class RotationAngles(
    val startRange: Float,
    val endRange: Float
)

enum class RotationAxis {
    AZIMUTH,
    PITCH,
    ROLL
}
