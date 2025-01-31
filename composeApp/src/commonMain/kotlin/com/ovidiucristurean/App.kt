package com.ovidiucristurean

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ovidiucristurean.kmpsensorcollector.PhoneSensorManager
import com.ovidiucristurean.presentation.screen.SensorListViewModel
import com.ovidiucristurean.presentation.screen.SensorListViewModelFactory
import com.ovidiucristurean.presentation.screen.SensorScreen
import com.ovidiucristurean.presentation.theme.DarkColorScheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    phoneSensorManager: PhoneSensorManager,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme
    ) {
        val sensorListViewModel: SensorListViewModel = viewModel(
            factory = SensorListViewModelFactory(phoneSensorManager)
        )
        SensorScreen(sensorListViewModel)
    }
}
