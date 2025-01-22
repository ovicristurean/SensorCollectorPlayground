package com.ovidiucristurean.presentation.state

sealed interface SensorCardInfo<out T : SensorDataInfo> {
    val name: String
    val isToggledOn: Boolean
    val hasDemo: Boolean
    val data: T?

    fun clearData(): SensorCardInfo<T>
   // fun toggle(isToggledOn: Boolean): SensorCardInfo<T>

    data class RotationCardInfo(
        override val name: String,
        override val isToggledOn: Boolean,
        override val hasDemo: Boolean,
        override val data: SensorDataInfo.RotationDataInfo?,
        val showDemo: Boolean,
    ) : SensorCardInfo<SensorDataInfo.RotationDataInfo> {
        override fun clearData(): SensorCardInfo<SensorDataInfo.RotationDataInfo> =
            copy(data = null)

//        override fun toggle(isToggledOn: Boolean): SensorCardInfo<SensorDataInfo.RotationDataInfo> =
//            copy(isToggledOn = isToggledOn)

    }

    data class AccelerometerCardInfo(
        override val name: String,
        override val isToggledOn: Boolean,
        override val hasDemo: Boolean,
        override val data: SensorDataInfo.AccelerometerDataInfo?
    ) : SensorCardInfo<SensorDataInfo.AccelerometerDataInfo> {
        override fun clearData(): SensorCardInfo<SensorDataInfo.AccelerometerDataInfo> =
            copy(data = null)

//        override fun toggle(isToggledOn: Boolean): SensorCardInfo<SensorDataInfo.AccelerometerDataInfo> =
//            copy(isToggledOn = isToggledOn)
    }
}

sealed interface SensorDataInfo {
    data class RotationDataInfo(
        val azimuth: Double,
        val pitch: Double,
        val roll: Double
    ) : SensorDataInfo

    data class AccelerometerDataInfo(
        val x: Float,
        val y: Float,
        val z: Float
    ) : SensorDataInfo
}
