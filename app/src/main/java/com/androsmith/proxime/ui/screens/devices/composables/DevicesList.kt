package com.androsmith.proxime.ui.screens.devices.composables

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun DeviceList(
    onConnect: (BluetoothDevice) -> Unit,
    devices: List<BluetoothDevice>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(devices) { device ->
            DeviceTile(
                onConnect = onConnect, device = device
            )
        }
    }
}