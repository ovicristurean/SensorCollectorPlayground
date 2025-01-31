package com.ovidiucristurean.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import sensorcollector.composeapp.generated.resources.Res
import sensorcollector.composeapp.generated.resources.visa

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
    val highlightCenter = mapTo01(
        roll,
        rotationAngles.startRange,
        rotationAngles.endRange
    )

    val highlightWidth = 0.1f

    val colorStops = arrayOf(
        0f to Color.Transparent,
        (highlightCenter - highlightWidth).coerceIn(
            0f,
            1f
        ) to Color.Transparent,
        highlightCenter to Color.Gray.copy(alpha = 0.3f),
        (highlightCenter + highlightWidth).coerceIn(0f, 1f) to Color.Transparent,
        1f to Color.Transparent
    )

    val brush = when (rotationAxis) {
        RotationAxis.AZIMUTH -> {
            Brush.horizontalGradient(
                colorStops = colorStops
            )
        }

        RotationAxis.PITCH -> {
            Brush.verticalGradient(
                colorStops = colorStops
            )
        }

        RotationAxis.ROLL -> {
            Brush.linearGradient(
                colorStops = colorStops
            )
        }
    }

    val normalizedPitch = (pitch / 180f) * 90f // Normalize pitch to [-45, 45]
    val normalizedRoll = (roll / 90f) * 45f // Normalize roll to [-45, 45]

    Box(
        modifier = Modifier.fillMaxSize()
            .graphicsLayer {
                rotationX = normalizedPitch // Apply normalized pitch for X rotation
                rotationY = normalizedRoll // Apply normalized roll for Y rotation
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = modifier
                .clip(RoundedCornerShape(24.dp)),
            painter = painterResource(Res.drawable.visa),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(24.dp))
                .background(brush)
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
