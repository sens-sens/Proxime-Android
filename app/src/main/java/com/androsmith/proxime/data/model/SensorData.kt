package com.androsmith.proxime.data.model

data class SensorData(
    val isProximityBlocked: Boolean,
    val isButtonPressed: Boolean,
    val connectionState: ConnectionState,
)