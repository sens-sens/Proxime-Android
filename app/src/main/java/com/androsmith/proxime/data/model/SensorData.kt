package com.androsmith.proxime.data.model

data class SensorData(
    val isProximityBlocked: Boolean,
    val isButtonPressed: Boolean,
    val connectionState: ConnectionState,
) {
    companion object {
        fun default(): SensorData {
            return SensorData(
                isProximityBlocked = false,
                isButtonPressed = false,
                connectionState = ConnectionState.Disconnected
            )
        }
    }
}